<template>
  <el-container class="layout-container">
    <el-header class="header">
      <div class="header-content">
        <div class="logo">
          <el-icon size="28"><School /></el-icon>
          <span>校园社团活动管理系统</span>
        </div>
        <div class="user-info">
          <el-icon><User /></el-icon>
          <span>{{ user.name }} ({{ user.role === 'ADMIN' ? '管理员' : '学生' }})</span>
          <el-button type="danger" size="small" @click="handleLogout">退出登录</el-button>
        </div>
      </div>
    </el-header>
    <el-container>
      <el-aside width="220px" class="aside">
        <el-menu
          :default-active="activeMenu"
          router
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
        >
          <el-menu-item index="/activities">
            <el-icon><Tickets /></el-icon>
            <span>活动列表</span>
          </el-menu-item>
          <el-menu-item index="/my-registrations">
            <el-icon><Document /></el-icon>
            <span>我的报名</span>
          </el-menu-item>
          <template v-if="user.role === 'ADMIN'">
            <el-sub-menu index="admin">
              <template #title>
                <el-icon><Setting /></el-icon>
                <span>管理中心</span>
              </template>
              <el-menu-item index="/admin/activities">活动管理</el-menu-item>
              <el-menu-item index="/admin/stats">统计分析</el-menu-item>
            </el-sub-menu>
          </template>
        </el-menu>
      </el-aside>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { School, User, Tickets, Document, Setting } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()

const user = ref(JSON.parse(localStorage.getItem('user') || '{}'))

const activeMenu = computed(() => route.path)

const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    ElMessage.success('已退出登录')
    router.push('/login')
  }).catch(() => {})
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.header {
  background-color: #fff;
  border-bottom: 1px solid #e4e7ed;
  padding: 0;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
  padding: 0 24px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 20px;
  font-weight: bold;
  color: #303133;
}

.logo .el-icon {
  color: #409EFF;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  color: #606266;
}

.aside {
  background-color: #304156;
}

.main {
  background-color: #f0f2f5;
  padding: 20px;
}
</style>
