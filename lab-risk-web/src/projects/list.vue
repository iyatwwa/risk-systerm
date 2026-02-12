<template>
  <div class="project-list-page">
    <el-card>
      <template #header>
        <div class="header-actions">
          <h2>
            <el-icon><FolderOpened /></el-icon> 项目管理
          </h2>
          <el-button type="primary" @click="handleCreate">
            <el-icon><Plus /></el-icon>
            新建项目
          </el-button>
        </div>
      </template>

      <el-form :model="searchForm" inline class="search-form">
        <el-form-item label="项目名称">
          <el-input v-model="searchForm.projectName" placeholder="请输入项目名称" clearable />
        </el-form-item>

        <el-form-item label="实验类型">
          <el-select v-model="searchForm.experimentType" placeholder="全部" clearable>
            <el-option label="化学实验" value="CHEMISTRY" />
            <el-option label="生物实验" value="BIOLOGY" />
            <el-option label="物理实验" value="PHYSICS" />
            <el-option label="辐射实验" value="RADIATION" />
            <el-option label="激光实验" value="LASER" />
          </el-select>
        </el-form-item>

        <el-form-item label="风险等级">
          <el-select v-model="searchForm.riskLevel" placeholder="全部" clearable>
            <el-option label="低风险" value="低风险" />
            <el-option label="中风险" value="中风险" />
            <el-option label="高风险" value="高风险" />
            <el-option label="极高风险" value="极高风险" />
          </el-select>
        </el-form-item>

        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部" clearable>
            <el-option label="草稿" value="DRAFT" />
            <el-option label="评估中" value="ASSESSING" />
            <el-option label="审批中" value="APPROVING" />
            <el-option label="已通过" value="APPROVED" />
            <el-option label="已拒绝" value="REJECTED" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>

      <el-table
        :data="tableData"
        border
        stripe
        v-loading="loading"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="projectCode" label="项目编号" width="150" />
        <el-table-column prop="projectName" label="项目名称" min-width="200" />

        <el-table-column prop="experimentType" label="实验类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getTypeTagType(row.experimentType)">
              {{ formatExperimentType(row.experimentType) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="applicantName" label="申请人" width="100" />
        <el-table-column prop="participantCount" label="参与人数" width="100" align="center" />

        <el-table-column prop="riskLevel" label="风险等级" width="120">
          <template #default="{ row }">
            <el-tag v-if="row.riskLevel" :type="getRiskLevelType(row.riskLevel)">
              {{ row.riskLevel }}
            </el-tag>
            <span v-else class="text-muted">未评估</span>
          </template>
        </el-table-column>

        <el-table-column prop="riskScore" label="风险分数" width="100" align="center">
          <template #default="{ row }">
            <span v-if="row.riskScore" :style="{ color: getScoreColor(row.riskScore) }">
              {{ row.riskScore }}
            </span>
            <span v-else class="text-muted">-</span>
          </template>
        </el-table-column>

        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ formatStatus(row.status) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="createTime" label="创建时间" width="180" />

        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleView(row)">
              <el-icon><View /></el-icon>
              查看
            </el-button>
            <el-button link type="warning" size="small" @click="handleAssess(row)">
              <el-icon><DocumentChecked /></el-icon>
              评估
            </el-button>
            <el-button link type="success" size="small" @click="handleEdit(row)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button link type="danger" size="small" @click="handleDelete(row)">
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="batch-actions" v-if="selectedRows.length > 0">
        <el-alert
          :title="`已选择 ${selectedRows.length} 项`"
          type="info"
          show-icon
          :closable="false"
        >
          <template #default>
            <el-button size="small" @click="handleBatchAssess">批量评估</el-button>
            <el-button size="small" type="danger" @click="handleBatchDelete">批量删除</el-button>
          </template>
        </el-alert>
      </div>

      <el-pagination
        v-model:current-page="pagination.page"
        v-model:page-size="pagination.size"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
        class="pagination"
      />
    </el-card>

    <el-dialog v-model="dialogVisible" title="项目详情" width="800px">
      <ProjectDetail v-if="currentProject" :project="currentProject" />
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  FolderOpened,
  Plus,
  Search,
  Refresh,
  View,
  Edit,
  Delete,
  DocumentChecked,
} from '@element-plus/icons-vue'

import ProjectDetail from './components/ProjectDetail.vue'

const router = useRouter()
const loading = ref(false)
const dialogVisible = ref(false)
const currentProject = ref<any>(null)
// 修改点 1: 为 selectedRows 指定明确的类型，避免推断为 never[]
const selectedRows = ref<any[]>([])

// 搜索表单
const searchForm = reactive({
  projectName: '',
  experimentType: '',
  riskLevel: '',
  status: '',
})

// 分页
const pagination = reactive({
  page: 1,
  size: 10,
  total: 0,
})

// 表格数据
const tableData = ref([
  {
    id: 1,
    projectCode: 'EXP-2026-001',
    projectName: '浓硫酸氧化实验',
    experimentType: 'CHEMISTRY',
    applicantName: '张三',
    participantCount: 15,
    riskLevel: '极高风险',
    riskScore: 120,
    status: 'APPROVED',
    createTime: '2026-02-10 10:30:00',
  },
])

// 加载数据
const loadData = async () => {
  loading.value = true
  setTimeout(() => {
    pagination.total = tableData.value.length
    loading.value = false
  }, 500)
}

const handleSearch = () => {
  pagination.page = 1
  loadData()
}

const handleReset = () => {
  Object.assign(searchForm, {
    projectName: '',
    experimentType: '',
    riskLevel: '',
    status: '',
  })
  handleSearch()
}

const handleCreate = () => router.push('/project/create')

const handleView = (row: any) => {
  currentProject.value = row
  dialogVisible.value = true
}

const handleAssess = (row: any) => {
  router.push(`/risk/assessment?projectId=${row.id}`)
}

const handleEdit = (row: any) => {
  router.push(`/project/edit/${row.id}`)
}

const handleDelete = (row: any) => {
  ElMessageBox.confirm(`确定要删除项目 "${row.projectName}" 吗?`, '删除确认', {
    type: 'warning',
  }).then(() => {
    ElMessage.success('删除成功')
    loadData()
  })
}

const handleSelectionChange = (selection: any[]) => {
  selectedRows.value = selection
}

const handleBatchAssess = () => ElMessage.info('批量评估功能开发中...')

const handleBatchDelete = () => {
  ElMessageBox.confirm(`确定要删除选中的 ${selectedRows.value.length} 个项目吗?`, '提示', {
    type: 'warning',
  }).then(() => {
    ElMessage.success('批量删除成功')
    selectedRows.value = []
    loadData()
  })
}

const handlePageChange = (page: number) => {
  pagination.page = page
  loadData()
}

const handleSizeChange = (size: number) => {
  pagination.size = size
  loadData()
}

const formatExperimentType = (type: string) => {
  const map: Record<string, string> = {
    CHEMISTRY: '化学',
    BIOLOGY: '生物',
    PHYSICS: '物理',
    RADIATION: '辐射',
    LASER: '激光',
  }
  return map[type] || type
}

const formatStatus = (status: string) => {
  const map: Record<string, string> = {
    DRAFT: '草稿',
    ASSESSING: '评估中',
    APPROVING: '审批中',
    APPROVED: '已通过',
    REJECTED: '已拒绝',
  }
  return map[status] || status
}

const getTypeTagType = (type: string) => {
  const map: Record<string, string> = {
    CHEMISTRY: 'danger',
    BIOLOGY: 'warning',
    PHYSICS: 'primary',
    RADIATION: 'danger',
    LASER: 'warning',
  }
  return map[type] || ''
}

const getRiskLevelType = (level: string) => {
  const map: Record<string, string> = {
    低风险: 'success',
    中风险: 'warning',
    高风险: 'danger',
    极高风险: 'danger',
  }
  return map[level] || 'info'
}

const getStatusType = (status: string) => {
  const map: Record<string, string> = {
    DRAFT: 'info',
    ASSESSING: 'warning',
    APPROVING: 'primary',
    APPROVED: 'success',
    REJECTED: 'danger',
  }
  return map[status] || 'info'
}

const getScoreColor = (score: number) => {
  if (score < 40) return '#67C23A'
  if (score < 70) return '#E6A23C'
  if (score < 90) return '#F56C6C'
  return '#909399'
}

onMounted(() => {
  loadData()
})
</script>

<style scoped lang="scss">
.project-list-page {
  padding: 20px;

  .header-actions {
    display: flex;
    justify-content: space-between;
    align-items: center;

    h2 {
      margin: 0;
      display: flex;
      align-items: center;
      gap: 8px;
    }
  }

  .search-form {
    margin-bottom: 20px;
  }

  .batch-actions {
    margin: 20px 0;
  }

  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
} // 修复点 3: 这里的花括号之前未正确闭合
</style>
