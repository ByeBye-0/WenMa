<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { Search } from '@element-plus/icons-vue'
import { getCoursesApi, searchCourese } from '@/api/courses'
const Courses = ref([])
const loading = ref(false)
const searchInfo = ref('')
const router = useRouter()
const search = async () => {
  if (!searchInfo.value) {
    getCourses()
  }
  const res = await searchCourese(searchInfo.value)
  if (res.code === 200) {
    Courses.value = Array.isArray(res.data) ? res.data : [res.data]
  }
}
watch(searchInfo, (newVal, oldVal) => {
  console.log('新值：', newVal, '旧值：', oldVal)
})
const getCourses = async () => {
  try {
    const res = await getCoursesApi()
    Courses.value = res.data
    console.log('课程数据:', res.data)
  } catch (error) {
    console.error('获取课程数据失败:', error)
  }
}
onMounted(() => {
  document.title = '课程中心'
  getCourses()
})
</script>

<template>
  <div class="container">
    <h1>hello,欢迎来到课程中心</h1>
    <div class="search">
      <el-input
        v-model="searchInfo"
        style="width: 400px; height: 50px"
        placeholder="请输入课程名称"
        :suffix-icon="Search"
        clearable
      />
      <el-button
        :icon="Search"
        style="line-height: 50px; width: 110px; margin-left: 25px; height: 50px; padding: 20px"
        @click="search"
        >查询</el-button
      >
    </div>

    <div class="site-main" v-loading="loading">
      <h1>热门课程</h1>
      <div class="card">
        <el-card
          v-for="course in Courses"
          :key="course.id"
          style="max-width: 350px"
          shadow="hover"
          @click="router.push(`/courses/${course.coursesId}`)"
        >
          <template #header>
            <div class="card-header">
              <span>{{ course.coursesName }}</span>
            </div>
          </template>
          <p class="text item">{{ course.description }}</p>
          <template #footer>
            <div class="card-footer">
              <span class="price">¥{{ course.price }}</span>
            </div>
          </template>
        </el-card>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: start;
  width: 100%;
  margin-top: 20px;
  margin-bottom: 50px;
}
/* .site-main {
  flex: 1;
} */
.card {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 20px;
  min-width: 1000px;
  margin-top: 25px;
}
.card :deep(.el-card) {
  background: linear-gradient(180deg, var(--card-bg) 0%, #0f172a 100%);
  border: 1px solid var(--card-border);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
}
.card :deep(.el-card):hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.5);
}
.card :deep(.el-card__header) span {
  color: var(--text-color);
}
.card :deep(.el-card__body) p {
  color: var(--text-sub-color);
}
</style>
