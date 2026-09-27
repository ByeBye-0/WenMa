<script setup>
import { ref } from 'vue'
import { LoginApi } from '@/api/Login.js'
import { ElMessage, ElNotification } from 'element-plus'
import { useRouter } from 'vue-router'
import { setUserInfo } from '@/Utils/userInfo'

const router = useRouter()
const LoginInfo = ref({ userType: '', username: '', password: '' })
const Login = async () => {
  if (
    LoginInfo.value.userType == '' ||
    LoginInfo.value.username == '' ||
    LoginInfo.value.password == ''
  ) {
    ElMessage.error('请选择身份类型')
    return
  } else {
    const res = await LoginApi({
      userType: LoginInfo.value.userType,
      username: LoginInfo.value.username,
      password: LoginInfo.value.password,
    })
    if (res.code === 200) {
      router.push('/index')
      ElNotification({
        title: '登录',
        message: '登录成功，欢迎回来',
        type: 'success',
        duration: 3000,
      })
      setUserInfo(res.data)
      console.log('登录成功，用户信息已存储到 localStorage:', res)
    } else {
      console.error('登录失败:', res)
      ElMessage.error(res.message || '登录失败，请检查用户名和密码')
    }
  }
}
const clear = () => {
  LoginInfo.value = { userType: '', username: '', password: '' }
}
</script>

<template>
  <body>
    <!-- 玻璃碎裂背景层 -->
    <div class="glass-shatter-bg">
      <div class="shards"></div>
      <div class="glass-pieces"></div>
    </div>

    <!-- 登录卡片 -->
    <main class="login-card">
      <div class="brand">
        <h1>login</h1>
        <div class="subtitle">mono · glass</div>
      </div>

      <!-- 登录表单区域 -->
      <!-- 监听submit事件，prevent是事件修饰符，阻止默认提交行为，执行handleLogin方法 -->
      <form @submit.prevent="Login">
        {{ LoginInfo }}
        <div class="user-type">
          <el-button
            round
            :class="{ 'is-active': LoginInfo.userType === '1' }"
            @click="LoginInfo.userType = '1'"
            >学生</el-button
          >
          <el-button
            round
            :class="{ 'is-active': LoginInfo.userType === '2' }"
            @click="LoginInfo.userType = '2'"
            >员工</el-button
          >
        </div>

        <div class="input-group">
          <label for="username">账号\用户名</label>
          <input
            v-model="LoginInfo.username"
            type="username"
            id="username"
            name="username"
            placeholder="账号"
            class="input-field"
            autocomplete="username"
            required
          />
        </div>

        <div class="input-group">
          <label for="password">密码</label>
          <input
            v-model="LoginInfo.password"
            type="password"
            id="password"
            name="password"
            placeholder="••••••••"
            class="input-field"
            autocomplete="current-password"
            required
          />
        </div>

        <div class="row-space">
          <label class="checkbox-label"> <input type="checkbox" name="remember" /> 记住我 </label>
          <a href="#" class="forgot-link">忘记密码？</a>
        </div>

        <button type="submit" class="login-btn">登 录</button>
        <button class="login-btn" @click="clear">重 置</button>
      </form>

      <!-- 社交登录或其他分割（保持黑白灰，简洁） -->
      <div class="divider">或</div>

      <!-- 简约的注册引导 -->
      <div class="signup-prompt">还没有账户？ <a href="#">立即注册</a></div>
    </main>
  </body>
</template>

<style scoped>
/* 身份选择 */
.user-type {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 1.6rem;
}

.user-type .el-button {
  width: 50%;
  height: 50px;
  /* 重置Element Plus默认样式，融入黑白灰极简风格 */
  background: rgba(10, 10, 10, 0.5);
  border: 1px solid rgba(200, 200, 200, 0.2);
  color: #b0b0b0;
  box-shadow:
    0 2px 8px -3px rgba(0, 0, 0, 0.5),
    inset 0 1px 0 rgba(255, 255, 255, 0.05);
  font-weight: 500;
  letter-spacing: 1.5px;
  transition: all 0.25s ease;
}

