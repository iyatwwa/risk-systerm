<template>
  <el-container class="layout-container">
    <!-- 侧边栏 -->
    <el-aside :width="isCollapse ? '64px' : '200px'" class="layout-aside">
      <div class="logo">
        <el-icon v-if="isCollapse" :size="30"><School /></el-icon>
        <template v-else>
          <el-icon :size="30"><School /></el-icon>
          <span>风险评估系统</span>
        </template>
      </div>

      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :collapse-transition="false"
        router
        class="el-menu-vertical"
      >
        <el-menu-item index="/dashboard">
          <el-icon><Odometer /></el-icon>
          <template #title>首页</template>
        </el-menu-item>

        <el-sub-menu index="project">
          <template #title>
            <el-icon><FolderOpened /></el-icon>
            <span>项目管理</span>
          </template>
          <el-menu-item index="/project/list">项目列表</el-menu-item>
          <el-menu-item index="/project/create">新建项目</el-menu-item>
          <el-menu-item index="/project/my">我的项目</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="risk">
          <template #title>
            <el-icon><DocumentChecked /></el-icon>
            <span>风险评估</span>
          </template>
          <el-menu-item index="/risk/assessment">在线评估</el-menu-item>
          <el-menu-item index="/risk/report">评估报告</el-menu-item>
          <el-menu-item index="/risk/history">评估历史</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="approval">
          <template #title>
            <el-icon><Checked /></el-icon>
            <span>审批流程</span>
          </template>
          <el-menu-item index="/approval/pending">待我审批</el-menu-item>
          <el-menu-item index="/approval/my">我的申请</el-menu-item>
          <el-menu-item index="/approval/history">审批记录</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="analytics">
          <template #title>
            <el-icon><TrendCharts /></el-icon>
            <span>数据分析</span>
          </template>
          <el-menu-item index="/analytics/statistics">风险统计</el-menu-item>
          <el-menu-item index="/analytics/trend">趋势分析</el-menu-item>
          <el-menu-item index="/analytics/export">报表导出</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="system">
          <template #title>
            <el-icon><Setting /></el-icon>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/system/user">用户管理</el-menu-item>
          <el-menu-item index="/system/role">角色权限</el-menu-item>
          <el-menu-item index="/system/rule">规则配置</el-menu-item>
          <el-menu-item index="/system/log">系统日志</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>

    <!-- 主内容区 -->
    <el-container>
      <!-- 顶部导航栏 -->
      <el-header class="layout-header">
        <div class="header-left">
          <el-icon class="collapse-btn" @click="toggleCollapse">
            <Expand v-if="isCollapse" />
            <Fold v-else />
          </el-icon>

          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/dashboard' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item v-for="item in breadcrumbs" :key="item.path">
              {{ item.title }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>

        <div class="header-right">
          <!-- 消息通知 -->
          <el-badge :value="12" class="notification">
            <el-icon :size="20"><Bell /></el-icon>
          </el-badge>

          <!-- 全屏 -->
          <el-icon :size="20" class="fullscreen" @click="toggleFullscreen">
            <FullScreen />
          </el-icon>

          <!-- 用户信息 -->
          <el-dropdown @command="handleCommand">
            <div class="user-info">
              <el-avatar
                :size="32"
                src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"
              />
              <span class="username">{{ userInfo.name }}</span>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon>
                  个人中心
                </el-dropdown-item>
                <el-dropdown-item command="settings">
                  <el-icon><Setting /></el-icon>
                  个人设置
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <el-icon><SwitchButton /></el-icon>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 主内容 -->
      <el-main class="layout-main">
        <router-view v-slot="{ Component }">
          <transition name="fade-transform" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  School,
  Odometer,
  FolderOpened,
  DocumentChecked,
  Checked,
  TrendCharts,
  Setting,
  Expand,
  Fold,
  Bell,
  FullScreen,
  User,
  SwitchButton,
} from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()

const isCollapse = ref(false)
const activeMenu = computed(() => route.path)

const userInfo = ref({
  name: '管理员',
  avatar: '',
})

const breadcrumbs = computed(() => {
  const matched = route.matched.filter((item) => item.meta && item.meta.title)
  return matched.map((item) => ({
    path: item.path,
    title: item.meta.title,
  }))
})

// 切换折叠
const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}

// 全屏切换
const toggleFullscreen = () => {
  if (!document.fullscreenElement) {
    document.documentElement.requestFullscreen()
  } else {
    document.exitFullscreen()
  }
}

// 下拉菜单命令
const handleCommand = (command: string) => {
  switch (command) {
    case 'profile':
      router.push('/profile')
      break
    case 'settings':
      router.push('/settings')
      break
    case 'logout':
      ElMessage.success('退出登录成功')
      router.push('/login')
      break
  }
}
</script>

<style scoped lang="scss">
.layout-container {
  height: 100vh;

  .layout-aside {
    background: #001529;
    transition: width 0.3s;

    .logo {
      height: 60px;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 10px;
      color: white;
      font-size: 18px;
      font-weight: bold;
      border-bottom: 1px solid rgba(255, 255, 255, 0.1);
    }

    .el-menu-vertical {
      border-right: none;
      background: #001529;

      :deep(.el-menu-item),
      :deep(.el-sub-menu__title) {
        color: rgba(255, 255, 255, 0.65);

        &:hover {
          background: rgba(255, 255, 255, 0.1);
          color: white;
        }
      }

      :deep(.el-menu-item.is-active) {
        background: #1890ff;
        color: white;
      }
    }
  }

  .layout-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: white;
    border-bottom: 1px solid #f0f0f0;
    padding: 0 20px;

    .header-left {
      display: flex;
      align-items: center;
      gap: 20px;

      .collapse-btn {
        font-size: 20px;
        cursor: pointer;
        transition: all 0.3s;

        &:hover {
          color: #409eff;
        }
      }
    }

    .header-right {
      display: flex;
      align-items: center;
      gap: 20px;

      .notification,
      .fullscreen {
        cursor: pointer;
        transition: all 0.3s;

        &:hover {
          color: #409eff;
        }
      }

      .user-info {
        display: flex;
        align-items: center;
        gap: 10px;
        cursor: pointer;

        .username {
          font-size: 14px;
        }
      }
    }
  }

  .layout-main {
    background: #f0f2f5;
    overflow-y: auto;
  }
}

// 路由过渡动画
.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all 0.3s;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
</style>
