<template>
  <div class="task-detail-container">
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
            任务详情
          </h1>
          <p class="page-subtitle">查看和管理任务的详细信息</p>
        </div>
        <div class="header-actions">
          <el-button 
            @click="router.back()" 
            icon="el-icon-arrow-left"
            class="back-btn"
          >
            返回
          </el-button>
          <el-button 
            type="primary" 
            @click="editTask()"
            icon="el-icon-edit"
            class="edit-btn"
          >
            编辑任务
          </el-button>
        </div>
      </div>
      
      <!-- 任务详情卡片 -->
      <div class="task-detail-section">
        <el-card 
          v-loading="loading" 
          class="task-detail-card"
          shadow="hover"
          border
        >
          <!-- 任务标题和状态标签 -->
          <div class="task-header">
            <div class="task-title-container">
              <h2 class="task-title">{{ task.title }}</h2>
              <div class="task-tags">
                <el-tag 
                  :type="getStatusType(task.status)" 
                  effect="dark"
                  class="status-tag"
                >
                  {{ getStatusText(task.status) }}
                </el-tag>
                <el-tag 
                  :type="getPriorityType(task.priority)"
                  effect="dark"
                  class="priority-tag"
                >
                  {{ getPriorityText(task.priority) }}
                </el-tag>
              </div>
            </div>
          </div>
          
          <!-- 任务元信息 -->
          <div class="task-meta">
            <div class="meta-grid">
              <div class="meta-item">
                <div class="meta-icon">
                  <i class="el-icon-document-copy"></i>
                </div>
                <div class="meta-content">
                  <div class="meta-label">任务ID</div>
                  <div class="meta-value">{{ task.id }}</div>
                </div>
              </div>
              
              <div class="meta-item">
                <div class="meta-icon">
                  <i class="el-icon-user"></i>
                </div>
                <div class="meta-content">
                  <div class="meta-label">负责人</div>
                  <div class="meta-value">
                    <div class="assignee-info">
                      <span class="assignee-avatar">{{ getInitial(task.assigneeName) }}</span>
                      <span class="assignee-name">{{ task.assigneeName || '-' }}</span>
                    </div>
                  </div>
                </div>
              </div>
              
              <div class="meta-item">
                <div class="meta-icon">
                  <i class="el-icon-date"></i>
                </div>
                <div class="meta-content">
                  <div class="meta-label">截止日期</div>
                  <div class="meta-value" :class="{ 'overdue': isOverdue(task.dueDate) }">
                    {{ task.dueDate || '-' }}
                  </div>
                </div>
              </div>
              
              <div class="meta-item">
                <div class="meta-icon">
                  <i class="el-icon-time"></i>
                </div>
                <div class="meta-content">
                  <div class="meta-label">创建时间</div>
                  <div class="meta-value">{{ formatDateTime(task.createdAt) }}</div>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 任务进度 -->
          <div class="task-section" v-if="task.status === 'in_progress'">
            <div class="section-header">
              <h3 class="section-title">任务进度</h3>
            </div>
            <div class="progress-container">
              <el-progress 
                :percentage="task.progress || 0" 
                :color="progressColor"
                :show-text="true"
                class="task-progress"
              ></el-progress>
              <div class="progress-info">
                <span>完成进度：{{ task.progress || 0 }}%</span>
              </div>
            </div>
          </div>
          
          <!-- 任务描述 -->
          <div class="task-section">
            <div class="section-header">
              <h3 class="section-title">任务描述</h3>
            </div>
            <div class="description-content">
              {{ task.description || '暂无描述' }}
            </div>
          </div>
          
          <!-- 任务附件 -->
          <div class="task-section" v-if="task.attachments && task.attachments.length > 0">
            <div class="section-header">
              <h3 class="section-title">任务附件</h3>
            </div>
            <div class="attachments-list">
              <el-divider></el-divider>
              <div 
                v-for="(attachment, index) in task.attachments" 
                :key="index"
                class="attachment-item"
              >
                <i class="el-icon-document"></i>
                <span class="attachment-name">{{ attachment.name }}</span>
                <span class="attachment-size">{{ formatFileSize(attachment.size) }}</span>
                <el-button 
                  type="text" 
                  icon="el-icon-download"
                  size="small"
                >
                  下载
                </el-button>
              </div>
            </div>
          </div>
        </el-card>
      </div>
      
      <!-- 评论区域 -->
      <div class="comments-section">
        <el-card 
          class="comments-card"
          shadow="hover"
          border
        >
          <div class="section-header">
            <h3 class="section-title">任务评论</h3>
            <span class="comment-count">{{ comments.length }} 条评论</span>
          </div>
          
          <!-- 添加评论 -->
          <div class="add-comment">
            <div class="comment-form">
              <el-input
                v-model="newComment"
                type="textarea"
                :rows="3"
                placeholder="添加评论... 按 Ctrl+Enter 快速提交"
                @keyup.enter.ctrl="submitComment"
                class="comment-textarea"
              ></el-input>
              <div class="comment-actions">
                <el-button 
                  type="primary" 
                  @click="submitComment"
                  :disabled="!newComment.trim()"
                  class="submit-comment-btn"
                  icon="el-icon-edit"
                >
                  发表评论
                </el-button>
              </div>
            </div>
          </div>
          
          <!-- 评论列表 -->
          <div class="comments-list">
            <el-divider></el-divider>
            <div v-if="comments.length > 0" class="timeline-container">
              <el-timeline>
                <el-timeline-item
                  v-for="comment in comments"
                  :key="comment.id"
                  :timestamp="formatDateTime(comment.createdAt)"
                  placement="top"
                >
                  <div class="comment-item">
                    <div class="comment-header">
                      <div class="comment-user-info">
                        <span class="comment-avatar">{{ getInitial(comment.userName) }}</span>
                        <span class="comment-user-name">{{ comment.userName }}</span>
                      </div>
                      <div class="comment-time">{{ formatRelativeTime(comment.createdAt) }}</div>
                    </div>
                    <div class="comment-content">{{ comment.content }}</div>
                  </div>
                </el-timeline-item>
              </el-timeline>
            </div>
            <div v-else class="no-comments">
              <div class="no-comments-icon">
                <i class="el-icon-comment"></i>
              </div>
              <p class="no-comments-text">暂无评论，成为第一个评论的人吧！</p>
            </div>
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
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const task = ref({})
const comments = ref([])
const loading = ref(false)
const newComment = ref('')
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

