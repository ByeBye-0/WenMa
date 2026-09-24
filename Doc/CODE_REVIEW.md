# 问码教育 代码审查报告

**审查范围**：`WenMa`（Vue 3 + Vite 前端，23 个源文件）、`WenMaService`（Spring Boot 3.5.16 + MyBatis-Plus 3.5.16 后端）
**审查时间**：2026-09-24
**方法**：全量源码精读（非抽样）+ Git 入库状态核验 + 依赖声明交叉校验
**发现问题**：15 项（P0 紧急 5 项 / P1 高 4 项 / P2 中 6 项）

---

## 一、总体结论

**这套代码目前不具备上线条件。** 核心问题不在代码风格，而在**认证链路形同虚设**：JWT 拦截器注册了对全部路径的拦截，但由于一个字符串字面量错误，实际从未拦住任何请求；即便拦住也没校验签名。叠加"支付绕过"与"明文密码"两处缺陷，任何能访问到该服务的人都可以：无需登录读取任意学生的课表与成绩、零元购买全部付费课程、以及直接拿到全部账号密码。

数据存储层本身是干净的——全部使用 MyBatis-Plus `LambdaQueryWrapper` 预编译参数绑定，**未发现 SQL 注入**；前端也无 `v-html` / `innerHTML` 等 XSS 汇聚点。这两点值得肯定。

---

## 二、P0 紧急问题（建议立即修复）

### P0-1　JWT 认证完全失效，全站接口裸奔

**位置**：`WenMaService/WenMa/Service/src/main/java/com/smy/WenMa/Interceptor/JwtTokenInterceptor.java:19-33`
**注册范围**：`config/InterceptorConfig.java:17` — `addPathPatterns("/**")`（覆盖全部接口）
**严重程度**：🔴 致命（未授权访问 + 身份伪造）

三重缺陷叠加：

```java
if (url.contains("/stucourses/ids/${id}"))   // ① 字面量占位符，永远不会命中
{
   DecodedJWT res = jwt.parseToken(token);   // ② parseToken = JWT.decode，不校验签名
   if (res == null) { return false; }        // ③ 未做 token 空值判断、未捕获验签异常
   return true;
}
return true;                                  // 所有其他请求一律放行
```

1. **条件永不成立**：真实请求 URI 是 `/stucourses/ids/S0001`，而代码比对的字符串包含字面的 `${id}`，两者不可能相等（作者疑似误把前端模板语法写进了后端）。所有请求走到最后的 `return true`。
2. **用错 API**：`Jwt.parseToken()` 实现是 `JWT.decode(token)`（`Tool/Jwt.java:63-65`），**只解码不验签**；真正会验证 HMAC 签名的 `verifyToken()`（`Tool/Jwt.java:51-55`）从未被调用。攻击者可用 `alg=none` 或任意签名伪造 `userId` / `userType`。
3. **无异常处理**：`token` 为 `null` 时 `JWT.decode` 抛出 `JWTDecodeException`，未 `try/catch`，会导致 500 而非预期拦截。

**实际后果**：`/courses/all`、`/Upload/avtor`、`/stucourses/add` 均可匿名调用；`/stucourses/ids/{任意学号}` 可横向越权拉取他人课表、成绩（含 `score` 字段）。

**修复方向**：

```java
// 反白名单：除登录接口外一律校验
private static final List<String> WHITELIST = List.of("/login/", "/courses/all");

@Override
public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    if ("OPTIONS".equals(request.getMethod())) return true;
    String url = request.getRequestURI();
    if (WHITELIST.stream().anyMatch(url::startsWith)) return true;

    String token = request.getHeader("token");
    if (token == null || token.isBlank()) { deny(response); return false; }
    try {
        DecodedJWT jwtDecoded = jwt.verifyToken(token);      // 关键：改用 verifyToken
        request.setAttribute("userId", jwtDecoded.getClaim("userId").asString());
        request.setAttribute("userType", jwtDecoded.getClaim("userType").asString());
        return true;
    } catch (JWTVerificationException e) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":401,\"msg\":\"登录已过期\"}");
        return false;
    }
}
```

同时把控制器中的每个接口都用 `request.getAttribute("userId")` 做归属校验，杜绝水平越权。

---

### P0-2　支付流程可被绕过，零成本获取任意付费课程

