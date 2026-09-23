<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { User, Collection, Timer, Plus } from '@element-plus/icons-vue'
import { ElMessage, ElNotification } from 'element-plus'
import { getIds, getMyCourses, getProgress } from '@/api/StuCourses'

const router = useRouter()

onMounted(() => {
  Ids()
})

// 头像上传--阿里云oss
const imageUrl = ref(
  'https://smy-wen-ma.oss-cn-beijing.aliyuncs.com/2026-09-18/ba4bf14e-52d2-4f21-a934-de752fd99444-.png',
)

const handleAvatarSuccess = (response, uploadFile) => {
  imageUrl.value = URL.createObjectURL(uploadFile.raw)
  ElNotification({
    title: '更改头像',
    type: 'Success',
    message: '头像上传成功',
    duration: 3000,
    progress: true,
  })
}

const beforeAvatarUpload = (rawFile) => {
  if (
    rawFile.type === 'image/jpeg' ||
    rawFile.type === 'image/png' ||
    rawFile.type === 'image/jpg'
  ) {
    return true
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error('Avatar picture size can not exceed 2MB!')
    return false
  }
  return false
}

// 获取课程表信息(学生学号,课程代码)
const StuInfo = ref([])
const coursesNum = ref([])
const coursesRes = ref([])
const coursesProgress = ref([])

const userInfo = localStorage.getItem('userInfo')
const stuid = userInfo ? JSON.parse(userInfo).userId : null
const coursesIds = ref([])

const Ids = async () => {
  if (stuid === null) {
    ElNotification({
      title: '未知错误',
      type: 'error',
      message: '无法获取学生学号,请重新登录',
    })
    return
  }

  const res = await getIds(stuid)
  if (res.code === 200) {
    StuInfo.value = Array.isArray(res.data) ? res.data : [res.data]
    coursesIds.value = StuInfo.value.map((item) => item.coursesId)
    coursesNum.value = StuInfo.value.filter((item) => item.score === 0)
    coursesRes.value = StuInfo.value.filter((item) => item.score >= 90)

    // ✅ 正确赋值：数组形式
    coursesProgress.value = StuInfo.value.map((item) => ({
      coursesId: item.coursesId,
      progress: item.progress,
    }))
    console.log('coursesProgress:', coursesProgress.value)

    await getCourses()
  }
}

const studentInfo = ref(JSON.parse(userInfo))
const myCourses = ref([])

// 获取学生课程信息，并把进度合并进每个课程
const getCourses = async () => {
  const res = await getMyCourses(coursesIds.value)
  if (res.code !== 200) {
    ElNotification({
      title: '未知错误',
      type: 'error',
      message: '无法获取学生课表,请重新登录',
    })
    return
  }

  // 建立 coursesId -> progress 的映射
  const progressMap = {}
  coursesProgress.value.forEach((p) => {
    progressMap[p.coursesId] = p.progress
  })

  // 合并进 myCourses
  myCourses.value = res.data.map((c) => ({
    ...c,
    progress: progressMap[c.coursesId] ?? 0,
  }))

  console.log('myCourses with progress:', myCourses.value)
}

const gotoCourses = (id) => {
  router.push(`/courses/${id}`)
}
</script>

<template>
  <div class="space-container">
    <div class="glass-bg"></div>
    <main class="space-main">
      <aside class="profile-card">
        <div class="avatar-box">
          <el-upload
            class="avatar-uploader"
            action="/api/Upload/avtor"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <img v-if="imageUrl" :src="imageUrl" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </div>
        <h2 class="name">{{ studentInfo.name }}</h2>
        <p class="motto">身份：{{ studentInfo.userType === '1' ? '学生' : '员工' }}</p>

        <div class="stats-grid">
          <div class="stat-item">
            <el-icon size="22"><Collection /></el-icon>
            <span class="stat-value">{{ StuInfo.length }}</span>
            <span class="stat-label">已购买的课程</span>
          </div>
          <div class="stat-item">
            <el-icon size="22"><User /></el-icon>
            <span class="stat-value">{{ coursesNum.length }}</span>
            <span class="stat-label">在学课程</span>
          </div>
          <div class="stat-item">
            <el-icon size="22"><Timer /></el-icon>
            <span class="stat-value">126h</span>
            <span class="stat-label">学习时长</span>
          </div>
          <div class="stat-item">
            <el-icon size="22"><Collection /></el-icon>
            <span class="stat-value">{{ coursesRes.length }}</span>
            <span class="stat-label">获得证书</span>
          </div>
        </div>
      </aside>

      <section class="courses-card">
        <h2 class="section-title">我的课程</h2>
        <div class="course-list">
          <div>
            <el-scrollbar height="400px">
              <div
                class="courses"
                v-for="c in myCourses"
                :key="c.coursesId"
                @click="gotoCourses(c.coursesId)"
              >
                <div class="course-info">
                  <h3>{{ c.coursesName }}</h3>
                  <p>{{ c.description }}</p>
                </div>
                <el-progress
                  :percentage="c.progress"
                  :stroke-width="8"
                  color="#eaeaea"
                  track-color="rgba(255,255,255,0.1)"
                />
              </div>
            </el-scrollbar>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<style scoped>
