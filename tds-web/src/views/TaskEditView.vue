<template>
  <div class="task-edit-container">
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
    
    <el-main class="main-content">
      <div class="page-header">
        <h1>{{ isEdit ? '编辑任务' : '创建任务' }}</h1>
      </div>
      
      <el-card class="task-form-card">
        <el-form
          ref="taskFormRef"
          :model="taskForm"
          :rules="rules"
          label-width="120px"
        >
          <el-form-item label="任务名称" prop="title">
            <el-input v-model="taskForm.title" placeholder="请输入任务名称"></el-input>
          </el-form-item>
          
          <el-form-item label="任务描述" prop="description">
            <el-input v-model="taskForm.description" type="textarea" :rows="4" placeholder="请输入任务描述"></el-input>
          </el-form-item>
          
          <el-form-item label="负责人" prop="assigneeId">
            <el-select v-model="taskForm.assigneeId" placeholder="请选择负责人">
              <el-option
                v-for="user in users"
                :key="user.id"
                :label="user.realName"
                :value="user.id"
              >
                {{ user.realName }} - {{ user.department }}
              </el-option>
            </el-select>
          </el-form-item>
          
          <el-form-item label="任务状态" prop="status">
            <el-radio-group v-model="taskForm.status">
              <el-radio label="pending">待处理</el-radio>
              <el-radio label="in_progress">进行中</el-radio>
              <el-radio label="completed">已完成</el-radio>
            </el-radio-group>
          </el-form-item>
          
          <el-form-item label="优先级" prop="priority">
            <el-select v-model="taskForm.priority" placeholder="请选择优先级">
              <el-option label="低" value="low"></el-option>
              <el-option label="中" value="medium"></el-option>
              <el-option label="高" value="high"></el-option>
            </el-select>
          </el-form-item>
          
          <el-form-item label="截止日期" prop="dueDate">
            <el-date-picker
              v-model="taskForm.dueDate"
              type="date"
              placeholder="选择日期"
              style="width: 100%"
            ></el-date-picker>
          </el-form-item>
          
          <el-form-item v-if="taskForm.status === 'in_progress'" label="完成进度">
            <el-progress v-model="taskForm.progress" :show-text="true"></el-progress>
            <el-input-number
              v-model="taskForm.progress"
              :min="0"
              :max="100"
              style="margin-top: 10px"
            ></el-input-number>
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" @click="submitForm" :loading="loading">提交</el-button>
            <el-button @click="cancelForm">取消</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </el-main>

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
import { ref, reactive, onMounted, computed, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const route = useRoute()
const taskFormRef = ref()
const loading = ref(false)
const activeIndex = ref('/tasks')
const users = ref([])

// 判断是编辑还是创建
const isEdit = computed(() => {
  return route.params.id !== undefined
})

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

// 任务表单数据
const taskForm = reactive({
  id: '',
  title: '',
  description: '',
  assigneeId: '',
  status: 'pending',
  priority: 'medium',
  dueDate: '',
  progress: 0
})

// 表单验证规则
const rules = {
  title: [
    { required: true, message: '请输入任务名称', trigger: 'blur' },
    { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入任务描述', trigger: 'blur' }
  ],
  assigneeId: [
    { required: true, message: '请选择负责人', trigger: 'change' }
  ],
  status: [
    { required: true, message: '请选择任务状态', trigger: 'change' }
  ],
  priority: [
    { required: true, message: '请选择优先级', trigger: 'change' }
  ],
  dueDate: [
    { required: true, message: '请选择截止日期', trigger: 'change' }
  ]
}

// 获取用户列表
const fetchUsers = async () => {
  try {
    const response = await axios.get('/api/users')
    users.value = response.data
  } catch (error) {
    console.error('获取用户列表失败', error)
    // 使用模拟数据作为后备
    users.value = [
      {
        id: 1,
        username: 'admin',
        realName: '管理员',
        department: '技术部',
        position: '管理员'
      },
      {
        id: 2,
        username: 'developer',
        realName: '开发者',
        department: '技术部',
        position: '开发工程师'
      }
    ]
  }
}

// 获取任务数据
const fetchTask = async () => {
  if (!isEdit.value) return
  
  try {
    loading.value = true
    const response = await axios.get(`/api/tasks/${route.params.id}`)
    const taskData = response.data
    
    // 适配后端字段映射
    Object.assign(taskForm, {
      id: taskData.id,
      title: taskData.name || '', // 后端使用name
      description: taskData.description || '',
      assigneeId: taskData.responsiblePerson?.id || '', // 后端使用responsiblePerson
      status: (taskData.status ? taskData.status.toLowerCase() : 'pending'), // 转换为小写
      priority: (taskData.priority ? taskData.priority.toLowerCase() : 'medium'),
      dueDate: taskData.deadline || '', // 后端使用deadline
      progress: taskData.progress || 0
    })
  } catch (error) {
    console.error('获取任务信息失败', error)
    ElMessage.error('获取任务信息失败，请重试')
    // 提供默认数据以便继续开发
    if (isEdit.value) {
      Object.assign(taskForm, {
        id: route.params.id,
        title: '测试任务',
        description: '这是一个测试任务描述',
        assigneeId: '1',
        status: 'pending',
        priority: 'medium',
        dueDate: new Date().toISOString().split('T')[0],
        progress: 0
      })
    }
  } finally {
    loading.value = false
  }
}

// 提交表单
const submitForm = async () => {
  try {
    // 使用Promise方式验证表单
    await taskFormRef.value.validate()
    loading.value = true
    
    // 格式化数据，适配后端模型
    const formData = {
      name: taskForm.title, // 后端使用name字段
      description: taskForm.description,
      responsiblePerson: {
        id: taskForm.assigneeId
      },
      status: taskForm.status.toUpperCase(), // 转换为大写
      priority: taskForm.priority.toUpperCase(), // 转换为大写
      deadline: taskForm.dueDate, // 后端使用deadline字段
      progress: taskForm.progress,
      // 其他必要字段
      creator: {
        id: 1 // 假设当前用户ID为1，实际项目中应从登录状态获取
      }
    }
    
    // 直接调用后端API
    if (isEdit.value) {
      // 更新任务
      await axios.put(`/api/tasks/${taskForm.id}`, formData)
    } else {
      // 创建任务
      await axios.post('/api/tasks', formData)
    }
    
    ElMessage.success(isEdit.value ? '任务更新成功' : '任务创建成功')
    
    // 添加延迟再跳转，确保用户能看到成功提示
    setTimeout(() => {
      router.push('/tasks')
    }, 1000)
  } catch (error) {
    if (error.name === 'ValidationError' || error.message.includes('验证')) {
      console.error('表单验证失败', error)
      ElMessage.error('请填写所有必填字段')
    } else {
    console.error('保存任务失败', error)
    ElMessage.error('保存失败，请检查后端服务是否运行')
    }
  } finally {
    loading.value = false
  }
}

// 取消表单
const cancelForm = () => {
  router.push('/tasks')
}

// 退出登录
const handleLogout = async () => {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
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
const handleSelect = (key, keyPath) => {
  if (key === 'logout') {
    handleLogout()
  }
}

// 更新激活的菜单索引
const updateActiveIndex = (to) => {
  activeIndex.value = to.path.startsWith('/tasks') ? '/tasks' : to.path
}

// 监听路由变化
router.afterEach((to, from) => {
  updateActiveIndex(to)
})

// 监听任务状态变化
watch(() => taskForm.status, (newStatus) => {
  if (newStatus === 'completed') {
    taskForm.progress = 100
  } else if (newStatus === 'pending') {
    taskForm.progress = 0
  }
})

onMounted(() => {
  fetchUsers()
  fetchTask()
})
</script>

<style scoped>
/* 全局样式 */
.task-edit-container {
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

/* 主内容区域 */
.main-content {
  flex: 1;
  padding: 104px 24px 24px;
  width: 100%;
  max-width: 100%;
  box-sizing: border-box;
  margin: 0 auto;
}

/* 页面头部 */
.page-header {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 24px 32px;
  margin-bottom: 24px;
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.1);
}

.page-header h1 {
  margin: 0;
  font-size: 28px;
  font-weight: 700;
  color: #303133;
  display: flex;
  align-items: center;
}

/* 表单卡片 */
.task-form-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  border: none;
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.1);
  padding: 32px;
  transition: all 0.3s ease;
}

/* 页脚样式 */
.modern-footer {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-top: 1px solid rgba(220, 223, 228, 0.5);
  padding: 24px 0;
  margin-top: auto;
}

.footer-content {
  max-width: 100%;
  margin: 0 auto;
  padding: 0 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 20px;
}

.footer-logo {
  display: flex;
  align-items: center;
  gap: 10px;
}

.logo-icon.small {
  width: 32px;
  height: 32px;
  font-size: 18px;
}

.logo-text.small {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.footer-info p {
  margin: 5px 0;
  font-size: 14px;
  color: #909399;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .nav-content {
    padding: 0 16px;
    height: 70px;
  }
  
  .main-content {
    padding: 94px 16px 16px;
  }
  
  .task-form-card {
    padding: 24px;
  }
  
  .footer-content {
    padding: 0 16px;
    flex-direction: column;
    text-align: center;
  }
}
.task-edit-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.logo {
  flex: 1;
  display: flex;
  align-items: center;
  font-size: 18px;
  font-weight: bold;
  color: white;
  padding-left: 20px;
}

.main-content {
    padding: 24px;
    width: 100%;
    max-width: 100%;
  }

  .task-form-card {
    width: 100%;
    max-width: 100%;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }

.page-header {
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #e0e0e0;
}

.page-header h1 {
  margin: 0;
  font-size: 24px;
  font-weight: 500;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .task-edit-container {
    height: auto;
    min-height: 100vh;
  }
  
  .logo {
    padding-left: 10px;
    font-size: 16px;
  }
  
  .el-header {
    padding: 0;
    height: auto;
  }
  
  .el-menu {
    overflow-x: auto;
    white-space: nowrap;
  }
  
  .task-form-card {
    padding: 10px;
  }
  
  .page-header {
    margin: 10px;
  }
  
  .page-header h1 {
    font-size: 20px;
  }
  
  .el-form {
    padding: 10px;
  }
  
  .el-form-item__label {
    width: 100px;
    font-size: 14px;
  }
  
  .el-form-item__content {
    margin-left: 100px;
    width: calc(100% - 100px);
  }

  .el-input,
  .el-select,
  .el-date-picker {
    width: 100%;
    max-width: 100%;
  }
}
</style>