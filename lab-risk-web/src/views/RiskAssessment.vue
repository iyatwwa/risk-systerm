<template>
  <div class="risk-assessment">
    <el-card>
      <template #header>
        <h2>实验项目风险评估</h2>
      </template>

      <el-form :model="form" :rules="rules" ref="formRef" label-width="150px">
        <el-form-item label="项目名称" prop="projectName">
          <el-input v-model="form.projectName" placeholder="请输入项目名称" />
        </el-form-item>

        <el-form-item label="实验类型" prop="experimentType">
          <el-select v-model="form.experimentType" placeholder="请选择实验类型">
            <el-option label="化学实验" value="CHEMISTRY" />
            <el-option label="生物实验" value="BIOLOGY" />
            <el-option label="物理实验" value="PHYSICS" />
            <el-option label="辐射实验" value="RADIATION" />
            <el-option label="激光实验" value="LASER" />
          </el-select>
        </el-form-item>

        <el-form-item label="危险等级" prop="dangerLevel">
          <el-select v-model="form.dangerLevel" placeholder="请选择危险等级">
            <el-option label="轻微危险" value="LEVEL_1" />
            <el-option label="一般危险" value="LEVEL_2" />
            <el-option label="较大危险" value="LEVEL_3" />
            <el-option label="重大危险" value="LEVEL_4" />
            <el-option label="特别重大危险" value="LEVEL_5" />
          </el-select>
        </el-form-item>

        <el-form-item label="实验地点" prop="location">
          <el-input v-model="form.location" />
        </el-form-item>

        <el-form-item label="参与人数" prop="participantCount">
          <el-input-number v-model="form.participantCount" :min="1" />
        </el-form-item>

        <el-form-item label="实验时长(小时)" prop="duration">
          <el-input-number v-model="form.duration" :min="1" />
        </el-form-item>

        <el-form-item label="涉及危险化学品">
          <el-switch v-model="form.hasDangerousChemicals" />
        </el-form-item>

        <el-form-item label="人员是否具备资质">
          <el-switch v-model="form.hasQualification" />
        </el-form-item>

        <el-form-item label="是否有应急预案">
          <el-switch v-model="form.hasEmergencyPlan" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitAssessment" :loading="loading">
            开始评估
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 评估结果 -->
      <el-card v-if="result" class="result-card">
        <h3>评估结果</h3>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="风险分数">
            <el-tag :type="getScoreType(result.riskScore)" size="large">
              {{ result.riskScore }} 分
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="风险等级">
            <el-tag :type="getLevelType(result.riskLevel)" size="large">
              {{ result.riskLevel }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="审批层级" :span="2">
            {{ result.requiredApprovalLevel }} 级审批
          </el-descriptions-item>
          <el-descriptions-item label="评估摘要" :span="2">
            {{ result.summary }}
          </el-descriptions-item>
        </el-descriptions>

        <h4 style="margin-top: 20px">风险因素</h4>
        <el-table :data="result.riskFactors" border>
          <el-table-column prop="factorName" label="风险项" />
          <el-table-column prop="score" label="分数" width="100" />
          <el-table-column prop="description" label="描述" />
        </el-table>

        <h4 style="margin-top: 20px">建议措施</h4>
        <el-alert
          v-for="(suggestion, index) in result.suggestions"
          :key="index"
          :title="suggestion"
          type="warning"
          :closable="false"
          style="margin-bottom: 10px"
        />
      </el-card>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const formRef = ref()
const loading = ref(false)
const result = ref(null)

const form = reactive({
  projectName: '',
  experimentType: '',
  dangerLevel: '',
  location: '',
  participantCount: 1,
  duration: 1,
  hasDangerousChemicals: false,
  hasQualification: true,
  hasEmergencyPlan: true,
})

const rules = {
  projectName: [{ required: true, message: '请输入项目名称', trigger: 'blur' }],
  experimentType: [{ required: true, message: '请选择实验类型', trigger: 'change' }],
  dangerLevel: [{ required: true, message: '请选择危险等级', trigger: 'change' }],
  location: [{ required: true, message: '请输入实验地点', trigger: 'blur' }],
}

const submitAssessment = async () => {
  await formRef.value.validate()
  loading.value = true

  try {
    const res = await request.post('/api/risk-assessment/assess', form)
    result.value = res
    ElMessage.success('评估完成')
  } catch (error) {
    ElMessage.error('评估失败')
  } finally {
    loading.value = false
  }
}

const getScoreType = (score: number) => {
  if (score < 40) return 'success'
  if (score < 70) return 'warning'
  if (score < 90) return 'danger'
  return 'danger'
}

const getLevelType = (level: string) => {
  const map = {
    低风险: 'success',
    中风险: 'warning',
    高风险: 'danger',
    极高风险: 'danger',
  }
  return map[level] || 'info'
}
</script>

<style scoped>
.risk-assessment {
  padding: 20px;
}
.result-card {
  margin-top: 30px;
}
</style>