.user-type .el-button:hover {
  /* 悬浮时边框与文字提亮，背景微调 */
  color: #e0e0e0;
  border-color: rgba(200, 200, 200, 0.4);
  background: rgba(30, 30, 30, 0.6);
  box-shadow:
    0 5px 15px -5px rgba(0, 0, 0, 0.6),
    inset 0 1px 0 rgba(255, 255, 255, 0.08);
}

.user-type :deep(.el-button):active,
.user-type :deep(.el-button).is-active {
  /* 选中/激活状态：使用与登录按钮一致的高亮灰白对比 */
  color: #111;
  background: #eaeaea;
  border-color: rgba(255, 255, 255, 0.6);
  box-shadow: 0 10px 25px -8px rgba(0, 0, 0, 0.6);
}

/* 全局重置与黑白灰基调 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  min-height: 100vh;
  font-family: 'Inter', 'Helvetica Neue', 'PingFang SC', 'Microsoft YaHei', sans-serif;
  background-color: #0b0b0b; /* 深黑背景，衬托玻璃碎屑 */
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow-x: hidden;
  color: #eaeaea;
}

/* 玻璃碎裂背景层 ———— 通过多个不规则多边形+渐变模拟碎玻璃反射，黑白灰层次 */
.glass-shatter-bg {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
  pointer-events: none;
  background:
        /* 底层深色渐变，模拟暗色玻璃基底 */ radial-gradient(
    circle at 20% 30%,
    #2a2a2a 0%,
    #111 40%,
    #050505 90%
  );
}

