<!-- 获取请求路径中的参数，向后端发起请求pathVailable -->
<script setup>
import { useRoute } from 'vue-router'
import { getDetail } from '@/api/courses'
import { ref, onMounted } from 'vue'
import { ElNotification } from 'element-plus'
import { BuyCourses } from '@/api/StuCourses'
import { getUserInfo } from '@/Utils/userInfo'

// 获取个人信息
const stuid = getUserInfo()?.userId ?? null

const route = useRoute()
// 获取课程id（从路径获取）
const coursesId = route.params.id

// 获取课程信息（onMounted）
const courseDetails = ref({})
const getDatail = async () => {
  try {
    const res = await getDetail(coursesId)
    courseDetails.value = res.data
    console.log('课程详情数据:', res.data)
  } catch (error) {
    console.error('获取课程详情失败:', error)
  }
}

onMounted(() => {
  getDatail()
})

const goBack = () => {
  window.history.back()
}

const activeName = ref('first')
const handleClick = (tab, event) => {
  console.log(tab, event)
}

// 购买相关
const outerVisible = ref(false)
const innerVisible = ref(false)

const BeforeBuy = () => {
  outerVisible.value = true
}

const successBuy = async () => {
  if (stuid === null) {
    ElNotification({
      progress: true,
      duration: 3000,
      type: 'error',
      message: '请先登录',
    })
    return
  }

  const res = await BuyCourses({
    stuId: stuid,
    coursesId: coursesId,
  })
  if (res.code === 200) {
    ElNotification({
      type: 'success',
      title: '支付结果',
      message: '购买成功,请到个人空间查看',
    })
    innerVisible.value = false
    outerVisible.value = false
  } else {
    ElNotification({
      progress: true,
      duration: 3000,
      type: 'error',
      title: '支付结果',
      message: '您已购买该课程，不可重复购买',
    })
    innerVisible.value = false
    outerVisible.value = false
  }
}

// 章节目录
const value = ref()
const data = ref()

// ai客服

const drawer = ref(false)
</script>

<template>
  <div class="main">
    <div class="container-left">
      <el-page-header @back="goBack" title="返回">
        <template #content>
          <span class="text-large font-600 mr-3">{{ courseDetails.coursesName }}</span>
        </template>
      </el-page-header>

      <div class="header">
        <h1>{{ courseDetails.coursesName }}</h1>
      </div>

      <span class="desc">{{ courseDetails.description }}</span>

      <el-tabs v-model="activeName" class="tabs" @tab-click="handleClick">
        <el-tab-pane label="课程介绍" name="first">
          <div class="box">{{ courseDetails.description }}</div>
        </el-tab-pane>
        <el-tab-pane label="章节目录" name="second">
          章节目录
          <div class="box">
            <el-tree-select
              v-model="value"
              :data="data"
              check-strictly
              :render-after-expand="false"
              style="width: 240px"
            />
          </div>
          <el-divider />
        </el-tab-pane>
      </el-tabs>
    </div>

    <div class="container-right">
      <h3>课程价格</h3>
      <h4 class="price">￥{{ courseDetails.price }}</h4>
      <ul>
        <li>专属1对1指导</li>
        <li>随堂答疑+作业批改</li>
        <li>阶段测评与学习报告</li>
        <li>结业就业指导与内推</li>
      </ul>
      <el-button class="buy-btn" @click="BeforeBuy">立刻报名</el-button>
      <el-button class="service-btn" type="primary" @click="drawer = true">咨询客服</el-button>
      <p class="tip">报名后即可进入学员空间学习，并随时使用课程专属AI答疑</p>
    </div>
  </div>

  <!-- 购买弹窗 -->
  <el-dialog v-model="outerVisible" :title="courseDetails.coursesName" width="800">
    <p>课程代码:{{ courseDetails.coursesId }}</p>
    <p>课程名称:{{ courseDetails.coursesName }}</p>
    <p>课程类型:{{ courseDetails.coategory }}</p>
    <p>课程描述:{{ courseDetails.description }}</p>
    <p>课程总学时:{{ courseDetails.totalHours }}</p>

    <el-dialog v-model="innerVisible" width="600" title="确认支付" append-to-body>
      <div style="text-align: center">
        <p style="font-size: 22px">请扫码支付</p>
        <h3 style="font-size: 25px">${{ courseDetails.price }}</h3>
        <div style="justify-content: center; margin-bottom: 10px">
          <img
            style="object-fit: cover; width: 400px; border-radius: 10%"
            src="../../components/icons/apy.jpg"
            alt=""
          />
        </div>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="innerVisible = false">取消支付</el-button>
          <el-button type="primary" @click="successBuy">已支付</el-button>
        </div>
      </template>
    </el-dialog>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="outerVisible = false">取消支付</el-button>
        <el-button type="primary" @click="innerVisible = true">
          确认支付
          <span style="margin-left: 5px">${{ courseDetails.price }}</span>
        </el-button>
      </div>
    </template>
  </el-dialog>

  <!-- ai客服 -->
  <el-drawer v-model="drawer" title="I am the title" :with-header="false">
    <span>Hi there!</span>
    <br />
    <span>正在开发中</span>
  </el-drawer>
