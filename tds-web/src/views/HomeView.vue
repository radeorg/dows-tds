<template>
  <div class="home-container">
    <!-- 现代化导航栏 -->
    <nav class="modern-nav">
      <div class="nav-content">
        <div class="logo-wrapper">
          <div class="logo-icon">
            <i class="el-icon-document"></i>
          </div>
          <div class="logo-text">
            <div class="logo-main">TaskHub</div>
            <div class="logo-subtitle">协作协议管理系统</div>
          </div>
        </div>
        
        <div class="nav-items">
          <el-menu 
            :default-active="activeIndex" 
            mode="horizontal" 
            @select="handleSelect" 
            router
            background-color="transparent"
            text-color="#606266"
            active-text-color="#409eff"
            class="nav-menu"
          >
            <el-menu-item index="/" class="nav-item">
              <i class="el-icon-s-home nav-icon"></i>
              <span>首页</span>
            </el-menu-item>
            <el-menu-item index="/agreements" class="nav-item">
              <i class="el-icon-document nav-icon"></i>
              <span>协议管理</span>
            </el-menu-item>
            <el-menu-item index="/tasks" class="nav-item">
              <i class="el-icon-tickets nav-icon"></i>
              <span>任务管理</span>
            </el-menu-item>
            <el-menu-item index="/users" class="nav-item">
              <i class="el-icon-user nav-icon"></i>
              <span>用户管理</span>
            </el-menu-item>
          </el-menu>
        </div>
        
        <div class="user-actions">
          <el-dropdown>
            <span class="user-avatar">
              <i class="el-icon-user"></i>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item disabled>{{ userName }}</el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout">
                  <i class="el-icon-switch-button"></i> 退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </nav>

    <!-- 主内容区域 -->
    <main class="main-content">
      <!-- 英雄区域 -->
      <section class="hero-section">
        <div class="hero-content">
          <h1 class="hero-title">高效管理<span class="highlight">协作协议</span></h1>
          <p class="hero-subtitle">为兼职技术人员打造的现代化项目协作解决方案</p>
          <div class="hero-actions">
            <el-button type="primary" size="large" @click="createAgreement" class="primary-btn">
              <i class="el-icon-plus"></i> 创建新协议
            </el-button>
            <el-button size="large" @click="goToAgreements" class="secondary-btn">
              <i class="el-icon-document"></i> 浏览协议
            </el-button>
          </div>
        </div>
        <div class="hero-visual">
          <div class="visual-bg"></div>
          <div class="visual-elements">
            <div class="element-card card-1"><i class="el-icon-document"></i></div>
            <div class="element-card card-2"><i class="el-icon-tickets"></i></div>
            <div class="element-card card-3"><i class="el-icon-user"></i></div>
            <div class="element-card card-4"><i class="el-icon-setting"></i></div>
          </div>
        </div>
      </section>

      <!-- 统计数据卡片 -->
      <section class="stats-section">
        <div class="stats-grid">
          <el-card class="stat-card animated fade-in" @click="goToAgreements">
            <div class="stat-card-content">
              <div class="stat-icon agreements-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="stat-details">
                <div class="stat-number">{{ totalAgreements }}</div>
                <div class="stat-title">协议总数</div>
                <div class="stat-progress">
                  <el-progress 
                    :percentage="agreementPercentage" 
                    color="#409eff" 
                    :stroke-width="6" 
                    :show-text="false"
                  ></el-progress>
                </div>
              </div>
            </div>
          </el-card>
          
          <el-card class="stat-card animated fade-in delay-1" @click="goToAgreements">
            <div class="stat-card-content">
              <div class="stat-icon active-icon">
                <i class="el-icon-check-circle"></i>
              </div>
              <div class="stat-details">
                <div class="stat-number">{{ activeAgreements }}</div>
                <div class="stat-title">有效协议</div>
                <div class="stat-trend positive">
                  <i class="el-icon-caret-top"></i> {{ agreementGrowth }}% 较上月
                </div>
              </div>
            </div>
          </el-card>
          
          <el-card class="stat-card animated fade-in delay-2" @click="goToUsers">
            <div class="stat-card-content">
              <div class="stat-icon users-icon">
                <i class="el-icon-user"></i>
              </div>
              <div class="stat-details">
                <div class="stat-number">{{ totalUsers }}</div>
                <div class="stat-title">用户总数</div>
                <div class="stat-trend positive">
                  <i class="el-icon-caret-top"></i> {{ userGrowth }}% 较上月
                </div>
              </div>
            </div>
          </el-card>
          
          <el-card class="stat-card animated fade-in delay-3" @click="goToTasks">
            <div class="stat-card-content">
              <div class="stat-icon tasks-icon">
                <i class="el-icon-tickets"></i>
              </div>
              <div class="stat-details">
                <div class="stat-number">{{ totalTasks }}</div>
                <div class="stat-title">进行中任务</div>
                <div class="stat-trend negative">
                  <i class="el-icon-caret-bottom"></i> {{ taskCompletionRate }}% 已完成
                </div>
              </div>
            </div>
          </el-card>
        </div>
      </section>

      <!-- 功能模块卡片 -->
      <section class="features-section">
        <h2 class="section-title">功能模块</h2>
        <div class="features-grid">
          <el-card class="feature-card" @click="goToAgreements">
            <div class="feature-icon agreement-icon">
              <i class="el-icon-document"></i>
            </div>
            <h3 class="feature-title">协议管理</h3>
            <p class="feature-description">创建、编辑、审批和跟踪所有兼职协作协议</p>
            <div class="feature-footer">
              <span>查看详情</span>
              <i class="el-icon-arrow-right"></i>
            </div>
          </el-card>
          
          <el-card class="feature-card" @click="goToTasks">
            <div class="feature-icon task-icon">
              <i class="el-icon-tickets"></i>
            </div>
            <h3 class="feature-title">任务管理</h3>
            <p class="feature-description">分配任务、跟踪进度、管理截止日期和优先级</p>
            <div class="feature-footer">
              <span>查看详情</span>
              <i class="el-icon-arrow-right"></i>
            </div>
          </el-card>
          
          <el-card class="feature-card" @click="goToUsers">
            <div class="feature-icon user-icon">
              <i class="el-icon-user"></i>
            </div>
            <h3 class="feature-title">用户管理</h3>
            <p class="feature-description">管理用户信息、角色权限和团队成员分配</p>
            <div class="feature-footer">
              <span>查看详情</span>
              <i class="el-icon-arrow-right"></i>
            </div>
          </el-card>
        </div>
      </section>

      <!-- 最近活动区域 -->
      <section class="activities-section">
        <h2 class="section-title">最近活动</h2>
        <el-card class="activities-card">
          <div class="activities-list">
            <div class="activity-item" v-for="(activity, index) in recentActivities" :key="index">
              <div class="activity-icon" :class="getActivityIconClass(activity.type)">
                <i :class="getActivityIcon(activity.type)"></i>
              </div>
              <div class="activity-content">
                <div class="activity-title">{{ activity.title }}</div>
                <div class="activity-time">{{ activity.time }}</div>
              </div>
            </div>
          </div>
        </el-card>
      </section>
    </main>

    <!-- 页脚 -->
    <footer class="modern-footer">
      <div class="footer-content">
        <div class="footer-logo">
          <div class="logo-icon small">
            <i class="el-icon-document"></i>
          </div>
          <div class="logo-text small">TaskHub</div>
        </div>
        <div class="footer-info">
          <p>© 2024 兼职技术人员项目协作协议管理系统</p>
          <p>打造高效、规范的协作管理体验</p>
        </div>
      </div>
    </footer>
  </div>
