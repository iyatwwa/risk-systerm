<template>
  <div class="login-container">
    <!-- 左侧介绍区 -->
    <div class="login-left">
      <div class="logo-section">
        <div class="logo">
          <el-icon :size="80"><School /></el-icon>
        </div>
        <h1>实验风险评估系统</h1>
        <p class="subtitle">Laboratory Risk Assessment System</p>
      </div>

      <div class="features">
        <div class="feature-item">
          <el-icon><Check /></el-icon>
          <span>智能风险评估</span>
        </div>
        <div class="feature-item">
          <el-icon><Check /></el-icon>
          <span>自动化审批流程</span>
        </div>
        <div class="feature-item">
          <el-icon><Check /></el-icon>
          <span>全程追踪管理</span>
        </div>
        <div class="feature-item">
          <el-icon><Check /></el-icon>
          <span>数据可视化分析</span>
        </div>
      </div>
    </div>

    <!-- 右侧登录表单 -->
    <div class="login-right">
      <el-card class="login-card">
        <template #header>
          <h2>用户登录</h2>
        </template>

        <el-form :model="loginForm" :rules="rules" ref="loginFormRef" size="large">
          <el-form-item prop="username">
            <el-input v-model="loginForm.username" placeholder="请输入用户名" prefix-icon="User" />
          </el-form-item>

          <el-form-item prop="password">
            <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="请输入密码"
              prefix-icon="Lock"
              show-password
              @keyup.enter="handleLogin"
            />
          </el-form-item>

          <el-form-item>
            <div class="login-options">
              <el-checkbox v-model="loginForm.remember">记住我</el-checkbox>
              <el-link type="primary">忘记密码?</el-link>
            </div>
          </el-form-item>

          <el-form-item>
            <el-button
              type="primary"
              size="large"
              style="width: 100%"
              :loading="loading"
              @click="handleLogin"
            >
              登 录
            </el-button>
          </el-form-item>

          <div class="register-link">还没有账号? <el-link type="primary">立即注册</el-link></div>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { School, Check } from '@element-plus/icons-vue'

const router = useRouter()
const loginFormRef = ref()
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: '',
  remember: false,
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' },
  ],
}

const handleLogin = async () => {
  await loginFormRef.value.validate()

  loading.value = true

  // 模拟登录
  setTimeout(() => {
    loading.value = false
    ElMessage.success('登录成功')
    router.push('/dashboard')
  }, 1000)
}
</script>

<style scoped lang="scss">
.login-container {
  display: flex;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);

  .login-left {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    color: white;
    padding: 60px;

    .logo-section {
      text-align: center;
      margin-bottom: 60px;

      .logo {
        margin-bottom: 20px;
      }

      h1 {
        font-size: 36px;
        margin: 20px 0 10px;
      }

      .subtitle {
        font-size: 16px;
        opacity: 0.9;
      }
    }

    .features {
      .feature-item {
        display: flex;
        align-items: center;
        gap: 12px;
        font-size: 18px;
        margin: 20px 0;

        .el-icon {
          font-size: 24px;
        }
      }
    }
  }

  .login-right {
    width: 500px;
    display: flex;
    align-items: center;
    justify-content: center;
    background: white;
    padding: 40px;

    .login-card {
      width: 100%;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

      h2 {
        text-align: center;
        margin: 0;
        color: #303133;
      }

      .login-options {
        display: flex;
        justify-content: space-between;
        width: 100%;
      }

      .register-link {
        text-align: center;
        color: #909399;
      }
    }
  }
}
</style>
