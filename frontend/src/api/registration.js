import request from '../utils/request'

export function registerActivity(id) {
  return request({
    url: `/activities/${id}/registrations`,
    method: 'post'
  })
}

export function getActivityRegistrations(id) {
  return request({
    url: `/activities/${id}/registrations`,
    method: 'get'
  })
}

export function getMyRegistrations() {
  return request({
    url: '/users/me/registrations',
    method: 'get'
  })
}

export function cancelRegistration(id) {
  return request({
    url: `/registrations/${id}`,
    method: 'delete'
  })
}
