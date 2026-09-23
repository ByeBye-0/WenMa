import axios from 'axios'

export const request = axios.create({
  baseURL: '/api',
  timeout: 5000,
})

// 添加响应拦截器
request.interceptors.response.use(
  (response) => {
    return response.data
  },
  (err) => {
    return Promise.reject(err)
  },
)
// 请求拦截器
request.interceptors.request.use(
  function (config) {
    // 在发送请求之前做些什么
    config.headers.token = JSON.parse(localStorage.getItem('userInfo')).token

    return config
  },
  function (error) {
    // 对请求错误做些什么
    return Promise.reject(error)
  },
)
