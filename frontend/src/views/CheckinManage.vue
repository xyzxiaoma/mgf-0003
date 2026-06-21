<template>
  <div class="checkin-manage">
    <div class="page-header">
      <h2>签到管理</h2>
      <el-button @click="goBack">
        <el-icon><ArrowLeft /></el-icon>
        返回活动列表
      </el-button>
    </div>

    <el-row :gutter="20" class="stats-row">
      <el-col :span="8">
        <el-card>
          <div class="stat-item">
            <div class="stat-label">总报名人数</div>
            <div class="stat-value">{{ checkinStatus.totalRegistered }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div class="stat-item success">
            <div class="stat-label">已签到</div>
            <div class="stat-value">{{ checkinStatus.checkedInCount }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div class="stat-item warning">
            <div class="stat-label">未签到</div>
            <div class="stat-value">{{ checkinStatus.notCheckedInCount }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-tabs v-model="activeTab">
      <el-tab-pane label="未签到人员" name="notCheckedIn">
        <el-table :data="checkinStatus.notCheckedInList || []" border stripe>
          <el-table-column prop="studentName" label="姓名" width="100" />
          <el-table-column prop="studentNo" label="学号" width="120" />
          <el-table-column prop="phone" label="手机号" width="120" />
          <el-table-column prop="registeredAt" label="报名时间" width="160">
            <template #default="{ row }">
              {{ formatDate(row.registeredAt) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120">
            <template #default="{ row }">
              <el-button
                type="primary"
                size="small"
                @click="handleCheckin(row)"
              >
                签到
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-empty v-if="!checkinStatus.notCheckedInList || checkinStatus.notCheckedInList.length === 0" description="所有人员均已签到" />
      </el-tab-pane>
      <el-tab-pane label="已签到人员" name="checkedIn">
        <el-table :data="checkinStatus.checkedInList || []" border stripe>
          <el-table-column prop="studentName" label="姓名" width="100" />
          <el-table-column prop="studentNo" label="学号" width="120" />
          <el-table-column prop="phone" label="手机号" width="120" />
          <el-table-column prop="registeredAt" label="签到时间" width="160">
            <template #default="{ row }">
              {{ formatDate(row.registeredAt) }}
            </template>
          </el-table-column>
          <el-table-column label="签到状态" width="100">
            <template #default="{ row }">
              <el-tag type="success">已签到</el-tag>
            </template>
          </el-table-column>
        </el-table>
        <el-empty v-if="!checkinStatus.checkedInList || checkinStatus.checkedInList.length === 0" description="暂无签到人员" />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowLeft } from '@element-plus/icons-vue'
import dayjs from 'dayjs'
import { getCheckinStatus, checkin } from '@/api/checkin'

const route = useRoute()
const router = useRouter()
const activityId = route.params.activityId

const activeTab = ref('notCheckedIn')
const checkinStatus = reactive({
  totalRegistered: 0,
  checkedInCount: 0,
  notCheckedInCount: 0,
  checkedInList: [],
  notCheckedInList: []
})

const loadData = async () => {
  try {
    const data = await getCheckinStatus(activityId)
    Object.assign(checkinStatus, data)
  } catch (error) {
    console.error(error)
  }
}

const handleCheckin = async (row) => {
  ElMessageBox.confirm(`确定要给「${row.studentName}」进行签到吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'info'
  }).then(async () => {
    try {
      await checkin(activityId, row.userId, '')
      ElMessage.success('签到成功')
      loadData()
    } catch (error) {
      console.error(error)
    }
  }).catch(() => {})
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

.stats-row {
  margin-bottom: 20px;
}

.stat-item {
  text-align: center;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #303133;
}

.stat-item.success .stat-value {
  color: #67c23a;
}

.stat-item.warning .stat-value {
  color: #e6a23c;
}
</style>