**位置**：`WenMa/src/views/courese/courses_detail.vue:50-84`（`successBuy`）
　　　　`WenMaService/.../Controller/StuCoursesController.java:20-25`（`/stucourses/add`）
　　　　`WenMaService/.../Service/Impl/StuCoursesServiceImpl.java:19-23`
**严重程度**：🔴 致命（业务逻辑漏洞 + 越权）

前端"扫码支付"弹窗只是一个静态图片（`apy.jpg`），点击「已支付」按钮即为终点：

```javascript
const successBuy = async () => {
  const res = await BuyCourses({ stuId: stuid, coursesId: coursesId })  // 直接下单，无支付凭证
  if (res.code === 200) { /* 购买成功 */ }
}
```

后端 `addStuCourses` 只做一次 `insert`：不校验支付流水、不校验课程金额、不校验是否重复购买，**并且 `stuId` 取自请求体而非 token**——攻击者可为任意学号下单。

**实际后果**：任何人可批量给自己或他人"购买"全部课程并为零支付；也可借此向他人课表灌入垃圾数据。

**修复方向**：
1. `stuId` 一律从 token / `request.getAttribute("userId")` 获取，请求体中的 `stuId` 直接忽略；
2. 引入订单表，改为「创建订单 → 支付网关回调 → 回调验签后才写 `stucourses`」三段式；
3. 底部防雷索：为 `(stuId, coursesId)` 建立数据库唯一索引，`ErrorCode` 做幂等处理；
4. 服务端校验课程实际价格与支付金额一致，防金额篡改。

---

### P0-3　明文密码比对 + 明文存储

**位置**：`WenMaService/.../Service/Impl/StuServiceImp.java:30`
　　　　`WenMaService/.../Service/Impl/EmpServiceImp.java:26`
　　　　`WenMaService/WenMa/Entity/src/main/java/Entity/Stu.java:21-22`
**严重程度**：🔴 致命（凭证泄露）

```java
queryWrapper.eq(Stu::getStuId, username).eq(Stu::getPassword, password);
```

密码作为**查询条件**直接拼接比较，等价于数据库必须明文保存口令；`Stu.java:21` 的注释更写明了「学生密码，默认 123456」。

**叠加风险**：全仓检索确认**无登录失败计数、无验证码、无锁定机制**（`grep -rniE "ratelimit|attempt|captcha|lock"` 零命中），可对学号做无限次暴力枚举。

**修复方向**：
1. 存储改为 `BCryptPasswordEncoder`（Spring Security）或 Argon2id 加盐哈希；
2. 登录改为「先按账号查用户 → 再 `matches()` 比对」两步，避免明文进 SQL；
3. 增加失败次数限制（Redis 计数，5 次锁定 15 分钟）+ 图形验证码；
4. 强制初始口令修改，清除默认 `123456`。

---

### P0-4　登录密码被明文渲染进页面 DOM

**位置**：`WenMa/src/views/Login.vue:62`
**严重程度**：🔴 严重（敏感信息泄露）

```html
<form @submit.prevent="Login">
  {{ LoginInfo }}   <!-- ← 整个表单对象（含 password）被插值渲染 -->
```

用户每输入一个字符，密码就以明文实时出现在页面可见位置，并写入 DOM。可被肩窥、录屏、浏览器扩展、崩溃快照采集。

**修复方向**：直接删除该行（显然是调试残留）。同类的还有 `views/employee/space.vue:8` 的 `{{ isDark }}` 调试输出，一并清理。

---

### P0-5　含密钥的配置文件已提交 Git，且仓库无 .gitignore

**位置**：`WenMaService/WenMa/Service/src/main/resources/application.yml`（已被跟踪）
　　　　`WenMaService/WenMa/Service/target/classes/application.yml`（编译副本，同样入库）
**严重程度**：🔴 致命（密钥泄露）

核验结果：

```
$ git ls-files | grep application
WenMaService/WenMa/Service/src/main/resources/application.yml
WenMaService/WenMa/Service/target/classes/application.yml

$ git remote -v
origin  https://gitee.com/lv-junyu-sumingyue/question-code-education.git

$ ls .gitignore
NO ROOT .gitignore          # 整个仓库没有任何忽略规则
```

仓库共有 153 个受跟踪文件，其中 **49 个是 `target/` 编译产物**。密钥通过 `@Value("${jwt.secret}")`（`Tool/Jwt.java:16`）从该 yml 注入，意味着 **JWT 签名密钥已随代码进入远端仓库历史**。一旦仓库可见性设置为公开或凭据泄露，攻击者可自签任意 `userId` 的 token——与 P0-1 的"不验签"叠加，等于防线全失。

