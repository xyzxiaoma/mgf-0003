<template>
  <div class="activity-list">
    <div class="page-header">
      <h2>活动列表</h2>
      <div class="filter">
        <el-date-picker
          v-model="filterDate"
          type="date"
          placeholder="按日期筛选"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
          @change="handleDateFilter"
        />
        <el-button @click="clearFilter">清除筛选</el-button>
      </div>
    </div>
    <el-row :gutter="20">
      <el-col :span="8" v-for="activity in activities" :key="activity.id">
        <el-card class="activity-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span class="activity-name">{{ activity.name }}</span>
              <el-tag :type="getStatusType(activity.status)">
                {{ getStatusText(activity.status) }}
              </el-tag>
            </div>
          </template>
          <div class="activity-info">
            <p><el-icon><OfficeBuilding /></el-icon> {{ activity.clubName }}</p>
            <p><el-icon><Calendar /></el-icon> {{ activity.activityDate }}</p>
            <p><el-icon><Location /></el-icon> {{ activity.location }}</p>
            <p><el-icon><User /></el-icon> 人数：{{ activity.registeredCount }}/{{ activity.maxCapacity }}</p>
            <el-progress
              :percentage="Math.round((activity.registeredCount / activity.maxCapacity) * 100)"
              :status="activity.registeredCount >= activity.maxCapacity ? 'exception' : ''"
            />
          </div>
          <div class="activity-desc" v-if="activity.description">
            {{ activity.description }}
          </div>
          <template #footer>
            <div class="card-footer">
              <span class="create-time">创建于 {{ formatDate(activity.createdAt) }}</span>
              <el-button
                type="primary"
                size="small"
                @click="handleRegister(activity)"
                :disabled="activity.registeredCount >= activity.maxCapacity"
              >
                立即报名
              </el-button>
            </div>
          </template>
        </el-card>
      </el-col>
    </el-row>
    <el-empty v-if="activities.length === 0" description="暂无活动" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { OfficeBuilding, Calendar, Location, User } from '@element-plus/icons-vue'
import dayjs from 'dayjs'
import { getActivities, getActivitiesByDate } from '@/api/activity'
import { registerActivity } from '@/api/registration'

const activities = ref([])
const filterDate = ref(null)

const loadActivities = async () => {
  try {
    activities.value = await getActivities()
  } catch (error) {
    console.error(error)
  }
}

const handleDateFilter = async (date) => {
  if (date) {
    activities.value = await getActivitiesByDate(date)
  }
}

const clearFilter = () => {
  filterDate.value = null
  loadActivities()
}

const handleRegister = async (activity) => {
  ElMessageBox.confirm(`确定要报名参加「${activity.name}」吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'info'
  }).then(async () => {
    try {
      await registerActivity(activity.id)
      ElMessage.success('报名成功')
      loadActivities()
    } catch (error) {
      console.error(error)
    }
  }).catch(() => {})
}

const getStatusType = (status) => {
  const types = {
    'NOT_STARTED': 'info',
    'IN_PROGRESS': 'success',
    'ENDED': 'warning'
  }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = {
    'NOT_STARTED': '未开始',
    'IN_PROGRESS': '进行中',
    'ENDED': '已结束'
  }
  return texts[status] || status
}

const formatDate = (date) => {
  return dayjs(date).format('YYYY-MM-DD HH:mm')
}

onMounted(() => {
  loadActivities()
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

.filter {
  display: flex;
  gap: 10px;
}

.activity-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.activity-name {
  font-weight: bold;
  font-size: 16px;
}

.activity-info {
  margin-bottom: 15px;
}

.activity-info p {
  margin: 8px 0;
  display: flex;
  align-items: center;
  gap: 6px;
  color: #606266;
}

.activity-desc {
  background-color: #f5f7fa;
  padding: 10px;
  border-radius: 4px;
  color: #909399;
  font-size: 13px;
  margin-bottom: 15px;
  max-height: 60px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.create-time {
  color: #c0c4cc;
  font-size: 12px;
}
</style>
