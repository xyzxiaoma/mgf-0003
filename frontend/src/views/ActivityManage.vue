<template>
  <div class="activity-manage">
    <div class="page-header">
      <h2>活动管理</h2>
      <el-button type="primary" @click="showAddDialog">
        <el-icon><Plus /></el-icon>
        新增活动
      </el-button>
    </div>
    <el-table :data="activities" border stripe>
      <el-table-column prop="name" label="活动名称" min-width="150" />
      <el-table-column prop="clubName" label="所属社团" width="120" />
      <el-table-column prop="activityDate" label="活动日期" width="120" />
      <el-table-column prop="location" label="活动地点" min-width="120" />
      <el-table-column label="报名情况" width="120">
        <template #default="{ row }">
          {{ row.registeredCount }}/{{ row.maxCapacity }}
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">
            {{ getStatusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="创建时间" width="160">
        <template #default="{ row }">
          {{ formatDate(row.createdAt) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="320" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="viewRegistrations(row)">报名人员</el-button>
          <el-button size="small" @click="viewCheckins(row)">签到管理</el-button>
          <el-button size="small" @click="showEditDialog(row)">编辑</el-button>
          <el-button
            size="small"
            type="danger"
            @click="handleDelete(row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-empty v-if="activities.length === 0" description="暂无活动" />

    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑活动' : '新增活动'"
      width="500px"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="活动名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="所属社团" prop="clubName">
          <el-input v-model="form.clubName" />
        </el-form-item>
        <el-form-item label="活动日期" prop="activityDate">
          <el-date-picker
            v-model="form.activityDate"
            type="date"
            style="width: 100%"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="活动地点" prop="location">
          <el-input v-model="form.location" />
        </el-form-item>
        <el-form-item label="人数上限" prop="maxCapacity">
          <el-input-number v-model="form.maxCapacity" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="活动状态" prop="status" v-if="isEdit">
          <el-select v-model="form.status" style="width: 100%">
            <el-option label="未开始" value="NOT_STARTED" />
            <el-option label="进行中" value="IN_PROGRESS" />
            <el-option label="已结束" value="ENDED" />
          </el-select>
        </el-form-item>
        <el-form-item label="活动说明" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import dayjs from 'dayjs'
import {
  getActivities,
  createActivity,
  updateActivity,
  deleteActivity
} from '@/api/activity'

const router = useRouter()
const activities = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref()
const editId = ref(null)

const form = reactive({
  name: '',
  clubName: '',
  activityDate: '',
  location: '',
  maxCapacity: 50,
  status: 'NOT_STARTED',
  description: ''
})

const rules = {
  name: [{ required: true, message: '请输入活动名称', trigger: 'blur' }],
  clubName: [{ required: true, message: '请输入所属社团', trigger: 'blur' }],
  activityDate: [{ required: true, message: '请选择活动日期', trigger: 'change' }],
  location: [{ required: true, message: '请输入活动地点', trigger: 'blur' }],
  maxCapacity: [{ required: true, message: '请输入人数上限', trigger: 'blur' }]
}

const loadActivities = async () => {
  try {
    activities.value = await getActivities()
  } catch (error) {
    console.error(error)
  }
}

const showAddDialog = () => {
  isEdit.value = false
  editId.value = null
  Object.assign(form, {
    name: '',
    clubName: '',
    activityDate: '',
    location: '',
    maxCapacity: 50,
    status: 'NOT_STARTED',
    description: ''
  })
  dialogVisible.value = true
}

const showEditDialog = (row) => {
  isEdit.value = true
  editId.value = row.id
  Object.assign(form, {
    name: row.name,
    clubName: row.clubName,
    activityDate: row.activityDate,
    location: row.location,
    maxCapacity: row.maxCapacity,
    status: row.status,
    description: row.description || ''
  })
  dialogVisible.value = true
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    if (isEdit.value) {
      await updateActivity(editId.value, form)
      ElMessage.success('修改成功')
    } else {
      await createActivity(form)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    loadActivities()
  } catch (error) {
    console.error(error)
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除「${row.name}」吗？删除后不可恢复！`, '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'danger'
  }).then(async () => {
    try {
      await deleteActivity(row.id)
      ElMessage.success('删除成功')
      loadActivities()
    } catch (error) {
      console.error(error)
    }
  }).catch(() => {})
}

const viewRegistrations = (row) => {
  router.push(`/admin/registrations/${row.id}`)
}

const viewCheckins = (row) => {
  router.push(`/admin/checkins/${row.id}`)
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
</style>