> 注：OSS 的 AccessKey 走的是环境变量（ `AliyunOss.java:40` `CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider()`），这一处做法是规范的，予以保留。

**修复方向**：
1. **立即轮换** `jwt.secret`、数据库口令等一切出现在历史提交中的凭据——已推送即视为已泄露，不要抱侥幸；
2. 新建根目录 `.gitignore`（覆盖 `target/`、`node_modules/`、`.idea/`、`application.yml`、`*.jar`、`dist/`）；
3. `git rm --cached` 移除已跟踪的 `application.yml` 与 `target/`，提供 `application-example.yml` 模板；
4. 清理历史：使用 `git filter-repo` 或 BFG 抹除密钥，并强制推送（团队同步重置）；
5. 部署侧改由环境变量 / 配置中心注入密钥。

---

## 三、P1 高危问题

### P1-1　文件上传无服务端校验，存在 OOM 与存储型 XSS 风险

**位置**：`Controller/UploadController.java:22-27`、`Tool/AliyunOss.java:38-65`
　　　　`WenMa/src/views/student/space.vue:30-42`（前端限制写反）
**严重程度**：🟠 高（DoS + XSS）

后端 `file.getBytes()` 把整个文件一次性读入堆内存，且**未做任何大小、类型、内容校验**，直接上传 OSS 并返回公网直链。攻击者只要改扩展名即可上传 `.html` / `.svg`，借助 OSS 域名同源特性触发存储型 XSS；大文件并发上传则可耗尽堆内存。

前端拦截也是失效的——判断顺序写反了：

```javascript
const beforeAvatarUpload = (rawFile) => {
  if (rawFile.type === 'image/jpeg' || ...) {
    return true                                  // ← 类型合法即直接放行，下面的 2MB 分支永远进不去
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error('Avatar picture size can not exceed 2MB!')
    return false
  }
  return false
}
```

**修复方向**：
1. 配置 `spring.servlet.multipart.max-file-size` 与 `max-request-size`，并在代码内二次校验；
2. 校验文件头 magic bytes（而非信任 `Content-Type`），白名单限定 jpg/png/webp；
3. 改用流式上传（`PutObjectRequest` 接受 `InputStream`，不必全量入堆）；
4. OSS Bucket 设为私有读，通过签名 URL 或 CDN 回源鉴权输出，并设置 `Content-Disposition`；
5. 前端改为「先判大小 → 再判类型」。

---

### P1-2　课程列表全表扫描，无分页无缓存

**位置**：`Service/Impl/CoursesServiceImp.java:43`（`coursesMapper.selectList(null)`）
　　　　`Controller/CoursesController.java:21`（`GET /courses/all`）
**严重程度**：🟠 高（性能）

无任何 `where` 与 `limit`，全量拉取课程表并整体序列化返回。该接口被课程中心首页调用，数据量增长后会造成长事务、大响应体（每次都拉全量）与连接池长时间占用。

**修复方向**：改为 MyBatis-Plus 分页（`Page<Courses>` + `selectPage`），并叠加 Caffeine/Redis 缓存与 `Cache-Aside` 失效策略。

---

### P1-3　N+1 查询

**位置**：`Controller/CoursesController.java:51-60`（`GET /courses/list`）
**严重程度**：🟠 高（性能）

```java
for (String coursesId : coursesIds) {
    Courses courses = coursesService.getCoursesDetailById(coursesId);   // 每个 ID 一次 DB 往返
    list.add(courses);
}
```

学生空间每次进入都有「查课程 ID 列表 → 逐个查详情」的串行 N 次往返，同时 P0-2 提到的 `coursesIds` 可被无限放大，形成放大攻击面。

**修复方向**：改为一次批量查询，并对入参数量做上限（如 ≤ 100）：

```java
LambdaQueryWrapper<Courses> q = new LambdaQueryWrapper<>();
q.in(Courses::getCoursesId, coursesIds);
return coursesMapper.selectList(q);
```

---

### P1-4　@vueuse/core 幽灵依赖，他人构建必然失败

**位置**：`views/employee/space.vue:2`、`views/Home/Layout.vue:8` 引用 `@vueuse/core`
　　　　`WenMa/package.json` 中**不存在**该依赖（核验 `grep -c vueuse` = 0）
**严重程度**：🟠 高（构建可靠性）

