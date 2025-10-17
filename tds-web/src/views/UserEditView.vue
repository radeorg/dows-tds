<template>
  <el-container class="user-edit-container">
    <el-header>
      <div class="logo">用户管理系统</div>
      <el-menu
        :default-active="activeIndex"
        class="el-menu-demo"
        mode="horizontal"
        background-color="#001529"
        text-color="#fff"
        active-text-color="#ffd04b"
        router
        @select="handleSelect"
      >
        <el-menu-item index="/">首页</el-menu-item>
        <el-menu-item index="/agreements">协议管理</el-menu-item>
        <el-menu-item index="/users">用户管理</el-menu-item>
        <el-menu-item index="logout" @click="handleLogout">退出登录</el-menu-item>
      </el-menu>
    </el-header>
    
    <el-main class="main-content">
      <div class="page-header">
        <h1>{{ isEdit ? '编辑用户' : '创建用户' }}</h1>
      </div>
      
      <el-card class="user-form-card">
        <el-form
          ref="userFormRef"
          :model="userForm"
          :rules="rules"
          label-width="120px"
        >
          <el-form-item label="用户名" prop="username">
            <el-input v-model="userForm.username" placeholder="请输入用户名"></el-input>
          </el-form-item>
          
          <el-form-item v-if="!isEdit" label="密码" prop="password">
            <el-input v-model="userForm.password" type="password" placeholder="请输入密码"></el-input>
          </el-form-item>
          
          <el-form-item label="真实姓名" prop="realName">
            <el-input v-model="userForm.realName" placeholder="请输入真实姓名"></el-input>
          </el-form-item>
          
          <el-form-item label="手机号码" prop="phone">
            <el-input v-model="userForm.phone" placeholder="请输入手机号码"></el-input>
          </el-form-item>
          
          <el-form-item label="电子邮箱" prop="email">
            <el-input v-model="userForm.email" type="email" placeholder="请输入电子邮箱"></el-input>
          </el-form-item>
          
          <el-form-item label="所属部门" prop="department">
            <el-input v-model="userForm.department" placeholder="请输入所属部门"></el-input>
          </el-form-item>
          
          <el-form-item label="职位" prop="position">
            <el-input v-model="userForm.position" placeholder="请输入职位"></el-input>
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" @click="submitForm" :loading="loading">提交</el-button>
            <el-button @click="cancelForm">取消</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </el-main>
  </el-container>
</template>

<script setup>
import { ref, reactive, onMounted, computed, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const route = useRoute()
const userFormRef = ref()
const loading = ref(false)
const activeIndex = ref('/users')

// 判断是编辑还是创建
const isEdit = computed(() => {
  return route.params.id !== undefined
})

// 用户表单数据
const userForm = reactive({
  id: '',
  username: '',
  password: '',
  realName: '',
  phone: '',
  email: '',
  department: '',
  position: ''
})

// 表单验证规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于 6 个字符', trigger: 'blur' }
  ],
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号码', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入电子邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的电子邮箱格式', trigger: 'blur' }
  ],
  department: [
    { required: true, message: '请输入所属部门', trigger: 'blur' }
  ],
  position: [
    { required: true, message: '请输入职位', trigger: 'blur' }
  ]
}

// 获取用户数据
const fetchUser = async () => {
  if (!isEdit.value) return
  
  try {
    loading.value = true
    const response = await axios.get(`/api/users/${route.params.id}`)
    Object.assign(userForm, response.data)
  } catch (error) {
    console.error('获取用户信息失败', error)
    ElMessage.error('获取用户信息失败，请重试')
  } finally {
    loading.value = false
  }
}

// 提交表单
const submitForm = async () => {
  await userFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        loading.value = true
        
        if (isEdit.value) {
          // 更新用户
          await axios.put(`/api/users/${userForm.id}`, {
            ...userForm,
            // 如果没有输入密码，则不更新密码
            password: userForm.password ? userForm.password : undefined
          })
          ElMessage.success('用户更新成功')
        } else {
          // 创建用户
          await axios.post('/api/users', userForm)
          ElMessage.success('用户创建成功')
        }
        
        // 返回到用户列表
        router.push('/users')
      } catch (error) {
        console.error('保存用户失败', error)
        ElMessage.error('保存失败，请重试')
      } finally {
        loading.value = false
      }
    } else {
      console.log('表单验证失败')
      return false
    }
  })
}

// 取消表单
const cancelForm = () => {
  router.push('/users')
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
  activeIndex.value = to.path
}

// 监听路由变化
router.afterEach((to, from) => {
  updateActiveIndex(to)
})

onMounted(() => {
  fetchUser()
})
</script>

<style scoped>
.user-edit-container {
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

  .user-form-card {
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
  .user-edit-container {
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
  
  .user-form-card {
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
  .el-select {
    width: 100%;
    max-width: 100%;
  }
}
</style>