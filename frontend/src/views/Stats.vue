<template>
  <div class="stats">
    <div class="page-header">
      <h2>统计分析</h2>
      <el-date-picker
        v-model="selectedMonth"
        type="month"
        placeholder="选择月份"
        format="YYYY-MM"
        value-format="YYYY-MM"
        @change="loadStats"
      />
    </div>

    <el-row :gutter="20" class="stats-row" v-if="stats">
      <el-col :span="6">
        <el-card>
          <div class="stat-item primary">
            <div class="stat-icon"><el-icon><Tickets /></el-icon></div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.totalActivities }}</div>
              <div class="stat-label">活动总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="stat-item info">
            <div class="stat-icon"><el-icon><User /></el-icon></div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.totalRegistrations }}</div>
              <div class="stat-label">报名总人数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="stat-item success">
            <div class="stat-icon"><el-icon><CircleCheck /></el-icon></div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.totalCheckins }}</div>
              <div class="stat-label">签到总人数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="stat-item warning">
            <div class="stat-icon"><el-icon><Clock /></el-icon></div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.totalNotCheckins }}</div>
              <div class="stat-label">未签到人数</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="club-stats-card" v-if="stats">
      <h3>按社团分类统计</h3>
      <el-table :data="stats.clubStats || []" border stripe>
        <el-table-column prop="clubName" label="社团名称" min-width="150" />
        <el-table-column prop="activityCount" label="发布活动数" width="120" align="center" />
        <el-table-column prop="registrationCount" label="报名人数" width="120" align="center" />
        <el-table-column prop="checkinCount" label="签到人数" width="120" align="center" />
        <el-table-column label="签到率" width="150" align="center">
          <template #default="{ row }">
            <el-progress
              :percentage="row.registrationCount > 0 ? Math.round((row.checkinCount / row.registrationCount) * 100) : 0"
            />
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-if="!stats.clubStats || stats.clubStats.length === 0" description="暂无统计数据" />
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Tickets, User, CircleCheck, Clock } from '@element-plus/icons-vue'
import dayjs from 'dayjs'
import { getMonthlyStats } from '@/api/stats'

const selectedMonth = ref(dayjs().format('YYYY-MM'))
const stats = reactive({
  month: '',
  totalActivities: 0,
  totalRegistrations: 0,
  totalCheckins: 0,
  totalNotCheckins: 0,
  clubStats: []
})

const loadStats = async () => {
  if (!selectedMonth.value) return
  try {
    const data = await getMonthlyStats(selectedMonth.value)
    Object.assign(stats, data)
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  loadStats()
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
  display: flex;
  align-items: center;
  gap: 15px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: #fff;
}

.stat-item.primary .stat-icon {
  background: linear-gradient(135deg, #667eea, #764ba2);
}

.stat-item.info .stat-icon {
  background: linear-gradient(135deg, #4facfe, #00f2fe);
}

.stat-item.success .stat-icon {
  background: linear-gradient(135deg, #43e97b, #38f9d7);
}

.stat-item.warning .stat-icon {
  background: linear-gradient(135deg, #fa709a, #fee140);
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.club-stats-card h3 {
  margin: 0 0 15px 0;
}
</style>
