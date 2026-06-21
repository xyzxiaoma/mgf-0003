import request from '../utils/request'

export function getActivities() {
  return request({
    url: '/activities',
    method: 'get'
  })
}

export function getActivitiesByDate(date) {
  return request({
    url: '/activities/day',
    method: 'get',
    params: { date }
  })
}

export function createActivity(data) {
  return request({
    url: '/activities',
    method: 'post',
    data
  })
}

export function updateActivity(id, data) {
  return request({
    url: `/activities/${id}`,
    method: 'put',
    data
  })
}

export function updateActivityStatus(id, status) {
  return request({
    url: `/activities/${id}/status`,
    method: 'patch',
    data: { status }
  })
}

export function deleteActivity(id) {
  return request({
    url: `/activities/${id}`,
    method: 'delete'
  })
}
