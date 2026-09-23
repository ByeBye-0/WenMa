import { request } from '@/Utils/request.js'

// 获取所有课程信息---
export const getCoursesApi = () => request.get('/courses/all')
// 根据课程id查找单个课程，返回单个课程详细信息--操作courses表
export const getCourseById = (id) => request.get(`/courses/${id}`)
// 根据条件查询课程--搜索课程（名字或编号）
export const searchCourese = (data) => request.get(`/courses/${data}`)

// 返回单个课程详细信息--在课程详细页面
export const getDetail = (id) => request.get(`/courses/detail/${id}`)

// 返回多个课程信息
export const getCourses = (params) => request.get(`/courses/list/${params}`)
