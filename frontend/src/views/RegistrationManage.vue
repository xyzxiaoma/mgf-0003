<template>
  <div class="registration-manage">
    <div class="page-header">
      <h2>报名人员管理</h2>
      <el-button @click="goBack">
        <el-icon><ArrowLeft /></el-icon>
        返回活动列表
      </el-button>
    </div>
    <el-card class="activity-info-card">
      <h3>活动信息</h3>
      <div class="info-row" v-if="activity">
        <span><strong>活动名称：</strong>{{ activity.name }}</span>
        <span><strong>社团：</strong>{{ activity.clubName }}</span>
        <span><strong>日期：</strong>{{ activity.activityDate }}</span>
        <span><strong>地点：</strong>{{ activity.location }}</span>
      </div>
    </el-card>
    <el-table :data="registrations" border stripe>
      <el-table-column prop="studentName" label="姓名" width="100" />
      <el-table-column prop="studentNo" label="学号" width="120" />
      <el-table-column prop="phone" label="手机号" width="120" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 'REGISTERED' ? 'success' : 'info'">
            {{ row.status === 'REGISTERED' ? '已报名' : '已取消' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="签到状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.checkedIn ? 'success' : 'warning'">
            {{ row.checkedIn ? '已签到' : '未签到' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="registeredAt" label="报名时间" width="160">
        <template #default="{ row }">
          {{ formatDate(row.registeredAt) }}
        </template>
      </el-table-column>
    </el-table>
    <el-empty v-if="registrations.length === 0" description="暂无报名人员" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import dayjs from 'dayjs'
import { getActivities } from '@/api/activity'
import { getActivityRegistrations } from '@/api/registration'

const route = useRoute()
const router = useRouter()
const activityId = route.params.activityId

const activity = ref(null)
const registrations = ref([])

const loadData = async () => {
  try {
    const activities = await getActivities()
    activity.value = activities.find(a => a.id === Number(activityId))
    registrations.value = await getActivityRegistrations(activityId)
  } catch (error) {
    console.error(error)
  }
}

const goBack = () => {
  router.push('/admin/activities')
}

const formatDate = (date) => {
  return dayjs(date).format('YYYY-MM-DD HH:mm')
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
}

.activity-info-card {
  margin-bottom: 20px;
}

.activity-info-card h3 {
  margin: 0 0 15px 0;
}

.info-row {
  display: flex;
  flex-wrap: wrap;
  gap: 30px;
}

.info-row span {
  color: #606266;
}
</style>