// 进度条颜色
const progressColor = computed(() => {
  const progress = task.value.progress || 0
  if (progress >= 100) return '#67c23a'
  if (progress >= 50) return '#409eff'
  if (progress >= 25) return '#e6a23c'
  return '#f56c6c'
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

// 格式化日期时间
const formatDateTime = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 格式化相对时间
const formatRelativeTime = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  const now = new Date()
  const diff = now - date
  
  const minutes = Math.floor(diff / 60000)
  const hours = Math.floor(diff / 3600000)
  const days = Math.floor(diff / 86400000)
  
  if (minutes < 1) return '刚刚'
  if (minutes < 60) return `${minutes}分钟前`
  if (hours < 24) return `${hours}小时前`
  if (days < 30) return `${days}天前`
  
  return formatDateTime(dateString)
}

// 格式化文件大小
const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 Bytes'
  const k = 1024
  const sizes = ['Bytes', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

// 编辑任务
const editTask = () => {
  router.push(`/tasks/edit/${route.params.id}`)
}

// 获取任务详情
const fetchTaskDetail = async () => {
  try {
    loading.value = true
    
    // 直接从后端API获取任务详情
    const response = await axios.get(`/api/tasks/${route.params.id}`)
    const apiTask = response.data
    
    // 处理任务数据，精确适配后端模型
    task.value = {
      id: apiTask.id,
      name: apiTask.name, // 后端使用name
      title: apiTask.name, // 前端使用title显示
      description: apiTask.description || '',
      progress: apiTask.progress || 0,
      status: apiTask.status, // 保持后端状态格式
      priority: apiTask.priority,
      paymentStatus: apiTask.paymentStatus || 'UNPAID',
      reward: apiTask.reward || 0, // 后端使用reward表示金额
      estimatedTime: apiTask.estimatedTime || 0, // 后端使用estimatedTime表示工时
      deadline: apiTask.deadline, // 后端使用deadline表示截止日期
      createdAt: apiTask.createdAt,
      updatedAt: apiTask.updatedAt,
      // 用户相关字段 - 精确映射后端结构
      responsiblePerson: apiTask.responsiblePerson || {}, // 后端使用responsiblePerson
      assigneeName: apiTask.responsiblePerson?.realName || '未分配',
      assigneeId: apiTask.responsiblePerson?.id || null,
      creator: apiTask.creator || {},
      creatorName: apiTask.creator?.realName || '系统',
      creatorId: apiTask.creator?.id || 0,
      // 关联数据
      attachments: apiTask.attachments || [],
      dependencies: apiTask.dependencies || [],
      milestones: apiTask.milestones || [],
      // 计算字段
      isOverdue: apiTask.deadline ? new Date(apiTask.deadline) < new Date() && apiTask.status !== 'COMPLETED' : false
    }
    
    console.log('任务详情已从后端API获取并处理')
  } catch (apiError) {
    console.error('获取任务详情失败', apiError)
    ElMessage.error('获取任务详情失败，请检查后端服务是否运行')
    
    // 提供最小化的备用数据
    task.value = {
      id: route.params.id,
      title: '任务详情加载失败',
      name: '任务详情加载失败',
      description: '无法从后端获取任务数据，请检查后端服务状态',
      status: 'PENDING',
      statusText: '待分配',
      priority: 'MEDIUM',
      priorityText: '中',
      progress: 0,
      paymentStatus: 'UNPAID',
      paymentStatusText: '未支付',
      reward: 0,
      amount: 0,
      dependencies: []
    }
  } finally {
    loading.value = false
  }
}

// 获取评论列表

// 获取评论列表
const fetchComments = async () => {
  try {
    // 从后端API获取评论数据
    const response = await axios.get(`/api/comments/task/${route.params.id}`)
    const apiComments = response.data || []
    
    // 处理评论数据，适配前端显示
    comments.value = apiComments
      .sort((a, b) => new Date(a.createdAt) - new Date(b.createdAt))
      .map(comment => ({
        id: comment.id,
        content: comment.content,
        userName: comment.user?.realName || '匿名用户',
        userId: comment.user?.id || null,
        createdAt: comment.createdAt,
        // 使用用户名首字母作为头像显示
        avatar: comment.user?.realName ? comment.user.realName.charAt(0) : '匿'
      }))
    
    console.log('评论数据已从后端API获取并处理')
  } catch (error) {
    console.error('获取评论列表失败', error)
    ElMessage.error('获取评论失败，请检查后端服务是否运行')
    // 提供空评论列表
    comments.value = []
  }
}

// 提交评论
const submitComment = async () => {
  const trimmedContent = newComment.value.trim()
  if (!trimmedContent) {
    ElMessage.warning('请输入评论内容')
    return
  }
  
  try {
    const taskId = route.params.id
    const user = userName.value
    
    // 构建API请求数据格式
    const commentData = {
      task: {
        id: taskId
      },
      user: {
        id: 1, // 假设当前用户ID为1，实际项目中应从登录状态获取
        realName: user
      },
      content: trimmedContent
    }
    
    // 立即在界面上显示评论（乐观更新）
    const tempId = `temp-${Date.now()}`
    const optimisticComment = {
      id: tempId,
      content: trimmedContent,
      userName: user,
      userId: 1,
      createdAt: new Date().toISOString(),
      avatar: user.charAt(0),
      isPending: true // 标记为待处理
    }
    comments.value.push(optimisticComment)
    
    // 清空输入框
    newComment.value = ''
    
    // 调用后端API提交评论
    const response = await axios.post('/api/comments', commentData)
    
    // 替换临时评论为API返回的正式评论
    const index = comments.value.findIndex(c => c.id === tempId)
    if (index !== -1) {
      comments.value[index] = {
        id: response.data.id,
        content: response.data.content,
        userName: response.data.user?.realName || user,
        userId: response.data.user?.id || 1,
        createdAt: response.data.createdAt,
        avatar: response.data.user?.realName ? response.data.user.realName.charAt(0) : user.charAt(0),
        isPending: false
      }
    }
    
    // 成功提示
    ElMessage.success('评论发表成功')
  } catch (error) {
    console.error('发表评论失败', error)
    
    // 从列表中移除失败的临时评论
    const tempId = comments.value.find(c => c.isPending)?.id
    if (tempId) {
      comments.value = comments.value.filter(c => c.id !== tempId)
    }
    
    ElMessage.error('发表评论失败，请检查后端服务是否运行')
  }
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
  fetchTaskDetail()
  fetchComments()
  updateActiveIndex()
})
</script>

<style scoped>
/* 全局样式 */
.task-detail-container {
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

.header-actions {
  display: flex;
  gap: 12px;
}

.back-btn {
  border-radius: 8px;
  transition: all 0.3s ease;
}

.back-btn:hover {
  color: #409eff;
  border-color: #409eff;
}

.edit-btn {
  border-radius: 8px;
  background: linear-gradient(135deg, #409eff, #667eea);
  border: none;
  transition: all 0.3s ease;
}

.edit-btn:hover {
  background: linear-gradient(135deg, #667eea, #764ba2);
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(64, 158, 255, 0.4);
}

/* 任务详情区域 */
.task-detail-section {
  margin-bottom: 24px;
}

.task-detail-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  border: none;
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: all 0.3s ease;
}

.task-detail-card:hover {
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
}

/* 任务标题和状态 */
.task-header {
  padding: 24px;
  border-bottom: 1px solid #f0f2f5;
}

.task-title-container {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.task-title {
  font-size: 24px;
  font-weight: 700;
  color: #303133;
  margin: 0;
  line-height: 1.4;
}

.task-tags {
  display: flex;
  gap: 12px;
}

.status-tag,
.priority-tag {
  padding: 6px 16px;
  font-size: 14px;
  border-radius: 6px;
}

/* 任务元信息 */
.task-meta {
  padding: 24px;
}

.meta-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 24px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  background: #fafafa;
  border-radius: 12px;
  transition: all 0.3s ease;
}

.meta-item:hover {
  background: #f0f2f5;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.meta-icon {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  background: linear-gradient(135deg, #409eff, #667eea);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
}

.meta-content {
  flex: 1;
}

.meta-label {
  font-size: 14px;
  color: #606266;
  margin-bottom: 4px;
}

.meta-value {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.assignee-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.assignee-avatar {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea, #764ba2);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 12px;
  font-weight: 600;
}

.assignee-name {
  font-weight: 500;
}

.overdue {
  color: #f56c6c !important;
  font-weight: 700 !important;
}

/* 任务章节 */
.task-section {
  padding: 24px;
  border-top: 1px solid #f0f2f5;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 8px;
}

/* 任务进度 */
.progress-container {
  padding: 16px;
  background: #fafafa;
  border-radius: 12px;
}

.task-progress {
  margin-bottom: 12px;
}

.progress-info {
  text-align: center;
  font-size: 14px;
  color: #606266;
}

/* 任务描述 */
.description-content {
  padding: 16px;
  background: #fafafa;
  border-radius: 12px;
  font-size: 16px;
  line-height: 1.6;
  color: #606266;
  white-space: pre-line;
}

/* 附件列表 */
.attachments-list {
  margin-top: 16px;
}

.attachment-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px;
  background: #fafafa;
  border-radius: 8px;
  margin-bottom: 12px;
  transition: all 0.3s ease;
}

.attachment-item:hover {
  background: #f0f2f5;
  transform: translateX(4px);
}

.attachment-item i {
  color: #409eff;
  margin-right: 12px;
  font-size: 18px;
}

.attachment-name {
  flex: 1;
  font-weight: 500;
  color: #303133;
}

.attachment-size {
  color: #909399;
  margin-right: 16px;
  font-size: 14px;
}

/* 评论区域 */
.comments-section {
  margin-bottom: 24px;
}

.comments-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  border: none;
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: all 0.3s ease;
}

.comments-card:hover {
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
}

.comment-count {
  font-size: 14px;
  color: #909399;
}

/* 添加评论 */
.add-comment {
  padding: 24px;
  border-bottom: 1px solid #f0f2f5;
}

.comment-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.comment-textarea {
  border-radius: 12px;
  border-color: #dcdfe6;
  resize: vertical;
  min-height: 120px;
}

.comment-textarea:focus {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

.comment-actions {
  display: flex;
  justify-content: flex-end;
}

.submit-comment-btn {
  border-radius: 8px;
  background: linear-gradient(135deg, #409eff, #667eea);
  border: none;
  transition: all 0.3s ease;
}

.submit-comment-btn:hover:not(:disabled) {
  background: linear-gradient(135deg, #667eea, #764ba2);
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(64, 158, 255, 0.4);
}

/* 评论列表 */
.comments-list {
  padding: 24px;
}

.timeline-container {
  margin-top: 16px;
}

.comment-item {
  background: #fafafa;
  padding: 16px;
  border-radius: 12px;
  transition: all 0.3s ease;
}

.comment-item:hover {
  background: #f0f2f5;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.comment-user-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.comment-avatar {
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
}

.comment-user-name {
  font-weight: 600;
  color: #303133;
}

.comment-time {
  font-size: 14px;
  color: #909399;
}

.comment-content {
  font-size: 16px;
  line-height: 1.6;
  color: #606266;
  white-space: pre-line;
}

/* 无评论状态 */
.no-comments {
  text-align: center;
  padding: 48px 24px;
}

.no-comments-icon {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: #f0f2f5;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #909399;
  font-size: 40px;
  margin: 0 auto 24px;
}

.no-comments-text {
  font-size: 16px;
  color: #909399;
  margin: 0;
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

/* 响应式设计 */
@media (max-width: 1200px) {
  .meta-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 992px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 20px;
  }
  
  .header-actions {
    width: 100%;
    justify-content: flex-start;
  }
  
  .back-btn,
  .edit-btn {
    flex: 1;
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
  
  .task-header {
    padding: 20px;
  }
  
  .task-title {
    font-size: 22px;
  }
  
  .task-meta {
    padding: 20px;
  }
  
  .meta-grid {
    grid-template-columns: 1fr;
    gap: 16px;
  }
  
  .task-section {
    padding: 20px;
  }
  
  .add-comment,
  .comments-list {
    padding: 20px;
  }
  
  .comment-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
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
  
  .header-actions {
    flex-direction: column;
    gap: 8px;
  }
  
  .task-header {
    padding: 16px;
  }
  
  .task-title {
    font-size: 20px;
  }
  
  .task-tags {
    flex-direction: column;
    gap: 8px;
  }
  
  .task-meta {
    padding: 16px;
  }
  
  .meta-item {
    padding: 12px;
  }
  
  .meta-icon {
    width: 36px;
    height: 36px;
    font-size: 16px;
  }
  
  .task-section {
    padding: 16px;
  }
  
  .add-comment,
  .comments-list {
    padding: 16px;
  }
  
  .comment-item {
    padding: 12px;
  }
  
  .no-comments {
    padding: 32px 16px;
  }
}
</style>