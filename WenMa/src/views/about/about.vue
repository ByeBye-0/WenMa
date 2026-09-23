<script setup>
import router from '@/router'
import { ref, onMounted, nextTick } from 'vue'

// --- 数据定义（保持不变） ---
const statsData = ref([
  { value: '15万+', label: '累计学员' },
  { value: '200+', label: '优质实战课程' },
  { value: '5000+', label: '合作企业' },
  { value: '95%', label: '学员就业率' },
])

const advantages = ref([
  {
    icon: 'fa-solid fa-code',
    title: '项目驱动教学',
    desc: '拒绝纯理论，3大企业级全栈项目贯穿始终，让你在实战中吃透Vue3全家桶与Node.js后端。',
  },
  {
    icon: 'fa-solid fa-user-tie',
    title: '名师1对1辅导',
    desc: '专属班主任与技术导师双轨制，随堂答疑+作业批改+阶段测评，拒绝流水线式放养。',
  },
  {
    icon: 'fa-solid fa-briefcase',
    title: '全链路就业服务',
    desc: '简历深度优化 + 模拟技术面试 + 大厂内推通道，不仅教你写代码，更教你如何拿高薪Offer。',
  },
])

const teachers = ref([
  {
    name: '张景明',
    role: 'Vue 技术专家',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=Felix',
    desc: '8年前端经验，主导多个千万级中后台系统。',
  },
  {
    name: '李老师',
    role: 'Java 架构师',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=Aneka',
    desc: '10年一线开发经验，精通微服务与分布式架构。',
  },
  {
    name: '王老师',
    role: '前端全栈工程师',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=Jack',
    desc: '前大厂P7，精通React/Vue及跨端开发。',
  },
  {
    name: '赵老师',
    role: '就业指导专家',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=Mia',
    desc: '资深HRBP，累计指导3000+学员入职大厂。',
  },
])

const milestones = ref([
  {
    year: '2021年',
    title: '平台正式成立',
    desc: '问码教育正式上线，推出首套前端体系课程，致力于打破IT培训行业信息差。',
  },
  {
    year: '2023年',
    title: '合作企业破千',
    desc: '与5000+互联网企业建立人才输送合作，学员就业率稳居行业前列。',
  },
  {
    year: '2024年',
    title: '全面升级Vue3全栈体系',
    desc: '紧跟技术潮流，重磅推出Vue3全栈项目实战就业班。',
  },
])

