<template>
  <div class="task-list-container">
    <!-- 现代化导航栏 -->
    <nav class="modern-nav">
      <div class="nav-content">
        <div class="logo-wrapper">
          <div class="logo-icon">
            <i class="el-icon-tickets"></i>
          </div>
          <div class="logo-text">
            <div class="logo-main">TaskHub</div>
            <div class="logo-subtitle">协作管理平台</div>
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
      <!-- 页面头部 -->
      <div class="page-header">
        <div class="header-content">
          <h1 class="page-title">
            <i class="el-icon-tickets header-icon"></i>
            任务管理
          </h1>
          <p class="page-subtitle">管理所有任务和工作流程</p>
        </div>
        <el-button 
          type="primary" 
          @click="createNewTask" 
          class="create-btn"
          icon="el-icon-plus"
        >
          新建任务
        </el-button>
      </div>
      
      <!-- 统计卡片 -->
      <div class="stats-cards">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-value">{{ totalTasks }}</div>
            <div class="stat-label">总任务数</div>
          </div>
        </el-card>
        <el-card class="stat-card warning">
          <div class="stat-content">
            <div class="stat-value">{{ pendingTasks }}</div>
            <div class="stat-label">待处理</div>
          </div>
        </el-card>
        <el-card class="stat-card primary">
          <div class="stat-content">
            <div class="stat-value">{{ inProgressTasks }}</div>
            <div class="stat-label">进行中</div>
          </div>
        </el-card>
        <el-card class="stat-card success">
          <div class="stat-content">
            <div class="stat-value">{{ completedTasks }}</div>
            <div class="stat-label">已完成</div>
          </div>
        </el-card>
        <el-card class="stat-card danger">
          <div class="stat-content">
            <div class="stat-value">{{ highPriorityTasks }}</div>
            <div class="stat-label">高优先级</div>
          </div>
        </el-card>
      </div>
      
      <!-- 搜索区域 -->
      <div class="search-section">
        <el-card class="search-card">
          <el-form :inline="true" class="search-form">
            <el-form-item label="搜索" class="search-item">
              <el-input 
                v-model="searchQuery" 
                placeholder="任务名称或描述"
                class="search-input"
                clearable
                prefix-icon="el-icon-search"
              ></el-input>
            </el-form-item>
            <el-form-item label="状态" class="search-item">
              <el-select 
                v-model="statusFilter" 
                placeholder="任务状态"
                class="search-select"
                clearable
              >
                <el-option label="待处理" value="pending"></el-option>
                <el-option label="进行中" value="in_progress"></el-option>
                <el-option label="已完成" value="completed"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="优先级" class="search-item">
              <el-select 
                v-model="priorityFilter" 
                placeholder="优先级"
                class="search-select"
                clearable
              >
                <el-option label="低" value="low"></el-option>
                <el-option label="中" value="medium"></el-option>
                <el-option label="高" value="high"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item class="search-item">
              <el-button 
                type="primary" 
                @click="searchTasks"
                icon="el-icon-search"
                class="search-btn"
              >
                搜索
              </el-button>
              <el-button 
                @click="resetFilters"
                icon="el-icon-refresh"
                class="reset-btn"
              >
                重置
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </div>
      
      <!-- 表格区域 -->
      <div class="table-section">
        <el-card class="table-card">
          <div class="table-header">
            <h3 class="table-title">任务列表</h3>
            <div class="table-actions">
              <el-button 
                type="info" 
                @click="exportTasks"
                icon="el-icon-download"
                size="small"
              >
                导出数据
              </el-button>
            </div>
          </div>
          
          <div class="table-container">
            <el-table 
              :data="filteredTasks" 
              style="width: 100%"
              v-loading="loading"
              border
              stripe
              fit
              highlight-current-row
              :header-cell-style="{ backgroundColor: '#fafafa', fontWeight: 'bold' }"
            >
              <el-table-column prop="id" label="任务ID" width="80" type="index"></el-table-column>
              <el-table-column prop="title" label="任务名称" min-width="200">
                <template #default="scope">
                  <div class="task-title-cell">
                    <el-tag 
                      :type="getPriorityType(scope.row.priority)" 
                      size="mini"
                      effect="dark"
                      class="priority-tag"
                    >
                      {{ getPriorityText(scope.row.priority) }}
                    </el-tag>
                    <a href="javascript:void(0)" @click="viewTask(scope.row.id)" class="task-title-link">
                      {{ scope.row.title }}
                    </a>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="description" label="任务描述" min-width="300">
                <template #default="scope">
                  <div class="task-description" :title="scope.row.description">
                    {{ scope.row.description }}
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="assigneeName" label="负责人" width="120">
                <template #default="scope">
                  <div class="assignee-cell">
                    <span class="assignee-avatar">{{ getInitial(scope.row.assigneeName) }}</span>
                    <span class="assignee-name">{{ scope.row.assigneeName }}</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="status" label="状态" width="100">
                <template #default="scope">
                  <el-tag
                    :type="getStatusType(scope.row.status)"
                    size="small"
                    effect="dark"
                    class="status-tag"
                  >
                    {{ getStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="dueDate" label="截止日期" width="140">
                <template #default="scope">
                  <div class="due-date-cell" :class="{ 'overdue': isOverdue(scope.row.dueDate) }">{{ scope.row.dueDate }}</div>
                </template>
              </el-table-column>
              <el-table-column prop="createdAt" label="创建时间" width="160"></el-table-column>
              <el-table-column label="操作" width="180" fixed="right">
                <template #default="scope">
                  <el-button 
                    size="small" 
                    type="primary"
                    icon="el-icon-view"
                    @click="viewTask(scope.row.id)"
                    class="table-btn"
                  >
                    查看
                  </el-button>
                  <el-button 
                    size="small" 
                    type="success"
                    icon="el-icon-edit"
                    @click="editTask(scope.row.id)"
                    class="table-btn"
                  >
                    编辑
                  </el-button>
                  <el-button 
                    size="small" 
                    type="danger"
                    icon="el-icon-delete"
                    @click="deleteTask(scope.row.id)"
                    class="table-btn"
                  >
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
          
          <!-- 分页 -->
          <div class="pagination-section">
            <el-pagination
              v-model:current-page="currentPage"
              v-model:page-size="pageSize"
              :page-sizes="[10, 20, 50, 100]"
              layout="total, sizes, prev, pager, next, jumper"
              :total="totalTasks"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              background
              class="pagination"
            ></el-pagination>
          </div>
        </el-card>
      </div>
    </main>

    <!-- 页脚 -->
    <footer class="modern-footer">
      <div class="footer-content">
        <div class="footer-logo">
          <div class="logo-icon small">
            <i class="el-icon-tickets"></i>
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

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const tasks = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const totalTasks = ref(0)
const searchQuery = ref('')
const statusFilter = ref('')
const priorityFilter = ref('')
const activeIndex = ref('/tasks')

// 计算属性：用户信息
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

// 统计数据计算
const pendingTasks = computed(() => 
  tasks.value.filter(task => task.status === 'pending').length
)

const inProgressTasks = computed(() => 
  tasks.value.filter(task => task.status === 'in_progress').length
)

const completedTasks = computed(() => 
  tasks.value.filter(task => task.status === 'completed').length
)

const highPriorityTasks = computed(() => 
  tasks.value.filter(task => task.priority === 'high').length
)

// 过滤后的任务列表
const filteredTasks = computed(() => {
  let result = tasks.value
  
  // 搜索过滤
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    result = result.filter(task => 
      task.title.toLowerCase().includes(query) || 
      task.description.toLowerCase().includes(query)
    )
  }
  
  // 状态过滤
  if (statusFilter.value) {
    result = result.filter(task => task.status === statusFilter.value)
  }
  
  // 优先级过滤
  if (priorityFilter.value) {
    result = result.filter(task => task.priority === priorityFilter.value)
  }
  
  return result
})

// 获取姓名首字母
const getInitial = (name) => {
  return name ? name.charAt(0) : '?'
}

// 获取状态类型
const getStatusType = (status) => {
  const typeMap = {
    pending: 'warning',
    in_progress: 'primary',
    completed: 'success'
  }
  return typeMap[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const textMap = {
    pending: '待处理',
    in_progress: '进行中',
    completed: '已完成'
  }
  return textMap[status] || status
}

// 获取优先级类型
const getPriorityType = (priority) => {
  const typeMap = {
    low: 'info',
    medium: 'warning',
    high: 'danger'
  }
  return typeMap[priority] || 'info'
}

// 获取优先级文本
const getPriorityText = (priority) => {
  const textMap = {
    low: '低',
    medium: '中',
    high: '高'
  }
  return textMap[priority] || priority
}

// 检查是否过期
const isOverdue = (dueDate) => {
  if (!dueDate) return false
  const today = new Date()
  const due = new Date(dueDate)
  return due < today
}

// 查看任务
const viewTask = (id) => {
  router.push(`/tasks/${id}`)
}

// 编辑任务
const editTask = (id) => {
  router.push(`/tasks/edit/${id}`)
}

// 删除任务
const deleteTask = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除该任务吗？删除后将无法恢复，相关评论数据也会被清除。', '删除确认', {
      confirmButtonText: '确定删除',
      cancelButtonText: '取消',
      type: 'warning',
      customClass: 'delete-confirm-dialog'
    })
    
    try {
      // 尝试API调用
      await axios.delete(`/api/tasks/${id}`)
    } catch (apiError) {
      console.warn('API调用失败，执行本地删除', apiError)
    }
    
    // 更新本地数据
    tasks.value = tasks.value.filter(task => task.id !== id)
    totalTasks.value = tasks.value.length
    
    // 更新本地存储中的任务数据
    const storedTasks = localStorage.getItem('mockTasks')
    if (storedTasks) {
      let allTasks = JSON.parse(storedTasks)
      allTasks = allTasks.filter(task => task.id !== id)
      localStorage.setItem('mockTasks', JSON.stringify(allTasks))
    }
    
    // 同时清理相关的评论数据
    const storedComments = localStorage.getItem('mockComments')
    if (storedComments) {
      let allComments = JSON.parse(storedComments)
      allComments = allComments.filter(comment => comment.taskId !== id)
      localStorage.setItem('mockComments', JSON.stringify(allComments))
    }
    
    ElMessage.success('任务删除成功')
  } catch (error) {
    if (error === 'cancel' || error.name === 'Error' && error.message.includes('cancel')) {
      ElMessage.info('已取消删除操作')
    } else {
      console.error('删除任务失败', error)
      ElMessage.error('删除失败，请重试')
    }
  }
}

