<template>
  <div class="my-registrations">
    <div class="page-header">
      <h2>我的报名</h2>
    </div>
    <el-table :data="registrations" border stripe>
      <el-table-column prop="activityName" label="活动名称" min-width="180" />
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
      <el-table-column label="操作" width="120">
        <template #default="{ row }">
          <el-button
            type="danger"
            size="small"
            @click="handleCancel(row)"
            :disabled="row.status !== 'REGISTERED'"
          >
            取消报名
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-empty v-if="registrations.length === 0" description="暂无报名记录" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import dayjs from 'dayjs'
import { getMyRegistrations, cancelRegistration } from '@/api/registration'

const registrations = ref([])

const loadRegistrations = async () => {
  try {
    registrations.value = await getMyRegistrations()
  } catch (error) {
    console.error(error)
  }
}

const handleCancel = async (row) => {
  ElMessageBox.confirm(`确定要取消「${row.activityName}」的报名吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await cancelRegistration(row.id)
      ElMessage.success('取消报名成功')
      loadRegistrations()
    } catch (error) {
      console.error(error)
    }
  }).catch(() => {})
}

const formatDate = (date) => {
  return dayjs(date).format('YYYY-MM-DD HH:mm')
}

onMounted(() => {
  loadRegistrations()
})
</script>

<style scoped>
.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
}
</style>