/* 样式保持你原来的即可，未做修改 */
.courses {
  background-color: rgba(34, 33, 33, 0.65);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 20px;
  padding: 5px;
  margin-bottom: 10px;
  transition:
    background-color 0.35s ease,
    border-color 0.35s ease,
    box-shadow 0.35s ease,
    transform 0.35s ease;
  cursor: pointer;
}

.courses:hover {
  background-color: rgba(255, 255, 255, 0.06);
  border-color: rgba(255, 255, 255, 0.18);
  box-shadow:
    0 0 0 1px rgba(255, 255, 255, 0.04),
    0 8px 24px -8px rgba(0, 0, 0, 0.6),
    inset 0 1px 0 rgba(255, 255, 255, 0.08);
  transform: translateY(-1px);
}

.avatar-uploader .avatar {
  background-color: rgb(220, 4, 4);
  width: 100px;
  height: 100px;
  display: block;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #fff;
  box-shadow: 1px 1px 1px 1px inset;
  transition: all 0.6s ease;
}
.avatar-uploader .avatar:hover {
  transform: scale(1.1);
  filter: blur(1px) brightness(0.5);
}

.space-container {
  position: relative;
  height: 85vh;
  overflow: hidden;
  background-color: #0b0b0b;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #eaeaea;
  font-family: 'Inter', 'Helvetica Neue', sans-serif;
}

.glass-bg {
  position: fixed;
  inset: 0;
  background: radial-gradient(circle at 30% 20%, #1a1a1a 0%, #050505 90%);
  z-index: 0;
}

.space-main {
  position: relative;
  z-index: 10;
  width: 90%;
  max-width: 1200px;
  display: flex;
  gap: 24px;
  padding: 8px 0;
  max-height: 95vh;
}

.profile-card,
.courses-card {
  background: rgba(22, 22, 22, 0.68);
  backdrop-filter: blur(18px) saturate(120%);
  -webkit-backdrop-filter: blur(18px) saturate(120%);
  border: 1px solid rgba(220, 220, 220, 0.15);
  border-radius: 24px;
  box-shadow:
    0 25px 50px -8px rgba(0, 0, 0, 0.7),
    inset 0 1px 0 rgba(255, 255, 255, 0.05);
}

.profile-card {
  width: 30%;
  padding: 40px 24px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.avatar-box {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
}

.name {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: 8px;
  letter-spacing: 1px;
}
.motto {
  font-size: 0.85rem;
  color: #9a9a9a;
  margin-bottom: 40px;
  text-align: center;
  font-style: italic;
}

.stats-grid {
  width: 100%;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 16px 0;
  background: rgba(255, 255, 255, 0.03);
  border-radius: 12px;
  border: 1px solid rgba(255, 255, 255, 0.05);
}

.stat-value {
  font-size: 1.2rem;
  font-weight: 700;
}
.stat-label {
  font-size: 0.75rem;
  color: #9a9a9a;
  letter-spacing: 1px;
}

.courses-card {
  width: 70%;
  padding: 40px;
}

.section-title {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: 30px;
  letter-spacing: 1px;
}

.course-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.course-info h3 {
  font-size: 1.1rem;
  margin-bottom: 8px;
  font-weight: 500;
}
.course-info p {
  font-size: 0.85rem;
  color: #9a9a9a;
  margin-bottom: 16px;
}

@media (max-width: 768px) {
  .space-main {
    flex-direction: column;
  }
  .profile-card,
  .courses-card {
    width: 100%;
  }
}
</style>
