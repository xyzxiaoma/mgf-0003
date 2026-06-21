import request from '../utils/request'

export function getMonthlyStats(month) {
  return request({
    url: '/stats',
    method: 'get',
    params: { month }
  })
}