// --- 生命周期与动效 (保持不变) ---
onMounted(async () => {
  await nextTick()
  const observer = new IntersectionObserver(
    (entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          entry.target.classList.add('visible')
        }
      })
    },
    { threshold: 0.1 },
  )

  document.querySelectorAll('.fade-in-up').forEach((el) => {
    observer.observe(el)
  })
})
const getCourses = () => {
  router.push('/courses')
}
</script>
<template>
  <div class="about-page">
    <!-- 1. 首屏 Banner (Hero Section) -->
    <section class="hero-section">
      <div class="hero-content">
        <h1 class="hero-title">赋能IT人才，成就职业梦想</h1>
        <p class="hero-subtitle">
          问码教育，专注培养符合企业真实需求的实战型全栈开发工程师。<br />
          我们不仅传授技术，更致力于帮你构建核心竞争力。
        </p>
        <button class="btn-primary" @click="getCourses">了解我们的课程</button>
      </div>
      <!-- 背景装饰 -->
      <div class="hero-bg-decoration">
        <div class="circle circle-1"></div>
        <div class="circle circle-2"></div>
      </div>
    </section>

    <!-- 2. 核心数据展示 -->
    <section class="stats-section">
      <div class="container stats-grid">
        <div v-for="(stat, index) in statsData" :key="index" class="stat-item fade-in-up">
          <div class="stat-value">{{ stat.value }}</div>
          <div class="stat-label">{{ stat.label }}</div>
        </div>
      </div>
    </section>

    <!-- 3. 我们的核心优势 -->
    <section class="advantages-section">
      <div class="container">
        <div class="section-header">
          <h2 class="section-title">为什么选择问码？</h2>
          <p class="section-subtitle">区别于传统培训，我们提供全链路的学习与就业保障</p>
        </div>
        <div class="advantages-grid">
          <div v-for="(adv, index) in advantages" :key="index" class="advantage-card fade-in-up">
            <div class="icon-box">
              <i :class="adv.icon"></i>
            </div>
            <h3 class="card-title">{{ adv.title }}</h3>
            <p class="card-desc">{{ adv.desc }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- 4. 师资力量 -->
    <section class="teachers-section">
      <div class="container">
        <div class="section-header">
          <h2 class="section-title">师资力量</h2>
          <p class="section-subtitle">一线大厂背景，懂技术更懂教学</p>
        </div>
        <div class="teachers-grid">
          <div v-for="(teacher, index) in teachers" :key="index" class="teacher-card fade-in-up">
            <div class="avatar-wrapper">
              <img :src="teacher.avatar" :alt="teacher.name" class="avatar-img" />
            </div>
            <h3 class="teacher-name">{{ teacher.name }}</h3>
            <p class="teacher-role">{{ teacher.role }}</p>
            <p class="teacher-desc">{{ teacher.desc }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- 5. 发展历程 (时间轴) -->
    <section class="timeline-section">
      <div class="container">
        <div class="section-header">
          <h2 class="section-title">发展历程</h2>
          <p class="section-subtitle">一步一个脚印，与学员共同成长</p>
        </div>

        <div class="timeline-container">
          <div class="timeline-line"></div>
          <div
            v-for="(item, index) in milestones"
            :key="index"
            class="timeline-item fade-in-up"
            :class="index % 2 === 0 ? 'timeline-left' : 'timeline-right'"
          >
            <div class="timeline-dot"></div>
            <div class="timeline-content">
              <div class="timeline-year">{{ item.year }}</div>
              <h3 class="timeline-title">{{ item.title }}</h3>
              <p class="timeline-desc">{{ item.desc }}</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 6. 底部联系方式 -->
    <section class="contact-section">
      <div class="container contact-container">
        <h2 class="section-title">联系我们</h2>
        <div class="contact-flex">
          <div class="contact-item">
            <div class="qr-placeholder">公众号二维码</div>
            <p class="contact-label">扫码关注公众号</p>
          </div>
          <div class="contact-item">
            <div class="qr-placeholder">客服微信</div>
            <p class="contact-label">添加客服进入交流群</p>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
/* ================= 全局变量与基础重置 ================= */
.about-page {
  /* 亮色模式变量 */
  --brand-color: #8b5cf6; /* 你的 Logo 主题紫 */
  --brand-dark: #7c3aed;
  --brand-light: #f3e8ff;
  --text-main: #1f2937;
  --text-muted: #6b7280;
  --bg-gray: #f9fafb;
  --bg-white: #ffffff;
  --border-color: #e5e7eb;
  --shadow-sm: 0 4px 6px rgba(0, 0, 0, 0.02);
  --shadow-md: 0 10px 25px rgba(0, 0, 0, 0.06);

  background-color: var(--bg-gray);
  color: var(--text-main);
  font-family:
    -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  line-height: 1.5;
  transition:
    background-color 0.3s,
    color 0.3s;
}

/* 暗黑模式下的变量覆盖：只需修改变量，无需修改每个类的具体属性 */
html.dark .about-page {
  --brand-color: #a78bfa; /* 暗黑下紫色提亮 */
  --brand-dark: #8b5cf6;
  --brand-light: #2e1065; /* 暗黑下的浅紫背景（深紫） */
  --text-main: #f8fafc; /* 主文字白色 */
  --text-muted: #94a3b8; /* 副文字灰蓝 */
  --bg-gray: #0f172a; /* 页面大背景深蓝灰 */
  --bg-white: #1e293b; /* 原本的 white 改为深色卡片背景 */
  --border-color: #334155; /* 边框颜色 */
  --shadow-sm: 0 4px 6px rgba(0, 0, 0, 0.3);
  --shadow-md: 0 10px 25px rgba(0, 0, 0, 0.5);
}

/* 通用容器 */
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.section-header {
  text-align: center;
  margin-bottom: 60px;
}
.section-title {
  font-size: 30px;
  font-weight: 700;
  margin-bottom: 15px;
}
.section-subtitle {
  color: var(--text-muted);
  font-size: 16px;
}

/* 按钮样式 */
.btn-primary {
  background-color: var(--brand-color);
  color: white;
  border: none;
  padding: 12px 32px;
  border-radius: 9999px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition:
    background-color 0.3s,
    transform 0.2s;
  box-shadow: 0 4px 14px rgba(139, 92, 246, 0.3);
}
.btn-primary:hover {
  background-color: var(--brand-dark);
  transform: translateY(-2px);
}

/* ================= 1. Hero Section ================= */
.hero-section {
  position: relative;
  /* 替换 white */
  background-color: var(--bg-white);
  padding: 100px 20px;
  text-align: center;
  overflow: hidden;
  transition: background-color 0.3s;
}
.hero-content {
  position: relative;
  z-index: 10;
  max-width: 800px;
  margin: 0 auto;
}
.hero-title {
  font-size: 48px;
  font-weight: 800;
  /* 替换 #111827 */
  color: var(--text-main);
  margin-bottom: 24px;
  line-height: 1.2;
}
.hero-subtitle {
  font-size: 18px;
  color: var(--text-muted);
  margin-bottom: 40px;
  line-height: 1.8;
}
.hero-bg-decoration {
  position: absolute;
  top: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 100%;
  height: 100%;
  max-width: 1200px;
  pointer-events: none;
  opacity: 0.1;
}
.circle {
  position: absolute;
  border-radius: 50%;
  filter: blur(60px);
}
.circle-1 {
  top: 40px;
  left: 40px;
  width: 300px;
  height: 300px;
  background-color: var(--brand-color);
}
.circle-2 {
  bottom: 40px;
  right: 40px;
  width: 300px;
  height: 300px;
  background-color: #d8b4fe;
}

/* ================= 2. Stats Section ================= */
.stats-section {
  /* 替换 white */
  background-color: var(--bg-white);
  border-top: 1px solid var(--border-color);
  border-bottom: 1px solid var(--border-color);
  padding: 50px 0;
  transition:
    background-color 0.3s,
    border-color 0.3s;
}
.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  text-align: center;
}
.stat-value {
  font-size: 36px;
  font-weight: 700;
  color: var(--brand-color);
  margin-bottom: 8px;
}
.stat-label {
  font-size: 14px;
  color: var(--text-muted);
}
.stat-item:not(:last-child) {
  border-right: 1px solid var(--border-color);
}

/* ================= 3. Advantages Section ================= */
.advantages-section {
  padding: 80px 0;
}
.advantages-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 30px;
}
.advantage-card {
  /* 替换 white */
  background-color: var(--bg-white);
  padding: 30px;
  border-radius: 16px;
  /* 替换阴影 */
  box-shadow: var(--shadow-sm);
  transition:
    box-shadow 0.3s,
    transform 0.3s,
    background-color 0.3s;
}
.advantage-card:hover {
  box-shadow: var(--shadow-md);
  transform: translateY(-5px);
}
.icon-box {
  width: 48px;
  height: 48px;
  /* 替换 brand-light */
  background-color: var(--brand-light);
  color: var(--brand-color);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  margin-bottom: 24px;
}
.card-title {
  font-size: 20px;
  font-weight: 700;
  margin-bottom: 12px;
}
.card-desc {
  color: var(--text-muted);
  font-size: 14px;
  line-height: 1.6;
}