</template>

<script>
import axios from 'axios'
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'

export default {
  name: 'HomeView',
  setup() {
    const router = useRouter()
    const totalAgreements = ref(48)
    const activeAgreements = ref(36)
    const totalUsers = ref(24)
    const totalTasks = ref(12)
    const activeIndex = ref('/')
    const agreementGrowth = ref(12)
    const userGrowth = ref(8)
    const taskCompletionRate = ref(68)
    
    // 计算属性
    const agreementPercentage = computed(() => {
      return Math.round((activeAgreements.value / totalAgreements.value) * 100)
    })
    
    const userName = computed(() => {
      const user = localStorage.getItem('user')
      if (user) {
        try {
          const userObj = JSON.parse(user)
          return userObj.realName || '管理员'
        } catch {
          return '管理员'
        }
      }
      return '管理员'
    })
    
    // 最近活动数据
    const recentActivities = ref([
      {
        type: 'agreement_created',
        title: '新协议《前端开发兼职合作》已创建',
        time: '2小时前'
      },
      {
        type: 'user_joined',
        title: '新用户「张开发」已加入系统',
        time: '昨天'
      },
      {
        type: 'task_completed',
        title: '任务「优化登录页面」已完成',
        time: '2天前'
      },
      {
        type: 'agreement_approved',
        title: '协议《UI设计合作》已通过审批',
        time: '3天前'
      }
    ])
    
    // 监听路由变化，更新当前激活的菜单项
    const updateActiveIndex = () => {
      const path = router.currentRoute.value.path
      if (path === '/') {
        activeIndex.value = '/'
      } else if (path.startsWith('/agreements')) {
        activeIndex.value = '/agreements'
      } else if (path.startsWith('/tasks')) {
        activeIndex.value = '/tasks'
      } else if (path.startsWith('/users')) {
        activeIndex.value = '/users'
      }
    }

    const handleSelect = (key) => {
      activeIndex.value = key
    }

    const handleLogout = async () => {
      try {
        await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          customClass: 'logout-dialog'
        })
        localStorage.removeItem('user')
        localStorage.removeItem('token')
        router.push('/login')
      } catch {
        // 用户取消操作
      }
    }

    const goToAgreements = () => {
      router.push('/agreements')
    }

    const goToUsers = () => {
      router.push('/users')
    }

    const goToTasks = () => {
      router.push('/tasks')
    }

    const createAgreement = () => {
      router.push('/agreements/new')
    }

    // 获取活动图标样式
    const getActivityIconClass = (type) => {
      const classMap = {
        agreement_created: 'primary',
        user_joined: 'success',
        task_completed: 'info',
        agreement_approved: 'success'
      }
      return classMap[type] || 'default'
    }

    // 获取活动图标
    const getActivityIcon = (type) => {
      const iconMap = {
        agreement_created: 'el-icon-plus',
        user_joined: 'el-icon-user-plus',
        task_completed: 'el-icon-check',
        agreement_approved: 'el-icon-check-circle'
      }
      return iconMap[type] || 'el-icon-info'
    }

    // 加载统计数据
    const loadStats = async () => {
      try {
        // 这里可以从后端获取实际数据
        // 暂时使用模拟数据
        console.log('加载统计数据')
      } catch (error) {
        console.error('加载统计数据失败', error)
        ElMessage.error('加载数据失败')
      }
    }

    onMounted(() => {
      loadStats()
      updateActiveIndex()
    })
    
    // 监听路由变化
    router.afterEach(() => {
      updateActiveIndex()
    })

    return {
      totalAgreements,
      activeAgreements,
      totalUsers,
      totalTasks,
      activeIndex,
      agreementGrowth,
      userGrowth,
      taskCompletionRate,
      agreementPercentage,
      userName,
      recentActivities,
      handleSelect,
      handleLogout,
      goToAgreements,
      goToUsers,
      goToTasks,
      createAgreement,
      getActivityIconClass,
      getActivityIcon
    }
  }
}
</script>

