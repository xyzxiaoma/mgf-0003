import request from '../utils/request'

export function checkin(activityId, userId, remark) {
  return request({
    url: `/activities/${activityId}/checkins`,
    method: 'post',
    data: { userId, remark }
  })
}

export function getActivityCheckins(id) {
  return request({
    url: `/activities/${id}/checkins`,
    method: 'get'
  })
}

export function getCheckinStatus(id) {
  return request({
    url: `/activities/${id}/checkins/status`,
    method: 'get'
  })
}