/* ================= 4. Teachers Section ================= */
.teachers-section {
  /* 替换 white */
  background-color: var(--bg-white);
  padding: 80px 0;
  transition: background-color 0.3s;
}
.teachers-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 30px;
  text-align: center;
}
.teacher-card {
  padding: 20px;
}
.avatar-wrapper {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  overflow: hidden;
  margin: 0 auto 20px;
  /* 替换 #f3f4f6 */
  background-color: var(--bg-gray);
}
.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
  /* 暗黑模式下稍微降低图片亮度，减少刺眼感 */
  filter: brightness(0.9);
}
html.dark .avatar-img {
  filter: brightness(0.8);
}
.teacher-card:hover .avatar-img {
  transform: scale(1.1);
}
.teacher-name {
  font-size: 18px;
  font-weight: 700;
  margin-bottom: 5px;
}
.teacher-role {
  color: var(--brand-color);
  font-size: 14px;
  margin-bottom: 10px;
}
.teacher-desc {
  color: var(--text-muted);
  font-size: 12px;
  line-height: 1.5;
}

/* ================= 5. Timeline Section ================= */
.timeline-section {
  padding: 80px 0;
}
.timeline-container {
  position: relative;
  max-width: 800px;
  margin: 0 auto;
  padding: 20px 0;
}
.timeline-line {
  position: absolute;
  top: 0;
  bottom: 0;
  left: 50%;
  width: 2px;
  /* 替换 brand-light */
  background-color: var(--brand-light);
  transform: translateX(-50%);
}
.timeline-item {
  position: relative;
  width: 50%;
  padding: 0 40px;
  margin-bottom: 40px;
  box-sizing: border-box;
}
.timeline-left {
  left: 0;
  text-align: right;
}
.timeline-right {
  left: 50%;
  text-align: left;
}
.timeline-dot {
  position: absolute;
  top: 8px;
  width: 16px;
  height: 16px;
  background-color: var(--brand-color);
  border-radius: 50%;
  /* 替换 white */
  border: 4px solid var(--bg-white);
  box-shadow: 0 0 0 2px var(--brand-light);
}
.timeline-left .timeline-dot {
  right: -8px;
}
.timeline-right .timeline-dot {
  left: -8px;
}
.timeline-year {
  color: var(--brand-color);
  font-weight: 700;
  font-size: 20px;
  margin-bottom: 5px;
}
.timeline-title {
  font-size: 18px;
  font-weight: 700;
  margin-bottom: 8px;
}
.timeline-desc {
  color: var(--text-muted);
  font-size: 14px;
  line-height: 1.6;
}

