<template>
  <div class="user-list-container">
    <!-- 现代化导航栏 -->
    <nav class="modern-nav">
      <div class="nav-content">
        <div class="logo-wrapper">
          <div class="logo-icon">
            <i class="el-icon-user"></i>
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
            <i class="el-icon-user header-icon"></i>
            用户管理
          </h1>
          <p class="page-subtitle">管理系统中的用户信息和权限</p>
        </div>
        <el-button 
          type="primary" 
          @click="createNewUser" 
          class="create-btn"
          icon="el-icon-plus"
        >
          创建用户
        </el-button>
      </div>
      
      <!-- 统计卡片 -->
      <div class="stats-cards">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-value">{{ totalUsers }}</div>
            <div class="stat-label">用户总数</div>
          </div>
        </el-card>
        <el-card class="stat-card success">
          <div class="stat-content">
            <div class="stat-value">{{ techDepartmentUsers }}</div>
            <div class="stat-label">技术部</div>
          </div>
        </el-card>
        <el-card class="stat-card info">
          <div class="stat-content">
            <div class="stat-value">{{ marketDepartmentUsers }}</div>
            <div class="stat-label">市场部</div>
          </div>
        </el-card>
        <el-card class="stat-card danger">
          <div class="stat-content">
            <div class="stat-value">{{ financeDepartmentUsers }}</div>
            <div class="stat-label">财务部</div>
          </div>
        </el-card>
      </div>
      
      <!-- 搜索区域 -->
      <div class="search-section">
        <el-card class="search-card">
          <el-form :inline="true" :model="searchForm" class="search-form">
            <el-form-item label="搜索" class="search-item">
              <el-input 
                v-model="searchQuery" 
                placeholder="用户名或真实姓名"
                class="search-input"
                clearable
                prefix-icon="el-icon-search"
              ></el-input>
            </el-form-item>
            <el-form-item label="部门" class="search-item">
              <el-select 
                v-model="departmentFilter" 
                placeholder="选择部门"
                class="search-select"
                clearable
              >
                <el-option label="技术部" value="技术部"></el-option>
                <el-option label="市场部" value="市场部"></el-option>
                <el-option label="财务部" value="财务部"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item class="search-item">
              <el-button 
                type="primary" 
                @click="search"
                icon="el-icon-search"
                class="search-btn"
              >
                搜索
              </el-button>
              <el-button 
                @click="resetSearch"
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
            <h3 class="table-title">用户列表</h3>
            <div class="table-actions">
              <el-button 
                type="info" 
                @click="exportUsers"
                icon="el-icon-download"
                size="small"
              >
                导出数据
              </el-button>
            </div>
          </div>
          
          <div class="table-container">
            <el-table 
              :data="filteredUsers" 
              style="width: 100%"
              border
              stripe
              fit
              highlight-current-row
              :header-cell-style="{ backgroundColor: '#fafafa', fontWeight: 'bold' }"
            >
              <el-table-column prop="id" label="ID" width="80" type="index"></el-table-column>
              <el-table-column prop="username" label="用户名" width="120" show-overflow-tooltip></el-table-column>
              <el-table-column prop="realName" label="真实姓名" width="120">
                <template #default="scope">
                  <div class="user-name-cell">
                    <span class="user-avatar-small">{{ getInitial(scope.row.realName) }}</span>
                    <span class="user-name">{{ scope.row.realName }}</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="phone" label="手机号" width="150"></el-table-column>
              <el-table-column prop="email" label="邮箱" width="200" show-overflow-tooltip></el-table-column>
              <el-table-column prop="department" label="部门" width="100">
                <template #default="scope">
                  <el-tag 
                    :type="getDepartmentType(scope.row.department)" 
                    size="small"
                    effect="dark"
                  >
                    {{ scope.row.department }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="position" label="职位" width="120" show-overflow-tooltip></el-table-column>
              <el-table-column prop="createdAt" label="创建时间" width="180"></el-table-column>
              <el-table-column label="操作" width="200" fixed="right">
                <template #default="scope">
                  <el-button 
                    size="small" 
                    type="primary"
                    icon="el-icon-view"
                    @click="viewUser(scope.row.id)"
                  >
                    查看
                  </el-button>
                  <el-button 
                    size="small" 
                    type="success"
                    icon="el-icon-edit"
                    @click="editUser(scope.row.id)"
                  >
                    编辑
                  </el-button>
                  <el-button 
                    size="small" 
                    type="danger"
                    icon="el-icon-delete"
                    @click="deleteUser(scope.row.id)"
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
              :total="total"
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
            <i class="el-icon-user"></i>
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
import { useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'UserListView',
  setup() {
    const router = useRouter()
    const users = ref([])
    const searchQuery = ref('')
    const departmentFilter = ref('')
    const currentPage = ref(1)
    const pageSize = ref(10)
    const total = ref(0)
    const activeIndex = ref('/users')
    const searchForm = reactive({
      query: '',
      department: ''
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
    
    // 统计数据计算
    const totalUsers = computed(() => users.value.length)
    const techDepartmentUsers = computed(() => 
      users.value.filter(user => user.department === '技术部').length
    )
    const marketDepartmentUsers = computed(() => 
      users.value.filter(user => user.department === '市场部').length
    )
    const financeDepartmentUsers = computed(() => 
      users.value.filter(user => user.department === '财务部').length
    )
    
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
    
    const handleSelect = (key) => {
      activeIndex.value = key
    }
    
    // 获取姓名首字母
    const getInitial = (name) => {
      return name ? name.charAt(0) : '?'
    }
    
    // 根据部门返回不同的标签类型
    const getDepartmentType = (department) => {
      switch (department) {
        case '技术部': return 'primary'
        case '市场部': return 'success'
        case '财务部': return 'warning'
        default: return 'info'
      }
    }

    const filteredUsers = computed(() => {
      let result = users.value
      
      if (searchQuery.value) {
        const query = searchQuery.value.toLowerCase()
        result = result.filter(user => 
          user.username.toLowerCase().includes(query) || 
          user.realName.toLowerCase().includes(query)
        )
      }
      
      if (departmentFilter.value) {
        result = result.filter(user => user.department === departmentFilter.value)
      }
      
      total.value = result.length
      return result.slice((currentPage.value - 1) * pageSize.value, currentPage.value * pageSize.value)
    })

    // 搜索方法
    const search = () => {
      currentPage.value = 1
    }

    // 重置搜索
    const resetSearch = () => {
      searchQuery.value = ''
      departmentFilter.value = ''
      currentPage.value = 1
    }

    // 导出用户数据
    const exportUsers = () => {
      ElMessage.success('导出功能正在开发中')
      // 实际项目中可以实现Excel导出功能
    }

    const handleSizeChange = (val) => {
      pageSize.value = val
      currentPage.value = 1
    }

    const handleCurrentChange = (val) => {
      currentPage.value = val
    }

    const viewUser = (id) => {
      // 暂时跳转到编辑页面，后续可以实现专门的详情页面
      router.push(`/users/edit/${id}`)
    }

    const editUser = (id) => {
      router.push(`/users/edit/${id}`)
    }

    const deleteUser = async (id) => {
      try {
        await ElMessageBox.confirm('确定要删除该用户吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        // 调用API删除用户
        await axios.delete(`/api/users/${id}`)
        
        // 更新本地数据
        users.value = users.value.filter(user => user.id !== id)
        
        ElMessage.success('删除成功')
      } catch (error) {
        if (error === 'cancel') {
          ElMessage.info('已取消删除')
        } else {
          console.error('删除用户失败', error)
          ElMessage.error('删除失败，请重试')
        }
      }
    }

    const createNewUser = () => {
      router.push('/users/new')
    }

    const fetchUsers = async () => {
      try {
        // 尝试从后端获取数据
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
            phone: '13800138000',
            email: 'admin@example.com',
            department: '技术部',
            position: '管理员',
            createdAt: '2024-01-01 10:00:00'
          },
          {
            id: 2,
            username: 'developer',
            realName: '开发者',
            phone: '13900139000',
            email: 'developer@example.com',
            department: '技术部',
            position: '开发工程师',
            createdAt: '2024-01-02 14:30:00'
          },
          {
            id: 3,
            username: 'designer',
            realName: '设计师',
            phone: '13700137000',
            email: 'designer@example.com',
            department: '技术部',
            position: 'UI设计师',
            createdAt: '2024-01-03 09:15:00'
          },
          {
            id: 4,
            username: 'marketer',
            realName: '市场专员',
            phone: '13600136000',
            email: 'marketer@example.com',
            department: '市场部',
            position: '市场经理',
            createdAt: '2024-01-04 15:20:00'
          },
          {
            id: 5,
            username: 'accountant',
            realName: '财务会计',
            phone: '13500135000',
            email: 'accountant@example.com',
            department: '财务部',
            position: '财务主管',
            createdAt: '2024-01-05 11:30:00'
          }
        ]
      }
    }

    onMounted(() => {
      fetchUsers()
      updateActiveIndex()
    })
    
    // 监听路由变化
    router.afterEach(() => {
      updateActiveIndex()
    })

    return {
      users,
      searchQuery,
      departmentFilter,
      searchForm,
      currentPage,
      pageSize,
      total,
      activeIndex,
      userName,
      totalUsers,
      techDepartmentUsers,
      marketDepartmentUsers,
      financeDepartmentUsers,
      filteredUsers,
      getInitial,
      getDepartmentType,
      handleSizeChange,
      handleCurrentChange,
      viewUser,
      editUser,
      deleteUser,
      createNewUser,
      handleLogout,
      handleSelect,
      search,
      resetSearch,
      exportUsers
    }
  }
}
</script>

<style scoped>
/* 全局样式 */
.user-list-container {
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
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  width: 100%;
  z-index: 1000;
  transition: all 0.3s ease;
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
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
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

.stat-card.success::before {
  background: linear-gradient(135deg, #67c23a, #85ce61);
}

.stat-card.info::before {
  background: linear-gradient(135deg, #909399, #c0c4cc);
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

.stat-card.success .stat-value {
  color: #67c23a;
}

.stat-card.info .stat-value {
  color: #909399;
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
  min-width: 200px;
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

/* 用户姓名单元格样式 */
.user-name-cell {
  display: flex;
  align-items: center;
  gap: 8px;
}

.user-avatar-small {
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

.user-name {
  font-weight: 500;
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
  .stats-cards {
    grid-template-columns: repeat(2, 1fr);
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
    grid-template-columns: 1fr;
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
  
  .el-button--small {
    padding: 6px 8px;
    font-size: 12px;
  }
  
  .user-name-cell {
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
</style>