// 创建新任务
const createNewTask = () => {
  router.push('/tasks/new')
}

// 搜索任务
const searchTasks = () => {
  currentPage.value = 1
}

// 重置过滤器
const resetFilters = () => {
  searchQuery.value = ''
  statusFilter.value = ''
  priorityFilter.value = ''
  currentPage.value = 1
}

// 导出任务数据
const exportTasks = () => {
  ElMessage.success('导出功能正在开发中')
  // 实际项目中可以实现Excel导出功能
}

// 分页处理
const handleSizeChange = (newSize) => {
  pageSize.value = newSize
  currentPage.value = 1
}

const handleCurrentChange = (newCurrent) => {
  currentPage.value = newCurrent
}

// 获取任务列表
const fetchTasks = async () => {
  try {
    loading.value = true
    
    // 构建查询参数
    const params = {}
    
    // 后端API只需要基本的查询参数，不需要复杂的路由
    if (searchQuery.value) {
      params.name = searchQuery.value
    }
    if (statusFilter.value) {
      params.status = statusFilter.value.toUpperCase()
    }
    if (priorityFilter.value) {
      params.priority = priorityFilter.value.toUpperCase()
    }
    
    // 直接调用后端API获取任务列表
    const response = await axios.get('/api/tasks', { params })
    const apiTasks = response.data
    
    // 数据转换：适配前端显示需要的字段
    const enhancedTasks = apiTasks.map(task => ({
      id: task.id,
      title: task.name, // 后端使用name
      description: task.description || '',
      assigneeId: task.responsiblePerson?.id || null,
      assigneeName: task.responsiblePerson?.realName || '未分配',
      status: task.status ? task.status.toLowerCase() : 'pending', // 转换为小写适配前端
      priority: task.priority ? task.priority.toLowerCase() : 'medium',
      duration: task.estimatedTime ? `${task.estimatedTime}天` : '未知',
      amount: task.reward?.toString() || '0',
      paymentStatus: task.paymentStatus ? task.paymentStatus.toLowerCase() : 'unpaid',
      dueDate: task.deadline,
      createdAt: task.createdAt,
      updateTime: task.updatedAt || task.createdAt,
      progress: task.progress || 0,
      // 计算是否过期
      isOverdue: task.deadline ? new Date(task.deadline) < new Date() && task.status !== 'COMPLETED' : false
    }))
    
    tasks.value = enhancedTasks
    totalTasks.value = enhancedTasks.length
    
    console.log('任务数据已从后端API获取并处理')
  } catch (error) {
    console.error('获取任务列表失败', error)
    ElMessage.error('获取数据失败，请检查后端服务是否运行')
    
    // 显示空状态
    tasks.value = []
    totalTasks.value = 0
  } finally {
    loading.value = false
  }
}