/* 碎裂多边形容器 ———— 使用多个svg与伪元素生成玻璃碎片 */
.shards {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  background:
        /* 线性裂纹 ———— 用多重线性渐变和角度制造龟裂感 */
    repeating-linear-gradient(
      47deg,
      rgba(220, 220, 220, 0.08) 0px,
      rgba(220, 220, 220, 0.02) 2px,
      transparent 3px,
      transparent 18px
    ),
    repeating-linear-gradient(
      137deg,
      rgba(160, 160, 160, 0.06) 0px,
      rgba(160, 160, 160, 0.01) 2px,
      transparent 4px,
      transparent 22px
    ),
    repeating-linear-gradient(
      25deg,
      rgba(240, 240, 240, 0.04) 0px,
      transparent 5px,
      transparent 25px
    ),
    radial-gradient(ellipse at 70% 40%, rgba(200, 200, 200, 0.15) 0%, transparent 50%),
    radial-gradient(ellipse at 30% 70%, rgba(130, 130, 130, 0.12) 0%, transparent 55%),
    linear-gradient(125deg, rgba(40, 40, 40, 0.8) 0%, #1a1a1a 100%);
  mix-blend-mode: screen;
}

/* 动态玻璃碎片 ———— 使用多个倾斜的平行四边形模拟破碎玻璃片 */
.glass-pieces {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  background:
        /* 碎片1 —— 亮灰斜片 */
    linear-gradient(
      135deg,
      rgba(255, 255, 255, 0.08) 0%,
      rgba(255, 255, 255, 0.01) 30%,
      transparent 40%
    ),
    linear-gradient(
      45deg,
      transparent 60%,
      rgba(255, 255, 255, 0.05) 80%,
      rgba(255, 255, 255, 0.12) 100%
    ),
    /* 碎片2 —— 暗部棱镜 */
    linear-gradient(
        20deg,
        transparent 10%,
        rgba(40, 40, 40, 0.5) 35%,
        rgba(120, 120, 120, 0.2) 50%,
        transparent 75%
      ),
    /* 碎片3 —— 灰白折射 */
    linear-gradient(
        160deg,
        rgba(190, 190, 190, 0.15) 0%,
        transparent 30%,
        rgba(240, 240, 240, 0.1) 60%,
        transparent 80%
      ),
    /* 碎片4 —— 裂纹高光 */
    linear-gradient(
        75deg,
        transparent 0%,
        rgba(255, 255, 255, 0.04) 20%,
        rgba(255, 255, 255, 0.2) 38%,
        transparent 55%
      );
  background-blend-mode: overlay;
  transform: rotate(0deg);
}

/* 使用伪元素增加玻璃尖锐棱角感 */
.glass-shatter-bg::before {
  content: '';
  position: absolute;
  inset: 0;
  background:
    conic-gradient(
      from 210deg at 70% 30%,
      rgba(255, 255, 255, 0.03) 0deg,
      transparent 20deg,
      rgba(200, 200, 200, 0.06) 60deg,
      transparent 120deg,
      rgba(160, 160, 160, 0.02) 200deg,
      rgba(255, 255, 255, 0.08) 280deg,
      transparent 330deg
    ),
    conic-gradient(
      from 10deg at 20% 80%,
      rgba(90, 90, 90, 0.1) 0deg,
      transparent 50deg,
      rgba(220, 220, 220, 0.05) 100deg,
      transparent 200deg,
      rgba(180, 180, 180, 0.08) 270deg,
      transparent 320deg
    );
  background-blend-mode: screen;
  filter: blur(0.6px);
}

.glass-shatter-bg::after {
  content: '';
  position: absolute;
  inset: 0;
  background:
    linear-gradient(
      18deg,
      transparent 0%,
      rgba(20, 20, 20, 0.9) 25%,
      transparent 45%,
      rgba(30, 30, 30, 0.7) 70%,
      transparent 90%
    ),
    repeating-linear-gradient(
      80deg,
      rgba(255, 255, 255, 0.02) 0px,
      rgba(255, 255, 255, 0.06) 1px,
      transparent 2px,
      transparent 8px
    );
  mix-blend-mode: overlay;
}

/* 主卡片 ———— 简洁玻璃卡片，灰阶调 */
.login-card {
  position: relative;
  z-index: 10;
  width: 100%;
  max-width: 500px;
  background: rgba(22, 22, 22, 0.68);
  backdrop-filter: blur(18px) saturate(120%);
  -webkit-backdrop-filter: blur(18px) saturate(120%);
  border: 1px solid rgba(220, 220, 220, 0.15);
  border-radius: 24px;
  box-shadow:
    0 25px 50px -8px rgba(0, 0, 0, 0.7),
    0 0 0 0.5px rgba(255, 255, 255, 0.05) inset;
  padding: 2.8rem 2.2rem;
  transition: all 0.2s ease;
  color: #e6e6e6;
}

/* 标题区域 */
.brand {
  text-align: center;
  margin-bottom: 2.5rem;
}

.brand h1 {
  font-size: 2rem;
  font-weight: 350;
  letter-spacing: 2px;
  color: #f0f0f0;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.5);
  margin-bottom: 0.3rem;
  text-transform: uppercase;
}

.brand .subtitle {
  font-size: 0.8rem;
  font-weight: 300;
  letter-spacing: 3px;
  color: #9a9a9a;
  text-transform: uppercase;
}

/* 表单样式 */
.input-group {
  margin-bottom: 1.6rem;
}

.input-group label {
  display: block;
  font-size: 0.75rem;
  font-weight: 500;
  letter-spacing: 1.5px;
  text-transform: uppercase;
  color: #b0b0b0;
  margin-bottom: 0.5rem;
}

.input-field {
  width: 100%;
  background: rgba(10, 10, 10, 0.5);
  border: 1px solid rgba(200, 200, 200, 0.2);
  border-radius: 12px;
  padding: 0.9rem 1.2rem;
  font-size: 1rem;
  color: #f0f0f0;
  outline: none;
  transition:
    border 0.2s,
    box-shadow 0.2s,
    background 0.2s;
  letter-spacing: 0.5px;
  font-weight: 300;
}

.input-field::placeholder {
  color: #5f5f5f;
  font-weight: 300;
  letter-spacing: 0.5px;
}

.input-field:focus {
  border-color: #b0b0b0;
  background: rgba(0, 0, 0, 0.7);
  box-shadow:
    0 0 0 3px rgba(220, 220, 220, 0.1),
    0 8px 20px -5px rgba(0, 0, 0, 0.6);
}

/* 行内辅助功能 */
.row-space {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 1.2rem 0 2rem;
  font-size: 0.75rem;
}

.checkbox-label {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #b0b0b0;
  cursor: pointer;
  letter-spacing: 0.5px;
}

.checkbox-label input[type='checkbox'] {
  appearance: none;
  -webkit-appearance: none;
  width: 16px;
  height: 16px;
  background: transparent;
  border: 1.5px solid #808080;
  border-radius: 4px;
  position: relative;
  cursor: pointer;
  transition: all 0.15s;
}

.checkbox-label input[type='checkbox']:checked {
  background-color: #d0d0d0;
  border-color: #d0d0d0;
}

.checkbox-label input[type='checkbox']:checked::after {
  content: '';
  position: absolute;
  left: 4px;
  top: 1px;
  width: 5px;
  height: 9px;
  border: solid #1a1a1a;
  border-width: 0 2px 2px 0;
  transform: rotate(45deg);
}

.forgot-link {
  color: #a0a0a0;
  text-decoration: none;
  letter-spacing: 0.5px;
  border-bottom: 1px solid transparent;
  transition:
    color 0.2s,
    border-color 0.2s;
}

.forgot-link:hover {
  color: #e0e0e0;
  border-bottom-color: #aaa;
}

/* 登录按钮 ———— 黑白灰极简 */
.login-btn {
  margin: 5px 0;
  width: 100%;
  padding: 0.9rem;
  border: none;
  border-radius: 12px;
  background: #eaeaea;
  color: #111;
  font-size: 1rem;
  font-weight: 600;
  letter-spacing: 2px;
  text-transform: uppercase;
  cursor: pointer;
  transition:
    background 0.25s,
    box-shadow 0.25s,
    transform 0.1s;
  box-shadow: 0 10px 25px -8px rgba(0, 0, 0, 0.6);
  border: 1px solid rgba(255, 255, 255, 0.6);
}

.login-btn:hover {
  background: #ffffff;
  box-shadow:
    0 15px 30px -6px rgba(0, 0, 0, 0.7),
    0 0 0 1px rgba(0, 0, 0, 0.1) inset;
}

.login-btn:active {
  transform: scale(0.98);
  background: #c0c0c0;
}

/* 注册提示 */
.signup-prompt {
  text-align: center;
  margin-top: 2rem;
  font-size: 0.8rem;
  color: #8a8a8a;
  letter-spacing: 0.5px;
}

.signup-prompt a {
  color: #d0d0d0;
  text-decoration: none;
  font-weight: 500;
  border-bottom: 1px solid #555;
  transition:
    color 0.2s,
    border-color 0.2s;
}

.signup-prompt a:hover {
  color: #fff;
  border-bottom-color: #ccc;
}

/* 装饰性分割线 */
.divider {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin: 1.8rem 0 1.2rem;
  color: #666;
  font-size: 0.7rem;
  letter-spacing: 2px;
}

.divider::before,
.divider::after {
  content: '';
  flex: 1;
  height: 1px;
  background: linear-gradient(90deg, transparent, #555, transparent);
}

/* 响应式微调 */
@media (max-width: 480px) {
  .login-card {
    padding: 2rem 1.5rem;
    border-radius: 18px;
  }
  .brand h1 {
    font-size: 1.7rem;
  }
}
</style>
