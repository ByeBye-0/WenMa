import { request } from '@/Utils/request'
// 根据学生id/获取课程表信息
export const getIds = (StuId) => request.get(`/stucourses/ids/${StuId}`)

// 支付课程
export const BuyCourses = (data) => request.post('/stucourses/add', data)

// 查看我的课程表
export const getMyCourses = (coursesIds) =>
  request.get(`/courses/list`, {
    params: { coursesIds },
    paramsSerializer: {
      indexes: null, // 关键：输出 coursesIds=C0001&coursesIds=C002，而不是 coursesIds[]=
    },
  })

//   查看课表进度
export const getProgress = (id) => request.get(`/stucourses/ids/${id}`)