// 状态映射函数
const mapStatus = (status) => {
  const statusMap = {
    'NOT_STARTED': 'pending',
    'IN_PROGRESS': 'in_progress',
    'REVIEWING': 'reviewing',
    'COMPLETED': 'completed'
  }
  return statusMap[status] || status?.toLowerCase() || 'pending'
}

// 优先级映射函数
const mapPriority = (priority) => {
  const priorityMap = {
    'LOW': 'low',
    'MEDIUM': 'medium',
    'HIGH': 'high',
    'URGENT': 'urgent'
  }
  return priorityMap[priority] || priority?.toLowerCase() || 'medium'
}

// 支付状态映射函数
const mapPaymentStatus = (paymentStatus) => {
  const paymentMap = {
    'NOT_PAID': 'unpaid',
    'PARTIALLY_PAID': 'partially_paid',
    'PAID': 'paid'
  }
  return paymentMap[paymentStatus] || paymentStatus?.toLowerCase() || 'unpaid'
}

// 计算进度函数
const calculateProgress = (status) => {
  const progressMap = {
    'NOT_STARTED': 0,
    'IN_PROGRESS': 50,
    'REVIEWING': 80,
    'COMPLETED': 100
  }
  return progressMap[status] || 0
}

// 退出登录
const handleLogout = async () => {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
      customClass: 'logout-dialog'
    })
    // 清除本地存储的登录信息
    localStorage.removeItem('user')
    localStorage.removeItem('token')
    // 跳转到登录页面
    router.push('/login')
  } catch (error) {
    // 用户取消退出
  }
}