/* ================= 6. Contact Section ================= */
.contact-section {
  /* 替换 white */
  background-color: var(--bg-white);
  border-top: 1px solid var(--border-color);
  padding: 60px 0;
  transition:
    background-color 0.3s,
    border-color 0.3s;
}
.contact-container {
  text-align: center;
}
.contact-flex {
  display: flex;
  justify-content: center;
  gap: 60px;
  margin-top: 40px;
}
.qr-placeholder {
  width: 130px;
  height: 130px;
  /* 替换 #f9fafb */
  background-color: var(--bg-gray);
  /* 替换 #d1d5db */
  border: 2px dashed var(--border-color);
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  /* 替换 #9ca3af */
  color: var(--text-muted);
  font-size: 14px;
  margin-bottom: 15px;
}
.contact-label {
  color: var(--text-muted);
  font-size: 14px;
}

/* ================= 滚动淡入动画 ================= */
.fade-in-up {
  opacity: 0;
  transform: translateY(30px);
  transition:
    opacity 0.8s ease-out,
    transform 0.8s ease-out;
}
.fade-in-up.visible {
  opacity: 1;
  transform: translateY(0);
}

/* ================= 响应式设计 (媒体查询) ================= */
@media (max-width: 1024px) {
  .teachers-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .hero-title {
    font-size: 32px;
  }
  .hero-subtitle {
    font-size: 16px;
  }
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
    row-gap: 30px;
  }
  .stat-item:nth-child(odd) {
    border-right: 1px solid var(--border-color);
  }
  .stat-item:nth-child(even) {
    border-right: none;
  }
  .advantages-grid {
    grid-template-columns: 1fr;
  }
  .teachers-grid {
    grid-template-columns: 1fr;
  }
  .timeline-line {
    left: 20px;
  }
  .timeline-item {
    width: 100%;
    left: 0;
    text-align: left;
    padding-left: 50px;
    padding-right: 0;
  }
  .timeline-left .timeline-dot,
  .timeline-right .timeline-dot {
    left: 12px;
    right: auto;
  }
  .contact-flex {
    flex-direction: column;
    align-items: center;
    gap: 30px;
  }
}
</style>
