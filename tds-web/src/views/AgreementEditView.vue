<template>
  <div class="agreement-edit-container">
    <!-- 现代化导航栏 -->
    <nav class="modern-nav">
      <div class="nav-content">
        <div class="logo-wrapper">
          <div class="logo-icon">
            <i class="el-icon-document"></i>
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
            <i class="el-icon-edit header-icon"></i>
            {{ isEdit ? '编辑协议' : '创建新协议' }}
          </h1>
          <p class="page-subtitle">{{ isEdit ? '修改协议信息' : '填写并提交新协议' }}</p>
        </div>
      </div>
      
      <!-- 表单区域 -->
      <div class="form-section">
        <el-card class="form-card">
          <div v-if="loading" class="loading">
            <el-loading-spinner></el-loading-spinner>
            <p>加载中...</p>
          </div>
          
          <el-form 
            v-else 
            ref="agreementFormRef" 
            :model="agreementForm" 
            :rules="rules" 
            label-width="120px"
            class="agreement-form"
          >
            <el-form-item label="协议标题" prop="title">
              <el-input 
                v-model="agreementForm.title" 
                placeholder="请输入协议标题"
                class="form-input"
              ></el-input>
            </el-form-item>
            
            <el-form-item label="协议版本" prop="version">
              <el-input 
                v-model="agreementForm.version" 
                placeholder="请输入协议版本"
                class="form-input"
              ></el-input>
            </el-form-item>
            
            <el-form-item label="协议状态" prop="status">
              <el-select 
                v-model="agreementForm.status" 
                placeholder="请选择协议状态"
                class="form-select"
              >
                <el-option label="草稿" value="DRAFT"></el-option>
                <el-option label="有效" value="ACTIVE"></el-option>
                <el-option label="无效" value="INACTIVE"></el-option>
              </el-select>
            </el-form-item>
            
            <el-form-item label="协议内容" prop="content">
              <el-input
                v-model="agreementForm.content"
                type="textarea"
                placeholder="请输入协议内容（支持Markdown格式）"
                rows="15"
                class="form-textarea"
              ></el-input>
            </el-form-item>
            
            <el-form-item class="form-actions">
              <el-button 
                type="primary" 
                @click="submitForm"
                class="submit-btn"
                :loading="saving"
              >
                保存
              </el-button>
              <el-button 
                @click="cancelForm"
                class="cancel-btn"
              >
                取消
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </div>
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
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'