// 菜单项选择处理
const handleSelect = (key) => {
  activeIndex.value = key
}

// 更新激活的菜单索引
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

// 监听路由变化
router.afterEach(() => {
  updateActiveIndex()
})

onMounted(() => {
  fetchTasks()
  updateActiveIndex()
})
</script>

<style scoped>
/* 全局样式 */
.task-list-container {
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
  transition: all 0.3s ease;
}

.logo-icon:hover {
  transform: scale(1.05);
  box-shadow: 0 6px 16px rgba(64, 158, 255, 0.4);
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
  position: relative;
}

.nav-item:hover {
  background: rgba(64, 158, 255, 0.1);
}

.nav-item.is-active {
  color: #409eff !important;
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

/* 页面头部 */
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 24px 32px;
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.page-header:hover {
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
}

.header-content {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.page-title {
  font-size: 28px;
  font-weight: 700;
  color: #303133;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 12px;
}

.header-icon {
  color: #409eff;
  font-size: 32px;
}

.page-subtitle {
  font-size: 16px;
  color: #606266;
  margin: 0;
}

.create-btn {
  padding: 12px 24px;
  font-size: 16px;
  font-weight: 500;
  border-radius: 12px;
  background: linear-gradient(135deg, #409eff, #667eea);
  border: none;
  transition: all 0.3s ease;
}

.create-btn:hover {
  background: linear-gradient(135deg, #667eea, #764ba2);
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(64, 158, 255, 0.4);
}

/* 统计卡片 */
.stats-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 20px;
  margin-bottom: 24px;
}

.stat-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  border: none;
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.1);
  padding: 24px;
  transition: all 0.3s ease;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.stat-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 6px;
  height: 100%;
  background: linear-gradient(135deg, #409eff, #667eea);
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
}

.stat-card.warning::before {
  background: linear-gradient(135deg, #e6a23c, #ebb563);
}

.stat-card.primary::before {
  background: linear-gradient(135deg, #409eff, #66b1ff);
}

.stat-card.success::before {
  background: linear-gradient(135deg, #67c23a, #85ce61);
}

.stat-card.danger::before {
  background: linear-gradient(135deg, #f56c6c, #f78989);
}

.stat-content {
  text-align: center;
  padding-left: 12px;
}

.stat-value {
  font-size: 36px;
  font-weight: 700;
  color: #303133;
  margin-bottom: 8px;
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {
  0% { transform: scale(1); }
  50% { transform: scale(1.05); }
  100% { transform: scale(1); }
}

.stat-card.warning .stat-value {
  color: #e6a23c;
}

.stat-card.primary .stat-value {
  color: #409eff;
}

.stat-card.success .stat-value {
  color: #67c23a;
}

.stat-card.danger .stat-value {
  color: #f56c6c;
}

.stat-label {
  font-size: 16px;
  color: #606266;
  font-weight: 500;
}

/* 搜索区域 */
.search-section {
  margin-bottom: 24px;
}

.search-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  border: none;
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: all 0.3s ease;
}

.search-card:hover {
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
}

.search-form {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 20px;
  padding: 24px;
}

.search-item {
  margin-bottom: 0;
}

.search-input,
.search-select {
  min-width: 180px;
  border-radius: 8px;
  border-color: #dcdfe6;
  transition: all 0.3s ease;
}

.search-input:focus,
.search-select:focus {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

.search-btn,
.reset-btn {
  border-radius: 8px;
  transition: all 0.3s ease;
}

.search-btn {
  background: linear-gradient(135deg, #409eff, #667eea);
  border: none;
}

.search-btn:hover {
  background: linear-gradient(135deg, #667eea, #764ba2);
}

.reset-btn:hover {
  color: #409eff;
  border-color: #409eff;
}

/* 表格区域 */
.table-section {
  margin-bottom: 24px;
}

.table-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  border: none;
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: all 0.3s ease;
}

.table-card:hover {
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px 24px 0;
  margin-bottom: 20px;
}

.table-title {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  margin: 0;
}

.table-container {
  padding: 0 24px;
  overflow-x: auto;
}

/* 任务标题单元格 */
.task-title-cell {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.priority-tag {
  margin-bottom: 4px;
}

.task-title-link {
  color: #409eff;
  font-weight: 500;
  text-decoration: none;
  transition: color 0.3s ease;
}

.task-title-link:hover {
  color: #667eea;
  text-decoration: underline;
}

/* 任务描述 */
.task-description {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  color: #606266;
}

/* 负责人单元格 */
.assignee-cell {
  display: flex;
  align-items: center;
  gap: 8px;
}

.assignee-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea, #764ba2);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 14px;
  font-weight: 600;
  flex-shrink: 0;
}

.assignee-name {
  font-weight: 500;
}

/* 状态标签 */
.status-tag {
  font-size: 12px;
  padding: 2px 10px;
}

/* 截止日期 */
.due-date-cell {
  font-weight: 500;
  transition: color 0.3s ease;
}

.due-date-cell.overdue {
  color: #f56c6c;
  font-weight: 600;
}

/* 表格按钮 */
.table-btn {
  margin-right: 6px;
  transition: all 0.3s ease;
}

.table-btn:hover {
  transform: translateY(-1px);
}

/* 分页 */
.pagination-section {
  padding: 24px;
  display: flex;
  justify-content: flex-end;
  border-top: 1px solid #f0f2f5;
  margin-top: 20px;
}

.pagination {
  display: flex;
  align-items: center;
  gap: 16px;
}

.pagination .el-pagination__sizes .el-input .el-input__inner {
  border-radius: 6px;
}

.pagination .el-pagination__jump .el-input .el-input__inner {
  border-radius: 6px;
}

/* 页脚样式 */
.modern-footer {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  box-shadow: 0 -2px 12px rgba(0, 0, 0, 0.1);
  padding: 24px;
  margin-top: auto;
}

.footer-content {
  max-width: 1400px;
  margin: 0 auto;
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

/* 响应式设计 */
@media (max-width: 1200px) {
  .stats-cards {
    grid-template-columns: repeat(3, 1fr);
  }
  
  .search-form {
    gap: 16px;
  }
}

@media (max-width: 992px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 20px;
  }
  
  .create-btn {
    width: 100%;
  }
  
  .search-item {
    width: 100%;
  }
  
  .search-input,
  .search-select {
    width: 100%;
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
    padding: 16px;
  }
  
  .page-header {
    padding: 20px 24px;
  }
  
  .page-title {
    font-size: 24px;
  }
  
  .stats-cards {
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
  }
  
  .search-form {
    padding: 20px;
  }
  
  .table-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }
  
  .table-actions {
    width: 100%;
    text-align: right;
  }
  
  .pagination-section {
    padding: 20px;
  }
  
  .pagination {
    flex-direction: column;
    align-items: center;
    gap: 12px;
  }
  
  .el-table {
    font-size: 14px;
  }
  
  .el-table-column {
    min-width: 80px;
  }
  
  .table-btn {
    padding: 6px 8px;
    font-size: 12px;
    margin-right: 4px;
  }
  
  .task-title-cell {
    flex-direction: column;
    align-items: flex-start;
    gap: 4px;
  }
  
  .assignee-cell {
    flex-direction: column;
    align-items: flex-start;
    gap: 4px;
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
  
  .nav-item {
    padding: 0 8px !important;
    font-size: 12px;
  }
  
  .nav-icon {
    margin-right: 2px;
    font-size: 12px;
  }
  
  .user-avatar {
    width: 36px;
    height: 36px;
    font-size: 16px;
  }
  
  .main-content {
    padding: 12px;
  }
  
  .page-header {
    padding: 16px 20px;
  }
  
  .page-title {
    font-size: 20px;
  }
  
  .stats-cards {
    grid-template-columns: 1fr;
  }
  
  .search-form {
    padding: 16px;
    gap: 12px;
  }
  
  .stat-card {
    padding: 20px;
  }
  
  .stat-value {
    font-size: 32px;
  }
  
  .table-header {
    padding: 20px 20px 0;
  }
  
  .table-container {
    padding: 0 16px;
  }
  
  .el-table--border {
    border-radius: 8px;
  }
}

/* 现代化页脚样式 */
.modern-footer {
  background: linear-gradient(135deg, #1f2937 0%, #111827 100%);
  color: white;
  padding: 2rem;
  margin-top: 2rem;
  box-shadow: 0 -4px 6px rgba(0, 0, 0, 0.1);
}

.footer-content {
  max-width: 1400px;
  margin: 0 auto;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  align-items: center;
}

.footer-left {
  margin-bottom: 1rem;
}

.footer-logo {
  font-size: 1.5rem;
  font-weight: bold;
  margin-bottom: 0.5rem;
  background: linear-gradient(135deg, #409eff, #667eea);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.footer-description {
  font-size: 0.9rem;
  opacity: 0.8;
}

.footer-center {
  margin-bottom: 1rem;
}

.footer-links {
  display: flex;
  gap: 1.5rem;
}

.footer-link {
  color: white;
  text-decoration: none;
  font-size: 0.9rem;
  opacity: 0.8;
  transition: all 0.3s ease;
}

.footer-link:hover {
  opacity: 1;
  color: #409eff;
  transform: translateY(-1px);
}

.footer-right {
  font-size: 0.875rem;
  opacity: 0.7;
}

/* 响应式页脚 */
@media (max-width: 768px) {
  .modern-footer {
    padding: 1.5rem 1rem;
  }
  
  .footer-content {
    flex-direction: column;
    text-align: center;
    gap: 1.5rem;
  }
  
  .footer-links {
    justify-content: center;
    flex-wrap: wrap;
    gap: 1rem;
  }
}

/* 状态和优先级标签样式增强 */
.status-badge {
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 500;
  display: inline-block;
}

.status-completed {
  background-color: #d1fae5;
  color: #065f46;
}

.status-in_progress {
  background-color: #dbeafe;
  color: #1e40af;
}

.status-pending {
  background-color: #fef3c7;
  color: #92400e;
}

.priority-badge {
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 500;
  display: inline-block;
}

.priority-high {
  background-color: #fee2e2;
  color: #991b1b;
}

.priority-medium {
  background-color: #dbeafe;
  color: #1e40af;
}

.priority-low {
  background-color: #d1fae5;
  color: #065f46;
}

.payment-badge {
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 500;
  display: inline-block;
}

.payment-paid {
  background-color: #d1fae5;
  color: #065f46;
}

.payment-partial {
  background-color: #fef3c7;
  color: #92400e;
}

.payment-unpaid {
  background-color: #fee2e2;
  color: #991b1b;
}

/* 过期任务样式 */
.overdue-task {
  background-color: #fef2f2;
  border-left: 4px solid #ef4444;
}

/* 按钮动画效果 */
.el-button {
  transition: all 0.3s ease;
}

.el-button:hover {
  transform: translateY(-1px);
}

/* 加载动画优化 */
.el-table__empty-text {
  padding: 3rem;
  color: #9ca3af;
}

/* 滚动条样式优化 */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>