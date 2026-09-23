import { request } from '@/Utils/request.js'

// 登录

export const LoginApi = (data) => request.post(`/login/${data.userType}`, data)