当前 `node_modules/@vueuse` 存在，是因为被 Element Plus 的传递依赖 hoist 上来的——典型的幽灵依赖。本机可以跑，一旦执行 `npm ci`、换分支、或依赖树调整，就会 `Failed to resolve import "@vueuse/core"` 而构建失败。

**修复方向**：`npm i @vueuse/core` 显式写入 `package.json`（生产依赖）。

---

## 四、P2 中等问题

| # | 问题 | 位置 | 风险 | 修复方向 |
|---|------|------|------|----------|
| P2-1 | **CORS 全开**：`allowedOriginPatterns("*")` + `allowedHeaders("*")` | `config/CrossConfig.java:10-16` | 任意站点可读取接口数据，与 P0-1 叠加后果翻倍 | 收敛到真实前端域名白名单，Headers 按需放开 |
| P2-2 | **无全局异常处理器**：`@RestControllerAdvice` 被注释掉 | `Exception/SqlExpection.java:11` | 任意未捕获异常（如 `TooManyResultsException`）直接把内部信息/堆栈回吐客户端 | 取消注解注释并完善各异常分支，生产环境屏蔽异常详情 |
| P2-3 | **JWT 过期时间 int 溢出**：`instance.add(Calendar.MILLISECOND, (int) expireTime)` | `Tool/Jwt.java:31` | `expireTime` 超过 `Integer.MAX_VALUE`（约 24.8 天的毫秒值）时强转溢出，token 有效期可能异常（立即过期或永不失效） | 改用 `Date.from(Instant.now().plusMillis(expireTime))`，单位统一为秒并加范围校验 |
| P2-4 | **前端 Blob URL 泄漏 + 空值崩溃**：`URL.createObjectURL` 未在卸载时 `revokeObjectURL`；`JSON.parse(userInfo)` 为 null 时模板 `studentInfo.name` 报错 | `views/student/space.vue:20, 82` | 反复更换头像累积内存泄漏；未登录访问该路由直接白屏 | `onUnmounted` 中 revoke；`ref(userInfo ? JSON.parse(userInfo) : {})` 并加 `v-if` 保护 |
| P2-5 | **调试输出与阻塞式打印**：前端 14 处 `console.*`；后端 5 处 `System.out.println`，其中 `JwtTokenInterceptor:22` 每请求必打 URL | 多文件 | 每次请求触发同步 IO，高并发下拖慢吞吐；日志膨胀；泄露用户名等敏感数据 | 统一用 `log.info`，拦截器中的打印删除，`console.*` 用构建插件 strip |
| P2-6 | **死代码与重复路由**：`EmpLogService` / `DeptService` / `EmpExprMapper` / `DeptMapper` 等 6 个类无任何调用方；`Service` 包下误放 `EmpExprMapper.java`；路由 `name: 'coursesDetail'` 重复定义两次 | `router/index.js:22-23` 及各 Service | 误导维护、包体冗余；Vue Router 重复 name 会告警 | 确认无用后删除；清理重复路由定义 |

---

## 五、值得肯定的部分

1. **数据访问层干净**：全部使用 `LambdaQueryWrapper` 预编译参数绑定，MyBatis-Plus 自动转义，**未发现任何 SQL 注入点**。
2. **前端无 XSS 汇聚点**：全仓无 `v-html`、`innerHTML`、`eval()`、`dangerouslyInsert`，Vue 默认插值转义保证了基本安全。
3. **OSS 凭证走环境变量**：`CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider()` 是规范做法。
4. **上传文件名已随机化**：`UUID.randomUUID() + 后缀` 避免了对象覆盖与路径猜测。

---

## 六、建议修复顺序

```
第 1 批（当天完成，止血）
  P0-5  轮换密钥 + 补 .gitignore + 清理历史
  P0-1  重写 JWT 拦截器（这是整个安全基座的开关）
  P0-4  删除 Login.vue:62 密码渲染（1 行改动，立刻做）

第 2 批（本周完成，堵住资损）
  P0-2  支付改为订单+回调模型，stuId 取自 token
  P0-3  密码 BCrypt 化 + 登录限流
  P1-4  补 @vueuse/core 依赖声明

第 3 批（迭代内完成，性能与健壮性）
  P1-1  上传校验（服务端为主、前端为辅）
  P1-3  N+1 改批量查询
  P1-2  课程分页 + 缓存
  P2 系列
```

**上线前置条件**：P0 五项全部修复并回归验证（重点验证——不带 token 调用 `/stucourses/ids/S0001` 应返回 401；伪造 token 应被判无效；未支付状态下无法写入 `stucourses` 表）。