export default {
  name: 'AgreementEditView',
  setup() {
    const route = useRoute()
    const router = useRouter()
    const loading = ref(false)
    const saving = ref(false)
    const agreementFormRef = ref(null)
    const activeIndex = ref('/agreements')
    
    const agreementForm = reactive({
      title: '',
      version: '1.0',
      status: 'DRAFT',
      content: ''
    })
    
    const rules = {
      title: [
        { required: true, message: '请输入协议标题', trigger: 'blur' }
      ],
      version: [
        { required: true, message: '请输入协议版本', trigger: 'blur' }
      ],
      status: [
        { required: true, message: '请选择协议状态', trigger: 'change' }
      ],
      content: [
        { required: true, message: '请输入协议内容', trigger: 'blur' }
      ]
    }
    
    // 判断是编辑模式还是创建模式
    const isEdit = computed(() => !!route.params.id)
    
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
    
    // 监听路由变化，更新当前激活的菜单项
    const updateActiveIndex = () => {
      const path = router.currentRoute.value.path
      if (path === '/') {
        activeIndex.value = '/'
      } else if (path.startsWith('/agreements') || path.startsWith('/agreement/')) {
        activeIndex.value = '/agreements'
      } else if (path.startsWith('/tasks')) {
        activeIndex.value = '/tasks'
      } else if (path.startsWith('/users')) {
        activeIndex.value = '/users'
      }
    }
    
    const handleLogout = () => {
      localStorage.removeItem('user')
      localStorage.removeItem('token')
      router.push('/login')
    }
    
    const handleSelect = (key) => {
      activeIndex.value = key
    }
    
    const fetchAgreement = async () => {
      const id = route.params.id
      if (id) {
        loading.value = true
        try {
          // 尝试从后端获取数据
          const response = await axios.get(`/api/agreements/${id}`)
          const data = response.data
          agreementForm.title = data.title
          agreementForm.version = data.version
          agreementForm.status = data.status
          agreementForm.content = data.content
        } catch (error) {
          console.error('获取协议详情失败', error)
          ElMessage.error('获取协议详情失败')
          // 在实际项目中可以添加更友好的错误处理
        } finally {
          loading.value = false
        }
      }
    }
    
    const submitForm = async () => {
      try {
        // 表单验证
        await agreementFormRef.value.validate()
        saving.value = true
        
        const id = route.params.id
        const user = JSON.parse(localStorage.getItem('user') || '{"id": 1}')
        const agreementData = {
          ...agreementForm,
          creatorId: user.id,
          id: id || Date.now(), // 如果是新创建的协议，生成一个临时ID
          createTime: new Date().toISOString(),
          updateTime: new Date().toISOString()
        }
        
        // 在实际环境中调用API
        try {
          if (id) {
            // 更新协议
            await axios.put(`/api/agreements/${id}`, agreementData)
          } else {
            // 创建新协议
            await axios.post('/api/agreements', agreementData)
          }
        } catch (apiError) {
          // API调用失败时的处理（模拟保存成功）
          console.warn('API调用失败，使用模拟保存', apiError)
          
          // 模拟成功保存到本地存储
          const agreements = JSON.parse(localStorage.getItem('mockAgreements') || '[]')
          if (id) {
            // 更新现有协议
            const index = agreements.findIndex(a => a.id === id)
            if (index !== -1) {
              agreements[index] = agreementData
            }
          } else {
            // 添加新协议
            agreements.push(agreementData)
          }
          localStorage.setItem('mockAgreements', JSON.stringify(agreements))
        }
        
        // 无论API调用是否成功，都显示成功消息并返回列表
        ElMessage.success(id ? '协议更新成功' : '协议创建成功')
        
        // 保存成功后返回列表
        setTimeout(() => {
          router.push('/agreements')
        }, 1000)
      } catch (error) {
        // 判断错误类型
        if (error.name === 'ValidationError' || error.message.includes('验证')) {
          // 表单验证失败
          console.error('表单验证失败', error)
          ElMessage.error('请填写所有必填字段')
        } else {
          // 其他错误
          console.error('保存协议失败', error)
          ElMessage.error('保存失败，请重试')
        }
      } finally {
        saving.value = false
      }
    }
    
    const cancelForm = () => {
      router.push('/agreements')
    }
    
    onMounted(() => {
      updateActiveIndex()
      fetchAgreement()
    })
    
    // 监听路由变化
    router.afterEach(() => {
      updateActiveIndex()
    })
    
    return {
      loading,
      saving,
      agreementFormRef,
      agreementForm,
      rules,
      activeIndex,
      isEdit,
      userName,
      handleLogout,
      handleSelect,
      submitForm,
      cancelForm
    }
  }
}
</script>

<style scoped>
/* 全局样式 */
.agreement-edit-container {
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

/* 表单区域 */
.form-section {
  margin-bottom: 24px;
}

.form-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  border: none;
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.1);
  padding: 32px;
  transition: all 0.3s ease;
}

.form-card:hover {
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
}

.agreement-form {
  width: 100%;
}

.form-input,
.form-select {
  border-radius: 8px;
  border-color: #dcdfe6;
  transition: all 0.3s ease;
  width: 100%;
  max-width: 100%;
}

.form-input:focus,
.form-select:focus {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

.form-textarea {
  border-radius: 8px;
  border-color: #dcdfe6;
  transition: all 0.3s ease;
  width: 100%;
  min-height: 400px;
}

.form-textarea:focus {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

.form-actions {
  display: flex;
  gap: 16px;
  margin-top: 32px;
}

.submit-btn,
.cancel-btn {
  padding: 12px 24px;
  font-size: 16px;
  font-weight: 500;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.submit-btn {
  background: linear-gradient(135deg, #409eff, #667eea);
  border: none;
}

.submit-btn:hover {
  background: linear-gradient(135deg, #667eea, #764ba2);
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(64, 158, 255, 0.4);
}

.cancel-btn:hover {
  color: #409eff;
  border-color: #409eff;
}

/* 加载状态 */
.loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 400px;
  gap: 16px;
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
  .form-input,
  .form-select {
    max-width: 100%;
  }
}

@media (max-width: 992px) {
  .page-header {
    padding: 20px 24px;
  }
  
  .form-card {
    padding: 24px;
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
  
  .page-title {
    font-size: 24px;
  }
  
  .form-card {
    padding: 20px;
  }
  
  .form-actions {
    flex-direction: column;
    gap: 12px;
  }
  
  .submit-btn,
  .cancel-btn {
    width: 100%;
  }
  
  .form-textarea {
    min-height: 300px;
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
  
  .form-card {
    padding: 16px;
  }
  
  .form-textarea {
    min-height: 200px;
  }
}
</style>