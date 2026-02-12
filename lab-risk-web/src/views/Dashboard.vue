<template>
  <div class="dashboard">
    <!-- 数据概览卡片 -->
    <el-row :gutter="20" class="stats-cards">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background: #409eff">
              <el-icon :size="30"><Document /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.totalProjects }}</div>
              <div class="stat-label">总项目数</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background: #e6a23c">
              <el-icon :size="30"><Warning /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.pendingAssessment }}</div>
              <div class="stat-label">待评估</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background: #f56c6c">
              <el-icon :size="30"><WarnTriangleFilled /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.highRisk }}</div>
              <div class="stat-label">高风险项目</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-icon" style="background: #67c23a">
              <el-icon :size="30"><Select /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.monthlyAssessment }}</div>
              <div class="stat-label">本月评估</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="charts-row">
      <el-col :span="16">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>风险趋势分析</span>
              <el-radio-group v-model="trendPeriod" size="small">
                <el-radio-button label="week">近7天</el-radio-button>
                <el-radio-button label="month">近30天</el-radio-button>
                <el-radio-button label="year">近一年</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div id="trendChart" style="height: 350px"></div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card>
          <template #header>
            <span>风险等级分布</span>
          </template>
          <div id="pieChart" style="height: 350px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 待办事项和动态 -->
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>待办事项</span>
              <el-link type="primary">查看全部</el-link>
            </div>
          </template>
          <el-timeline>
            <el-timeline-item
              v-for="item in todoList"
              :key="item.id"
              :timestamp="item.time"
              :type="item.type"
            >
              {{ item.content }}
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>最近项目动态</span>
              <el-link type="primary">查看全部</el-link>
            </div>
          </template>
          <el-timeline>
            <el-timeline-item v-for="item in activities" :key="item.id" :timestamp="item.time">
              <el-tag :type="item.tagType" size="small">{{ item.tag }}</el-tag>
              {{ item.content }}
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import { Document, Warning, WarnTriangleFilled, Select } from '@element-plus/icons-vue'

// 统计数据
const stats = ref({
  totalProjects: 1234,
  pendingAssessment: 56,
  highRisk: 23,
  monthlyAssessment: 89,
})

const trendPeriod = ref('week')

// 待办事项
const todoList = ref([
  { id: 1, content: '审批高风险化学实验项目', time: '2026-02-12 10:30', type: 'danger' },
  { id: 2, content: '查看生物实验评估报告', time: '2026-02-12 09:15', type: 'warning' },
  { id: 3, content: '完成本月实验统计', time: '2026-02-11 16:00', type: 'primary' },
])

// 项目动态
const activities = ref([
  {
    id: 1,
    tag: '提交',
    tagType: 'primary',
    content: '张三提交了"浓硫酸氧化实验"评估',
    time: '2026-02-12 11:00',
  },
  {
    id: 2,
    tag: '通过',
    tagType: 'success',
    content: '"物理光学实验"审批通过',
    time: '2026-02-12 10:30',
  },
  {
    id: 3,
    tag: '警告',
    tagType: 'danger',
    content: '"生物培养实验"风险等级为高风险',
    time: '2026-02-12 09:45',
  },
])

// 初始化图表
onMounted(() => {
  initTrendChart()
  initPieChart()
})

// 趋势图
const initTrendChart = () => {
  const chart = echarts.init(document.getElementById('trendChart'))
  chart.setOption({
    tooltip: { trigger: 'axis' },
    legend: { data: ['低风险', '中风险', '高风险', '极高风险'] },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: {
      type: 'category',
      data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
    },
    yAxis: { type: 'value' },
    series: [
      {
        name: '低风险',
        type: 'line',
        data: [12, 15, 10, 14, 18, 20, 16],
        smooth: true,
        itemStyle: { color: '#67C23A' },
      },
      {
        name: '中风险',
        type: 'line',
        data: [8, 10, 12, 9, 11, 13, 10],
        smooth: true,
        itemStyle: { color: '#E6A23C' },
      },
      {
        name: '高风险',
        type: 'line',
        data: [5, 6, 4, 7, 5, 8, 6],
        smooth: true,
        itemStyle: { color: '#F56C6C' },
      },
      {
        name: '极高风险',
        type: 'line',
        data: [2, 3, 2, 4, 3, 5, 4],
        smooth: true,
        itemStyle: { color: '#909399' },
      },
    ],
  })
}

// 饼图
const initPieChart = () => {
  const chart = echarts.init(document.getElementById('pieChart'))
  chart.setOption({
    tooltip: { trigger: 'item' },
    legend: { orient: 'vertical', left: 'left' },
    series: [
      {
        name: '风险等级',
        type: 'pie',
        radius: '70%',
        data: [
          { value: 65, name: '低风险', itemStyle: { color: '#67C23A' } },
          { value: 48, name: '中风险', itemStyle: { color: '#E6A23C' } },
          { value: 28, name: '高风险', itemStyle: { color: '#F56C6C' } },
          { value: 15, name: '极高风险', itemStyle: { color: '#909399' } },
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)',
          },
        },
      },
    ],
  })
}
</script>

<style scoped lang="scss">
.dashboard {
  padding: 20px;

  .stats-cards {
    margin-bottom: 20px;

    .stat-card {
      display: flex;
      align-items: center;
      gap: 20px;

      .stat-icon {
        width: 60px;
        height: 60px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: white;
      }

      .stat-info {
        flex: 1;

        .stat-value {
          font-size: 28px;
          font-weight: bold;
          color: #303133;
        }

        .stat-label {
          font-size: 14px;
          color: #909399;
          margin-top: 5px;
        }
      }
    }
  }

  .charts-row {
    margin-bottom: 20px;
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}
</style>
