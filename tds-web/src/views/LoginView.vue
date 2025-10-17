<template>
  <div class="login-container">
    <!-- 背景装饰 -->
    <div class="bg-decoration">
      <div class="bg-circle circle-1"></div>
      <div class="bg-circle circle-2"></div>
      <div class="bg-circle circle-3"></div>
    </div>
    
    <!-- 登录卡片 -->
    <div class="login-card">
      <!-- 卡片头部 -->
      <div class="login-header">
        <div class="logo-wrapper">
          <div class="logo-icon">
            <i class="el-icon-document"></i>
          </div>
          <div class="logo-text">
            <div class="logo-main">TaskHub</div>
            <div class="logo-subtitle">协作协议管理系统</div>
          </div>
        </div>
        <h2 class="login-title">欢迎回来</h2>
        <p class="login-subtitle">请输入您的账号和密码进行登录</p>
      </div>
      
      <!-- 登录表单 -->
      <el-form 
        :model="loginForm" 
        :rules="rules" 
        ref="loginFormRef" 
        class="login-form"
        label-position="top"
        :validate-on-rule-change="false"
      >
        <!-- 用户名输入 -->
        <el-form-item prop="username" class="form-item-wrapper">
          <div class="input-container">
            <div class="input-icon">
              <i class="el-icon-user"></i>
            </div>
            <el-input
              v-model="loginForm.username"
              placeholder="请输入用户名"
              class="custom-input"
              clearable
              :focus="isUsernameFocus"
              @focus="isUsernameFocus = true"
              @blur="isUsernameFocus = false"
            ></el-input>
          </div>
        </el-form-item>
        
        <!-- 密码输入 -->
        <el-form-item prop="password" class="form-item-wrapper">
          <div class="input-container">
            <div class="input-icon">
              <i class="el-icon-lock"></i>
            </div>
            <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="请输入密码"
              class="custom-input"
              show-password
              :focus="isPasswordFocus"
              @focus="isPasswordFocus = true"
              @blur="isPasswordFocus = false"
            ></el-input>
          </div>
        </el-form-item>
        
        <!-- 记住密码选项 -->
        <el-form-item class="remember-wrapper">
          <el-checkbox v-model="rememberPassword" class="remember-checkbox">
            记住密码
          </el-checkbox>
          <div class="forgot-password">
            <span class="forgot-text">忘记密码？</span>
          </div>
        </el-form-item>
        
        <!-- 登录按钮 -->
        <el-form-item class="login-button-wrapper">
          <el-button 
            type="primary" 
            @click="handleLogin" 
            :loading="loading" 
            class="login-button"
            :disabled="!isFormValid"
          >
            <span v-if="!loading">登录</span>
            <span v-else>登录中...</span>
          </el-button>
        </el-form-item>
        
        <!-- 测试账号信息 -->
        <div class="test-account-info">
          <div class="info-title">测试账号：</div>
          <div class="info-item">用户名：admin</div>
          <div class="info-item">密码：123456</div>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const loginFormRef = ref(null)
const loading = ref(false)
const rememberPassword = ref(false)
const isUsernameFocus = ref(false)
const isPasswordFocus = ref(false)

// 登录表单数据
const loginForm = ref({
  username: '',
  password: ''
})

// 表单验证规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ]
}

// 计算表单是否有效（非空检查）
const isFormValid = computed(() => {
  return loginForm.value.username.trim() && loginForm.value.password.trim()
})

// 处理登录
const handleLogin = async () => {
  try {
    // 验证表单
    await loginFormRef.value.validate()
    loading.value = true
    
    // 模拟API请求延迟
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    // 模拟登录验证
    if (loginForm.value.username === 'admin' && loginForm.value.password === '123456') {
      // 登录成功，存储用户信息
      localStorage.setItem('token', 'mock-token')
      localStorage.setItem('user', JSON.stringify({
        id: 1,
        username: 'admin',
        realName: '管理员'
      }))
      
      // 如果选择记住密码，存储表单信息（实际项目中不建议明文存储密码）
      if (rememberPassword.value) {
        localStorage.setItem('rememberedUsername', loginForm.value.username)
      } else {
        localStorage.removeItem('rememberedUsername')
      }
      
      // 登录成功提示
      ElMessage.success('登录成功，正在跳转...')
      
      // 跳转到首页
      setTimeout(() => {
        router.push('/')
      }, 1000)
    } else {
      // 登录失败提示
      ElMessage.error('用户名或密码错误')
    }
  } catch (error) {
    // 表单验证失败不提示错误，通过Element Plus默认提示
    if (!(error instanceof Error) || !error.message.includes('validate')) {
      console.error('登录失败', error)
      ElMessage.error('登录失败，请稍后重试')
    }
  } finally {
    loading.value = false
  }
}

// 检查是否有记住的用户名
const checkRememberedUser = () => {
  const rememberedUsername = localStorage.getItem('rememberedUsername')
  if (rememberedUsername) {
    loginForm.value.username = rememberedUsername
    rememberPassword.value = true
  }
}

// 组件挂载时执行
onMounted(() => {
  checkRememberedUser()
})
</script>

<style scoped>
/* 全局样式 */
.login-container {
  min-height: 100vh;
  height: 100vh;
  width: 100%;
  max-width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4eaf0 100%);
  padding: 20px;
  box-sizing: border-box;
  position: relative;
  overflow: hidden;
  margin: 0;
}