</template>

<style scoped>
/* ========== 页面主体 ========== */
.main {
  /* 顶部留出导航栏高度，避免内容被遮挡 */
  padding: 80px 100px 40px;
  width: 100%;
  box-sizing: border-box;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 30px;
}

/* ========== 左侧内容 ========== */
.container-left {
  flex: 1;
  min-width: 0; /* 防止子元素撑破 flex 容器 */
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.header h1 {
  font-size: 32px;
  font-weight: 700;
  margin: 16px 0 12px;
}

.desc {
  color: #9a9a9a;
  font-size: 14px;
  margin-bottom: 20px;
}

.tabs {
  width: 100%;
}

.box {
  width: 100%;
  min-height: 500px;
  margin: 10px 0;
  padding: 20px;
  border-radius: 20px;
  background-color: var(--card-bg, #fafafa);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
  border: 1px solid var(--card-border, rgba(0, 0, 0, 0.06));
  box-sizing: border-box;
}

/* ========== 右侧卡片 ========== */
.container-right {
  width: 320px;
  flex-shrink: 0;
  padding: 24px 28px;
  border-radius: 20px;
  background-color: var(--card-bg, #fafafa);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.4);
  border: 1px solid var(--card-border, rgba(0, 0, 0, 0.06));
  height: auto;
  box-sizing: border-box;
}

.container-right h3 {
  font-size: 16px;
  font-weight: 600;
  margin: 0 0 10px;
  color: #eaeaea;
}

.container-right .price {
  font-family: 'FangSong', serif;
  font-size: 36px;
  letter-spacing: 4px;
  color: var(--brand-orange, #ff8b60);
  margin: 10px 0 20px;
  line-height: 1.2;
}

.container-right ul {
  padding-left: 18px;
  margin: 0 0 20px;
}

.container-right li {
  margin-bottom: 10px;
  font-size: 14px;
  line-height: 1.6;
}

.container-right .el-button {
  width: 100%;
  height: auto;
  padding: 10px 0;
  border-radius: 12px;
  margin: 0 0 12px;
  justify-content: center;
  align-items: center;
}

.container-right .el-button + .el-button {
  margin-left: 0;
}

.container-right :deep(.el-button:hover) {
  background-color: var(--text-sub-color, #c0c4cc);
  color: var(--bg-color, #fff);
}

.container-right .tip {
  font-size: 12px;
  color: #9a9a9a;
  line-height: 1.6;
  margin: 8px 0 0;
}

/* ========== 顶部 PageHeader ========== */
:deep(.el-page-header__title) {
  font-size: 22px;
  font-weight: 700;
  font-family: 'FangSong', serif;
}

.el-page-header {
  margin-left: 0;
  margin-bottom: 20px;
  color: var(--text-color, #303133);
}

.el-page-header:hover {
  color: rgb(121, 187, 255);
}

/* ========== 响应式 ========== */
@media (max-width: 1024px) {
  .main {
    padding: 80px 40px 40px;
    flex-direction: column;
  }

  .container-right {
    width: 100%;
  }
}
</style>
