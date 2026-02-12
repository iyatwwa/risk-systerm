import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录' },
  },
  {
    path: '/',
    component: () => import('@/layout/Layout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '首页', icon: 'Odometer' },
      },
      // 项目管理
      {
        path: 'project',
        name: 'Project',
        meta: { title: '项目管理', icon: 'FolderOpened' },
        children: [
          {
            path: 'list',
            name: 'ProjectList',
            component: () => import('@/views/project/List.vue'),
            meta: { title: '项目列表' },
          },
          {
            path: 'create',
            name: 'ProjectCreate',
            component: () => import('@/views/project/Create.vue'),
            meta: { title: '新建项目' },
          },
          {
            path: 'edit/:id',
            name: 'ProjectEdit',
            component: () => import('@/views/project/Edit.vue'),
            meta: { title: '编辑项目' },
          },
          {
            path: 'my',
            name: 'MyProject',
            component: () => import('@/views/project/My.vue'),
            meta: { title: '我的项目' },
          },
        ],
      },
      // 风险评估
      {
        path: 'risk',
        name: 'Risk',
        meta: { title: '风险评估', icon: 'DocumentChecked' },
        children: [
          {
            path: 'assessment',
            name: 'RiskAssessment',
            component: () => import('@/views/risk/Assessment.vue'),
            meta: { title: '在线评估' },
          },
          {
            path: 'report',
            name: 'RiskReport',
            component: () => import('@/views/risk/Report.vue'),
            meta: { title: '评估报告' },
          },
          {
            path: 'history',
            name: 'RiskHistory',
            component: () => import('@/views/risk/History.vue'),
            meta: { title: '评估历史' },
          },
        ],
      },
      // 审批流程
      {
        path: 'approval',
        name: 'Approval',
        meta: { title: '审批流程', icon: 'Checked' },
        children: [
          {
            path: 'pending',
            name: 'ApprovalPending',
            component: () => import('@/views/approval/Pending.vue'),
            meta: { title: '待我审批' },
          },
          {
            path: 'my',
            name: 'MyApproval',
            component: () => import('@/views/approval/My.vue'),
            meta: { title: '我的申请' },
          },
          {
            path: 'history',
            name: 'ApprovalHistory',
            component: () => import('@/views/approval/History.vue'),
            meta: { title: '审批记录' },
          },
        ],
      },
      // 数据分析
      {
        path: 'analytics',
        name: 'Analytics',
        meta: { title: '数据分析', icon: 'TrendCharts' },
        children: [
          {
            path: 'statistics',
            name: 'Statistics',
            component: () => import('@/views/analytics/Statistics.vue'),
            meta: { title: '风险统计' },
          },
          {
            path: 'trend',
            name: 'Trend',
            component: () => import('@/views/analytics/Trend.vue'),
            meta: { title: '趋势分析' },
          },
          {
            path: 'export',
            name: 'Export',
            component: () => import('@/views/analytics/Export.vue'),
            meta: { title: '报表导出' },
          },
        ],
      },
      // 系统管理
      {
        path: 'system',
        name: 'System',
        meta: { title: '系统管理', icon: 'Setting' },
        children: [
          {
            path: 'user',
            name: 'UserManage',
            component: () => import('@/views/system/User.vue'),
            meta: { title: '用户管理' },
          },
          {
            path: 'role',
            name: 'RoleManage',
            component: () => import('@/views/system/Role.vue'),
            meta: { title: '角色权限' },
          },
          {
            path: 'rule',
            name: 'RuleConfig',
            component: () => import('@/views/system/Rule.vue'),
            meta: { title: '规则配置' },
          },
          {
            path: 'log',
            name: 'SystemLog',
            component: () => import('@/views/system/Log.vue'),
            meta: { title: '系统日志' },
          },
        ],
      },
      // 个人中心
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('@/views/Profile.vue'),
        meta: { title: '个人中心' },
      },
    ],
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/404.vue'),
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
