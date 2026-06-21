import { createRouter, createWebHistory } from 'vue-router'
import { ElMessage } from 'element-plus'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    component: () => import('@/views/Layout.vue'),
    meta: { requiresAuth: true },
    redirect: '/activities',
    children: [
      {
        path: 'activities',
        name: 'ActivityList',
        component: () => import('@/views/ActivityList.vue')
      },
      {
        path: 'my-registrations',
        name: 'MyRegistrations',
        component: () => import('@/views/MyRegistrations.vue')
      },
      {
        path: 'admin/activities',
        name: 'ActivityManage',
        component: () => import('@/views/ActivityManage.vue'),
        meta: { requiresAdmin: true }
      },
      {
        path: 'admin/registrations/:activityId',
        name: 'RegistrationManage',
        component: () => import('@/views/RegistrationManage.vue'),
        meta: { requiresAdmin: true }
      },
      {
        path: 'admin/checkins/:activityId',
        name: 'CheckinManage',
        component: () => import('@/views/CheckinManage.vue'),
        meta: { requiresAdmin: true }
      },
      {
        path: 'admin/stats',
        name: 'Stats',
        component: () => import('@/views/Stats.vue'),
        meta: { requiresAdmin: true }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const user = JSON.parse(localStorage.getItem('user') || '{}')

  if (to.meta.requiresAuth && !token) {
    ElMessage.warning('请先登录')
    next('/login')
    return
  }

  if (to.meta.requiresAdmin && user.role !== 'ADMIN') {
    ElMessage.error('没有权限访问')
    next('/')
    return
  }

  if ((to.path === '/login' || to.path === '/register') && token) {
    next('/')
    return
  }

  next()
})

export default router