<style scoped>
/* 全局样式 */
.home-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4eaf0 100%);
  color: #333;
  display: flex;
  flex-direction: column;
}

/* 导航栏样式 */
.modern-nav {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
  transition: all 0.3s ease;
  box-sizing: border-box;
}

.nav-content {
  width: 100%;
  padding: 0 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 80px;
}

.logo-wrapper {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-icon {
  width: 44px;
  height: 44px;
  border-radius: 12px;
  background: linear-gradient(135deg, #409eff, #667eea);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 24px;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.logo-text .logo-main {
  font-size: 20px;
  font-weight: 600;
  background: linear-gradient(135deg, #409eff, #667eea);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.logo-text .logo-subtitle {
  font-size: 12px;
  color: #909399;
  margin-top: 2px;
}

.nav-menu {
  background: transparent !important;
}

.nav-item {
  font-size: 15px;
  font-weight: 500;
  padding: 0 20px !important;
  transition: all 0.3s ease;
}

.nav-item:hover {
  background: rgba(64, 158, 255, 0.1);
}

.nav-icon {
  margin-right: 8px;
  font-size: 16px;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea, #764ba2);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 18px;
}

.user-avatar:hover {
  transform: scale(1.1);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

/* 主内容区域 */
.main-content {
  flex: 1;
  padding: 104px 24px 24px;
  width: 100%;
  max-width: 100%;
  box-sizing: border-box;
}

/* 英雄区域 */
.hero-section {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 24px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  margin-bottom: 40px;
  overflow: hidden;
  display: flex;
  position: relative;
}

.hero-content {
  flex: 1;
  padding: 64px 48px;
  z-index: 1;
}

.hero-title {
  font-size: 48px;
  font-weight: 700;
  color: #303133;
  margin: 0 0 16px 0;
  line-height: 1.2;
}

.hero-title .highlight {
  background: linear-gradient(135deg, #409eff, #667eea);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-subtitle {
  font-size: 18px;
  color: #606266;
  margin: 0 0 32px 0;
  width: 100%;
  max-width: 100%;
}

.hero-actions {
  display: flex;
  gap: 16px;
}

.primary-btn,
.secondary-btn {
  padding: 12px 28px;
  font-size: 16px;
  font-weight: 500;
  border-radius: 12px;
  transition: all 0.3s ease;
}

.primary-btn {
  background: linear-gradient(135deg, #409eff, #667eea);
  border: none;
}

.primary-btn:hover {
  background: linear-gradient(135deg, #667eea, #764ba2);
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(64, 158, 255, 0.4);
}

.secondary-btn {
  background: white;
  border: 2px solid #e4e7ed;
  color: #606266;
}

.secondary-btn:hover {
  border-color: #409eff;
  color: #409eff;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.2);
}

.hero-visual {
  flex: 1;
  position: relative;
  overflow: hidden;
  min-height: 300px;
}

.visual-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(64, 158, 255, 0.1), rgba(102, 126, 234, 0.1));
}

.visual-elements {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 24px;
}

.element-card {
  width: 100px;
  height: 100px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
  color: white;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.2);
  animation: float 6s ease-in-out infinite;
}

.element-card.card-1 {
  background: linear-gradient(135deg, #409eff, #667eea);
  animation-delay: 0s;
}

.element-card.card-2 {
  background: linear-gradient(135deg, #67c23a, #85ce61);
  animation-delay: 1s;
}

.element-card.card-3 {
  background: linear-gradient(135deg, #e6a23c, #f39c12);
  animation-delay: 2s;
}

.element-card.card-4 {
  background: linear-gradient(135deg, #f56c6c, #e74c3c);
  animation-delay: 3s;
}

/* 统计数据区域 */
.stats-section {
  margin-bottom: 40px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 24px;
}

.stat-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  border: none;
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: all 0.3s ease;
  overflow: hidden;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
}

.stat-card-content {
  display: flex;
  align-items: center;
  padding: 28px;
  gap: 20px;
}

.stat-icon {
  width: 64px;
  height: 64px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
  color: white;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
}

.agreements-icon {
  background: linear-gradient(135deg, #409eff, #667eea);
}

.active-icon {
  background: linear-gradient(135deg, #67c23a, #85ce61);
}

.users-icon {
  background: linear-gradient(135deg, #e6a23c, #f39c12);
}

.tasks-icon {
  background: linear-gradient(135deg, #f56c6c, #e74c3c);
}

.stat-details {
  flex: 1;
}

.stat-number {
  font-size: 36px;
  font-weight: 700;
  color: #303133;
  margin-bottom: 8px;
}

.stat-title {
  font-size: 16px;
  color: #606266;
  margin-bottom: 12px;
}

.stat-trend {
  font-size: 14px;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 4px;
}

.stat-trend.positive {
  color: #67c23a;
}

.stat-trend.negative {
  color: #f56c6c;
}

.stat-progress {
  width: 100%;
}

/* 功能模块区域 */
.features-section {
  margin-bottom: 40px;
}

.section-title {
  font-size: 28px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 24px 0;
  display: flex;
  align-items: center;
  gap: 12px;
}

.section-title::after {
  content: '';
  flex: 1;
  height: 2px;
  background: linear-gradient(90deg, #409eff, transparent);
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 24px;
}

.feature-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  border: none;
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: all 0.3s ease;
  padding: 32px;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.feature-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
}

.feature-card:hover .feature-icon {
  transform: scale(1.1) rotate(5deg);
}

.feature-icon {
  width: 64px;
  height: 64px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32px;
  color: white;
  margin-bottom: 20px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
}

.agreement-icon {
  background: linear-gradient(135deg, #409eff, #667eea);
}

.task-icon {
  background: linear-gradient(135deg, #67c23a, #85ce61);
}

.user-icon {
  background: linear-gradient(135deg, #e6a23c, #f39c12);
}

.feature-title {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 12px 0;
}

.feature-description {
  font-size: 15px;
  color: #606266;
  margin: 0 0 auto 0;
  line-height: 1.6;
}

.feature-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 20px;
  font-size: 14px;
  font-weight: 500;
  color: #409eff;
}

.feature-footer:hover {
  color: #667eea;
}

.feature-footer i {
  transition: transform 0.3s ease;
}

.feature-card:hover .feature-footer i {
  transform: translateX(4px);
}

/* 最近活动区域 */
.activities-section {
  margin-bottom: 40px;
}

.activities-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  border: none;
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.activities-list {
  padding: 8px 0;
}

.activity-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px 24px;
  transition: background-color 0.3s ease;
  border-bottom: 1px solid #f0f2f5;
}

.activity-item:last-child {
  border-bottom: none;
}

.activity-item:hover {
  background: #f8f9fa;
}

.activity-icon {
  width: 40px;
  height: 40px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  color: white;
  flex-shrink: 0;
}

.activity-icon.primary {
  background: linear-gradient(135deg, #409eff, #667eea);
}

.activity-icon.success {
  background: linear-gradient(135deg, #67c23a, #85ce61);
}

.activity-icon.info {
  background: linear-gradient(135deg, #909399, #606266);
}

.activity-icon.default {
  background: linear-gradient(135deg, #e6a23c, #f39c12);
}

.activity-content {
  flex: 1;
}

.activity-title {
  font-size: 15px;
  font-weight: 500;
  color: #303133;
  margin-bottom: 4px;
}

.activity-time {
  font-size: 13px;
  color: #909399;
}

/* 页脚样式 */
.modern-footer {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  box-shadow: 0 -2px 12px rgba(0, 0, 0, 0.1);
  padding: 24px;
}

.footer-content {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 20px;
  flex-direction: column;
}

.footer-logo {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-icon.small {
  width: 36px;
  height: 36px;
  font-size: 20px;
}

.logo-text.small {
  font-size: 18px;
  font-weight: 600;
  background: linear-gradient(135deg, #409eff, #667eea);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.footer-info {
  text-align: center;
}

.footer-info p {
  margin: 4px 0;
  color: #606266;
  font-size: 14px;
}

/* 动画效果 */
@keyframes float {
  0%, 100% {
    transform: translateY(0) rotate(0deg);
  }
  50% {
    transform: translateY(-20px) rotate(5deg);
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.animated {
  animation-duration: 0.6s;
  animation-fill-mode: both;
}

.fade-in {
  animation-name: fadeIn;
}

.delay-1 {
  animation-delay: 0.1s;
}

.delay-2 {
  animation-delay: 0.2s;
}

.delay-3 {
  animation-delay: 0.3s;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .hero-title {
    font-size: 40px;
  }
  
  .hero-content {
    padding: 48px 32px;
  }
  
  .features-grid {
    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  }
}

@media (max-width: 992px) {
  .hero-section {
    flex-direction: column;
  }
  
  .hero-visual {
    min-height: 200px;
    padding: 40px;
  }
  
  .element-card {
    width: 80px;
    height: 80px;
    font-size: 24px;
  }
  
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .nav-content {
    padding: 0 16px;
  }
  
  .logo-text .logo-main {
    font-size: 18px;
  }
  
  .logo-text .logo-subtitle {
    display: none;
  }
  
  .nav-item {
    padding: 0 12px !important;
    font-size: 14px;
  }
  
  .nav-icon {
    margin-right: 4px;
    font-size: 14px;
  }
  
  .main-content {
    padding: 24px 16px 48px;
  }
  
  .hero-content {
    padding: 32px 24px;
  }
  
  .hero-title {
    font-size: 32px;
  }
  
  .hero-subtitle {
    font-size: 16px;
  }
  
  .hero-actions {
    flex-direction: column;
  }
  
  .primary-btn,
  .secondary-btn {
    width: 100%;
  }
  
  .stats-grid {
    grid-template-columns: 1fr;
    gap: 16px;
  }
  
  .features-grid {
    grid-template-columns: 1fr;
    gap: 16px;
  }
  
  .section-title {
    font-size: 24px;
  }
  
  .activity-item {
    padding: 12px 16px;
  }
}

@media (max-width: 480px) {
  .nav-content {
    height: 70px;
  }
  
  .logo-icon {
    width: 36px;
    height: 36px;
    font-size: 20px;
  }
  
  .hero-visual {
    display: none;
  }
  
  .stat-card-content {
    flex-direction: column;
    text-align: center;
    padding: 20px;
  }
  
  .stat-details {
    width: 100%;
  }
  
  .feature-card {
    padding: 24px;
  }
}
</style>