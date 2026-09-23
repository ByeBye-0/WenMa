<script setup>
// 官网整体布局：顶栏导航 + 内容区 + 页脚
// AI 客服浮窗组件后续步骤会挂载到本布局(对所有浏览官网的访客生效)
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ref, onMounted } from 'vue'
import { Moon, Sunny } from '@element-plus/icons-vue'
import { useDark } from '@vueuse/core'

const isDark = useDark()

const router = useRouter()
const route = useRoute()

onMounted(() => {
  console.log(`Layout the component is now mounted.`)
  console.log('user:', user.value)
  if (user.value) logged.value = true
})
// 用 ref 缓存登录态(本布局内登录/退出时同步更新)
const logged = ref(false)
const user = ref(JSON.parse(localStorage.getItem('userInfo')))

// 导航项：尚未实现的功能标记 disabled，避免点击后出现 404
const navs = [
  { text: '首页', path: '/', disabled: false },
  { text: '课程中心', path: '/courses', disabled: false },
  { text: '关于我们', path: '/about', disabled: false },
]

const goSpace = () => {
  if (user.value.userType === '1') {
    router.push('/student/space')
  } else if (user.value.userType === '2') {
    router.push('/employee/space')
  } else {
    ElMessage.error('未知用户类型，无法进入空间')
  }
}

const handleLogout = () => {
  logged.value = false
  user.value = null
  ElMessage.success('已退出登录')
  localStorage.removeItem('userInfo')
  if (route.path !== '/') router.push('/')
}
</script>

<template>
  <div class="portal-layout">
    <!-- ========== 顶栏 ========== -->

    <header class="site-header">
      <router-link to="/" class="brand">
        <span class="brand-logo">问</span>
        <span class="brand-name">码</span>
      </router-link>
      <nav class="site-nav">
        <el-switch
          class="nav-item"
          v-model="isDark"
          inline-prompt
          active-text="是"
          inactive-text="否"
          :active-icon="Moon"
          :inactive-icon="Sunny"
          size="large"
          style="
            line-height: 66px;
            --el-switch-on-color: #475569;
            --el-switch-off-color: rgb(42, 89, 138);
          "
        />
        <router-link
          v-for="item in navs"
          :key="item.path"
          :to="item.disabled ? undefined : item.path"
          class="nav-item"
          :class="{
            disabled: item.disabled,
            active: route.path === item.path,
          }"
        >
          {{ item.text }}
        </router-link>
      </nav>

      <div class="header-actions">
        <template v-if="logged && user">
          <span class="user-name">{{ user.name }}</span>
          <el-button text @click="goSpace">进入我的空间</el-button>
          <el-button text @click="handleLogout">退出</el-button>
        </template>
        <template v-else>
          <el-button style="font-size: 20px" text @click="router.push('/login')">登录</el-button>
          <el-button style="font-size: 20px" round @click="router.push('/login?mode=register')"
            >注册</el-button
          >
        </template>
      </div>
    </header>

    <!-- ========== 内容区 ========== -->
    <main class="site-main">
      <router-view />
    </main>

    <!-- ========== 页脚 ========== -->
    <footer class="site-footer">
      <div class="footer-inner">
        <router-link to="/" class="footer-brand">
          <span class="footer-logo">问</span>码
        </router-link>
        <p class="footer-slogan">以问为始，以码为舟 · 让每一次提问都成为进步的起点</p>
        <p class="copyright">xxx</p>
      </div>
    </footer>
    <el-backtop :right="80" :bottom="100" />
    <!-- TODO(第 3 步)：在此挂载 AI 客服浮窗组件，访客进入官网自动弹出 -->
  </div>
</template>

<style scoped>
/* 删除多余的重复定义，只保留这一份 */
.portal-layout {
  min-height: 100vh;
  width: 100%;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  background: var(--bg-color);
  color: var(--text-color);
  /* 增加平滑过渡 */
  transition:
    background-color 0.4s ease,
    color 0.4s ease;
}

.el-backtop {
  width: 80px;
  height: 80px;
  /* 使用主题变量，代替刺眼的蓝色 */
  background-color: var(--brand-orange);
  color: #fff;
  transition: background-color 0.4s ease;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

/* ---------- 顶栏 ---------- */
.site-header {
  position: sticky;
  top: 0;
  z-index: 100;
  display: flex;
  align-items: center;
  gap: 40px;
  height: 100px;
  padding: 0 48px;
  background: var(--header-bg);
  backdrop-filter: blur(8px);
  border-bottom: 1px solid var(--card-border);
  transition:
    background-color 0.4s ease,
    border-color 0.4s ease;
}

.brand {
  display: flex;
  align-items: center;
  gap: 10px;
  border-radius: 8px;
  width: 80px;
  text-align: center;
  transition: all 0.2s;
}
.brand:hover {
  background-color: rgba(116, 109, 109, 0.2);
}

.brand-logo {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 34px;
  height: 34px;
  border-radius: 8px;
  /* 改为变量，让其在暗黑下自动变化（或者保持黑白反转） */
  background: var(--text-color);
  color: var(--bg-color);
  font-weight: 700;
  font-size: 25px;
  transition:
    background-color 0.4s ease,
    color 0.4s ease;
}

.brand-name {
  font-size: 40px;
  font-weight: 700;
  letter-spacing: 1px;
  color: var(--text-color);
  text-decoration: none;
  transition: color 0.4s ease;
}

.site-nav {
  align-items: center;
  flex: 1;
  display: flex;
  gap: 4px;
}

.nav-item {
  line-height: 66px;
  padding: 8px 16px;
  border-radius: 8px;
  color: var(--text-sub-color);
  font-size: 20px;
  transition: all 0.3s ease;
}
.nav-item:hover:not(.disabled) {
  color: var(--text-color);
  background: var(--card-bg);
}
.nav-item.active {
  color: #fff;
  background: var(--brand-orange);
  font-weight: 600;
}
.nav-item.disabled {
  /* 修复硬编码颜色，使用带透明度的变量 */
  color: var(--text-sub-color);
  opacity: 0.5;
  cursor: not-allowed;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 6px;
}
.user-name {
  font-size: 20px;
  color: var(--text-sub-color);
  transition: color 0.4s ease;
}

/* ---------- 内容区 ---------- */
.site-main {
  flex: 1;
  background: var(--bg-color);
  transition: background-color 0.4s ease;
}

/* ---------- 页脚 ---------- */
.site-footer {
  background: #0b1120; /* 页脚本身就很深，两种模式下都合适，或者也可以换成 var(--card-bg) */
  color: #94a3b8;
  border-top: 1px solid var(--card-border);
  transition: border-color 0.4s ease;
}

.footer-inner {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 24px;
  text-align: center;
}
.footer-brand {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  font-size: 18px;
  font-weight: 700;
  color: #fff;
}
.footer-logo {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 28px;
  height: 28px;
  border-radius: 6px;
  background: #fff;
  color: #111113;
  font-size: 15px;
  font-weight: 700;
}
.footer-slogan {
  margin: 14px 0 6px;
  font-size: 14px;
  color: #a1a1aa;
}
.copyright {
  font-size: 12px;
  color: #71717a;
}
</style>
