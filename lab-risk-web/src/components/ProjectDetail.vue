<template>
  <div class="project-detail">
    <el-descriptions :column="2" border>
      <el-descriptions-item label="项目编号">
        {{ project.projectCode }}
      </el-descriptions-item>
      <el-descriptions-item label="项目名称">
        {{ project.projectName }}
      </el-descriptions-item>
      <el-descriptions-item label="实验类型">
        <el-tag>{{ formatExperimentType(project.experimentType) }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="申请人">
        {{ project.applicantName }}
      </el-descriptions-item>
      <el-descriptions-item label="参与人数">
        {{ project.participantCount }} 人
      </el-descriptions-item>
      <el-descriptions-item label="实验时长">
        {{ project.duration || '-' }} 小时
      </el-descriptions-item>
      <el-descriptions-item label="风险等级">
        <el-tag v-if="project.riskLevel" :type="getRiskLevelType(project.riskLevel)">
          {{ project.riskLevel }}
        </el-tag>
        <span v-else>未评估</span>
      </el-descriptions-item>
      <el-descriptions-item label="风险分数">
        <span
          v-if="project.riskScore"
          :style="{ color: getScoreColor(project.riskScore), fontWeight: 'bold' }"
        >
          {{ project.riskScore }} 分
        </span>
        <span v-else>-</span>
      </el-descriptions-item>
      <el-descriptions-item label="状态">
        <el-tag :type="getStatusType(project.status)">
          {{ formatStatus(project.status) }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="创建时间">
        {{ project.createTime }}
      </el-descriptions-item>
      <el-descriptions-item label="项目描述" :span="2">
        {{ project.description || '暂无描述' }}
      </el-descriptions-item>
    </el-descriptions>

    <!-- 风险因素(如果已评估) -->
    <div v-if="project.riskLevel" class="risk-info">
      <el-divider content-position="left">
        <h3>
          <el-icon><WarnTriangleFilled /></el-icon> 风险评估信息
        </h3>
      </el-divider>

      <el-alert
        :type="getRiskAlertType(project.riskLevel)"
        :title="`该项目风险等级为: ${project.riskLevel}`"
        :description="`风险分数: ${project.riskScore} 分`"
        show-icon
        :closable="false"
      />
    </div>

    <!-- 操作按钮 -->
    <div class="actions">
      <el-button type="primary" @click="handleViewReport">
        <el-icon><Document /></el-icon>
        查看评估报告
      </el-button>
      <el-button @click="handleExport">
        <el-icon><Download /></el-icon>
        导出详情
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { WarnTriangleFilled, Document, Download } from '@element-plus/icons-vue'

defineProps<{
  project: any
}>()

const formatExperimentType = (type: string) => {
  const map: Record<string, string> = {
    CHEMISTRY: '化学实验',
    BIOLOGY: '生物实验',
    PHYSICS: '物理实验',
    RADIATION: '辐射实验',
    LASER: '激光实验',
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

const getRiskLevelType = (level: string) => {
  const map: Record<string, string> = {
    低风险: 'success',
    中风险: 'warning',
    高风险: 'danger',
    极高风险: 'danger',
  }
  return map[level] || 'info'
}

const getRiskAlertType = (level: string) => {
  const map: Record<string, string> = {
    低风险: 'success',
    中风险: 'warning',
    高风险: 'error',
    极高风险: 'error',
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

const handleViewReport = () => {
  ElMessage.info('查看评估报告功能开发中...')
}

const handleExport = () => {
  ElMessage.success('导出成功')
}
</script>

<style scoped lang="scss">
.project-detail {
  .risk-info {
    margin-top: 30px;

    h3 {
      margin: 0;
      display: flex;
      align-items: center;
      gap: 8px;
    }

    .el-alert {
      margin-top: 15px;
    }
  }

  .actions {
    margin-top: 30px;
    display: flex;
    gap: 10px;
    justify-content: center;
  }
}
</style>
