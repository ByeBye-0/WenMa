const KEY = 'userInfo'

/**
 * 读取本地用户信息。脏数据自动清理，未登录或数据非法一律返回 null。
 */
export function getUserInfo() {
  const raw = localStorage.getItem(KEY)
  if (!raw) return null
  try {
    const info = JSON.parse(raw)
    return info && typeof info === 'object' ? info : null
  } catch {
    clearUserInfo()
    return null
  }
}

/**
 * 写入用户信息。data 为空时跳过，避免把 "undefined" 之类的脏值存进去。
 */
export function setUserInfo(data) {
  if (!data) return
  localStorage.setItem(KEY, JSON.stringify(data))
}

export function clearUserInfo() {
  localStorage.removeItem(KEY)
}

/**
 * 请求头用的 token，未登录返回 null。
 */
export function getToken() {
  return getUserInfo()?.token ?? null
}
