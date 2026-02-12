<script setup lang="ts">
import { ref, reactive } from 'vue'
import { DocumentChecked } from '@element-plus/icons-vue'
import type { FormInstance, FormRules } from 'element-plus'

// --- 类型定义 ---
interface AssessmentData {
  score: number
  level: string
}

// --- 状态定义 ---
const currentStep = ref(0)
const loading = ref(false)
const formRef = ref<FormInstance>()

// 表单数据
const form = reactive({
  projectName: '',
  experimentType: '',
})

// 校验规则
const rules = reactive<FormRules>({
  projectName: [{ required: true, message: '请输入项目名称', trigger: 'blur' }],
  experimentType: [{ required: true, message: '请选择实验类型', trigger: 'change' }],
})

// 修改点：使用泛型定义 result 既可以是 null 也可以是 AssessmentData 类型
const result = ref<AssessmentData | null>(null)

// --- 方法定义 ---

// 下一步
const nextStep = async () => {
  if (!formRef.value) return
  await formRef.value.validate((valid) => {
    if (valid) {
      currentStep.value++
    }
  })
}

// 上一步
const prevStep = () => {
  if (currentStep.value > 0) {
    currentStep.value--
  }
}

// 提交评估
const submitAssessment = () => {
  loading.value = true
  // 模拟 API 请求
  setTimeout(() => {
    loading.value = false
    // 现在这里的赋值不会报错了
    result.value = {
      score: 85,
      level: '低风险',
    }
    currentStep.value = 2
  }, 1500)
}

// 重置
const resetForm = () => {
  currentStep.value = 0
  form.projectName = ''
  form.experimentType = ''
  result.value = null
}
</script>

<template>
  <div class="risk-assessment-page">
    <el-card>
      <template #header>
        <div class="header">
          <h2>
            <el-icon><DocumentChecked /></el-icon>
            实验项目风险评估
          </h2>
          <el-steps :active="currentStep" align-center style="max-width: 800px">
            <el-step title="基本信息" />
            <el-step title="风险因素" />
            <el-step title="评估结果" />
          </el-steps>
        </div>
      </template>

      <div v-show="currentStep === 0">
        <el-form :model="form" :rules="rules" ref="formRef" label-width="150px">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="项目名称" prop="projectName">
                <el-input v-model="form.projectName" placeholder="请输入项目名称" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="实验类型" prop="experimentType">
                <el-select v-model="form.experimentType" placeholder="请选择">
                  <el-option label="化学实验" value="CHEMISTRY" />
                  <el-option label="生物实验" value="BIOLOGY" />
                  <el-option label="物理实验" value="PHYSICS" />
                  <el-option label="辐射实验" value="RADIATION" />
                  <el-option label="激光实验" value="LASER" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>

        <div class="step-buttons" style="margin-top: 20px; text-align: right">
          <el-button type="primary" @click="nextStep">下一步</el-button>
        </div>
      </div>

      <div v-show="currentStep === 1">
        <div style="padding: 40px; text-align: center; color: #909399">
          已进入风险因素识别阶段...
        </div>
        <div class="step-buttons" style="margin-top: 20px; text-align: right">
          <el-button @click="prevStep">上一步</el-button>
          <el-button type="primary" @click="submitAssessment" :loading="loading">
            开始评估
          </el-button>
        </div>
      </div>

      <div v-show="currentStep === 2">
        <AssessmentResult v-if="result" :result="result" @reset="resetForm" />
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.header {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
}
.header h2 {
  margin: 0;
  display: flex;
  align-items: center;
  gap: 8px;
}
.step-buttons {
  border-top: 1px solid #ebeef5;
  padding-top: 20px;
}
</style>
