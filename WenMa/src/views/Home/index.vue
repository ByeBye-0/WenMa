<script setup>
// 官网首页：Hero 主视觉 + 热门课程区块
// 课程数据经 api/course.js 统一出口读取(mock，后端就绪后无痛切换)
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { UserFilled } from '@element-plus/icons-vue'
import { getCoursesApi } from '@/api/courses'
import { getUserInfo } from '@/Utils/userInfo'
const router = useRouter()
const userInfo = getUserInfo()
// 首页热门课程(按报名人数取前 8)
const hotCourses = ref()
const getHotCourses = async () => {
  try {
    const res = await getCoursesApi()
    hotCourses.value = res.data.slice(0, 9)
    console.log('热门课程数据:', res.data)
  } catch (error) {
    console.error('获取热门课程数据失败:', error)
  }
}
onMounted(() => {
  getHotCourses()
  loading.value = false
})

const loading = ref(true)
// 免费试学
const getCourses = () => {
  if (userInfo === null) {
    router.push('/login')
  } else {
    router.push('/courses')
  }
}
const drawer = ref(false)
</script>

<template>
  <div class="home-page">
    <!-- Hero 主视觉 -->
    <section class="hero">
      <div class="hero-inner">
        <p class="hero-tagline">职业教育 · 值得信赖</p>
        <h1 class="hero-title">学编程，来 <span class="highlight">问码</span></h1>
        <p class="hero-desc">以问为始，以码为舟</p>
        <div class="hero-actions">
          <el-button type="primary" size="large" round @click="getCourses"> 免费试学 </el-button>
          <el-button size="large" round @click="router.push('/courses')"> 浏览课程 </el-button>
        </div>
      </div>
    </section>

    <!-- 热门课程区 -->
    <section class="section">
      <div class="section-head">
        <h2>热门课程</h2>
        <span class="section-sub">往期报名最高的口碑好课，系统教学带你高效进阶</span>
      </div>
      <div v-loading="loading" element-loading-background="rgba(255,255,255,.6)" class="hot-grid">
        <!-- <CourseCard v-for="course in hotCourses" :key="course.id" :course="course" /> -->

        <el-card
          v-for="course in hotCourses"
          :key="course.coursesId"
          style="width: 350px; cursor: pointer; border-radius: 12px"
          shadow="hover"
          @click="router.push(`/courses/${course.coursesId}`)"
        >
          <template #header>
            <span style="font-weight: 800; font-size: large">{{ course.coursesName }}</span>
          </template>
          <p>{{ course.description }}</p>
          <p>价格: ¥{{ course.price }}</p>
          <p>总学时: {{ course.totalHours }} 小时</p>
          <template #footer>
            <button @click.stop="drawer = true">
              <el-icon style="justify-content: center"><UserFilled /></el-icon>
              <span style="margin-left: 8px; line-height: 1.5">AI客服</span>
            </button>
          </template>
        </el-card>
      </div>

      <div class="more">
        <el-button round @click="router.push('/courses')">查看全部课程 →</el-button>
      </div>
    </section>
  </div>

  <el-drawer v-model="drawer" title="AI客服" :with-header="false">
    <span>Hi there!</span>
    <br />
    <span>尚未开发</span>
  </el-drawer>
</template>

<style scoped>
.card :deep(.el-card) {
  background: var(--card-bg);
  border: 1px solid var(--card-border);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3); /* 加深阴影 */
}
.card :deep(.el-card):hover {
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.5);
}
.home-page {
  padding-bottom: 40px;
}

/* ---------- Hero ---------- */
.hero {
  background:
    radial-gradient(ellipse at 20% 0%, rgba(255, 122, 24, 0.1), transparent 50%),
    linear-gradient(180deg, var(--bg-color) 0%, var(--card-bg) 100%);
  padding: 88px 24px 72px;
  text-align: center;
}
.hero-tagline {
  display: inline-block;
  padding: 4px 14px;
  border: 1px solid #ffd9c4;
  border-radius: 999px;
  color: #f2590b;
  background: #fff4ec;
  font-size: 13px;
  letter-spacing: 1px;
}
.hero-title {
  margin: 20px 0 12px;
  font-size: 46px;
  font-weight: 800;
  color: var(--text-color); /* 替换 #0f172a */
}
.highlight {
  color: #f2590b;
}
.hero-desc {
  color: var(--text-sub-color); /* 替换 #475569 */
  font-size: 17px;
}
.hero-actions {
  margin-top: 32px;
  display: flex;
  justify-content: center;
  gap: 16px;
}

/* ---------- 通用区块 ---------- */
.section {
  border-radius: 12px;
  background: var(--section-bg); /* 替换 #f1eeee */
  max-width: 1500px;
  margin: 0 auto;
  padding: 48px 24px 0;
}
.section-head {
  text-align: center;
  margin-bottom: 24px;
}
.section-head h2 {
  font-size: 28px;
  color: var(--text-color);
}
.section-sub {
  display: block;
  margin-top: 8px;
  color: var(--text-sub-color);
  font-size: 14px;
}
.hot-grid {
  min-height: 180px;
  display: grid;
  grid-template-columns: repeat(auto-fill, 350px);
  gap: 40px;
  margin: 0 auto;
  justify-content: center;
}
.more {
  padding: 25px;
  text-align: center;
}
.more :deep(.el-button) {
  width: 300px;
  height: 80px;
  padding: 10px 28px;
}
</style>