/* 背景装饰 */
.bg-decoration {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 0;
}

.bg-circle {
  position: absolute;
  border-radius: 50%;
  background: linear-gradient(135deg, rgba(64, 158, 255, 0.1), rgba(102, 126, 234, 0.1));
  filter: blur(80px);
  animation: float 10s ease-in-out infinite;
}

.circle-1 {
  width: 400px;
  height: 400px;
  top: -100px;
  left: -100px;
  animation-delay: 0s;
}

.circle-2 {
  width: 500px;
  height: 500px;
  bottom: -150px;
  right: -150px;
  animation-delay: 2s;
}

.circle-3 {
  width: 300px;
  height: 300px;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  animation-delay: 1s;
}

/* 登录卡片 */
.login-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 24px;
  width: 100%;
  width: 100%;
  max-width: 100%;
  box-shadow: 0 12px 48px rgba(0, 0, 0, 0.1);
  padding: 40px;
  box-sizing: border-box;
  position: relative;
  z-index: 1;
  transition: all 0.3s ease;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.login-card:hover {
  box-shadow: 0 16px 64px rgba(0, 0, 0, 0.15);
  transform: translateY(-4px);
}

/* 卡片头部 */
.login-header {
  text-align: center;
  margin-bottom: 32px;
}

.logo-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  margin-bottom: 20px;
}

.logo-icon {
  width: 52px;
  height: 52px;
  border-radius: 16px;
  background: linear-gradient(135deg, #409eff, #667eea);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 28px;
  box-shadow: 0 6px 20px rgba(64, 158, 255, 0.3);
  transition: all 0.3s ease;
}

.logo-text .logo-main {
  font-size: 24px;
  font-weight: 700;
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

.login-title {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 8px 0;
}

.login-subtitle {
  font-size: 14px;
  color: #606266;
  margin: 0;
}

/* 登录表单 */
.login-form {
  width: 100%;
}

.form-item-wrapper {
  margin-bottom: 24px;
}

.input-container {
  position: relative;
  display: flex;
  align-items: center;
  background: #f8f9fa;
  border-radius: 12px;
  padding: 0 16px;
  transition: all 0.3s ease;
  border: 2px solid transparent;
}

.form-item-wrapper.is-error .input-container {
  border-color: #f56c6c;
  background: #fef0f0;
}

.input-container:focus-within {
  border-color: #409eff;
  background: white;
  box-shadow: 0 0 0 4px rgba(64, 158, 255, 0.1);
}

.input-icon {
  color: #909399;
  font-size: 18px;
  margin-right: 12px;
}

.form-item-wrapper.is-error .input-icon,
.input-container:focus-within .input-icon {
  color: #409eff;
}

.custom-input {
  border: none;
  background: transparent;
  flex: 1;
  height: 48px;
  font-size: 15px;
  color: #303133;
}

.custom-input:focus {
  box-shadow: none;
  border: none;
}

.custom-input::placeholder {
  color: #c0c4cc;
}

/* 记住密码选项 */
.remember-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
}

.remember-checkbox {
  color: #606266;
  font-size: 14px;
}

.el-checkbox__input.is-checked .el-checkbox__inner {
  background-color: #409eff;
  border-color: #409eff;
}

.forgot-password {
  cursor: pointer;
}

.forgot-text {
  font-size: 14px;
  color: #409eff;
  transition: all 0.3s ease;
}

.forgot-text:hover {
  color: #667eea;
  text-decoration: underline;
}

/* 登录按钮 */
.login-button-wrapper {
  margin-bottom: 24px;
}

.login-button {
  width: 100%;
  height: 48px;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(135deg, #409eff, #667eea);
  border: none;
  transition: all 0.3s ease;
}

.login-button:hover:not(:disabled) {
  background: linear-gradient(135deg, #667eea, #764ba2);
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(64, 158, 255, 0.4);
}

.login-button:disabled {
  background: #c0c4cc;
  cursor: not-allowed;
}

/* 测试账号信息 */
.test-account-info {
  background: #f8f9fa;
  border-radius: 12px;
  padding: 16px;
  margin-top: 16px;
}

.info-title {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
}

.info-item {
  font-size: 13px;
  color: #606266;
  margin-bottom: 4px;
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

/* 响应式设计 */
@media (max-width: 768px) {
  .login-card {
    padding: 32px 24px;
    margin: 0;
  }
  
  .logo-icon {
    width: 48px;
    height: 48px;
    font-size: 24px;
  }
  
  .logo-text .logo-main {
    font-size: 20px;
  }
  
  .login-title {
    font-size: 20px;
  }
  
  .input-container {
    padding: 0 12px;
  }
  
  .custom-input {
    height: 44px;
    font-size: 14px;
  }
  
  .login-button {
    height: 44px;
    font-size: 15px;
  }
}

@media (max-width: 480px) {
  .login-container {
    padding: 12px;
  }
  
  .login-card {
    padding: 24px 20px;
    border-radius: 20px;
  }
  
  .circle-1,
  .circle-2,
  .circle-3 {
    filter: blur(60px);
  }
  
  .circle-1 {
    width: 300px;
    height: 300px;
  }
  
  .circle-2 {
    width: 400px;
    height: 400px;
  }
  
  .circle-3 {
    width: 200px;
    height: 200px;
  }
  
  .remember-wrapper {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
}
</style>