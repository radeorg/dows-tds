<template>
  <div class="agreement-detail-container">
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
            <i class="el-icon-document header-icon"></i>
            协议详情
          </h1>
          <p class="page-subtitle">查看和管理协议的详细内容</p>
        </div>
        <div class="header-actions">
          <el-button 
            @click="backToList" 
            icon="el-icon-arrow-left"
            class="back-btn"
          >
            返回列表
          </el-button>
          <el-button 
            type="primary" 
            @click="editAgreement"
            icon="el-icon-edit"
            class="edit-btn"
          >
            编辑协议
          </el-button>
          <el-button 
            @click="printAgreement"
            icon="el-icon-printer"
            class="print-btn"
          >
            打印
          </el-button>
        </div>
      </div>

      <!-- 协议内容区域 -->
      <el-card 
        v-loading="loading" 
        class="agreement-card"
        shadow="hover"
        border
        element-loading-text="正在加载协议内容..."
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgba(255, 255, 255, 0.8)"
      >
        <!-- 协议头部信息 -->
        <div class="agreement-header">
          <h1 class="agreement-title">{{ agreement?.title || '协议标题' }}</h1>
          
          <!-- 协议元信息 -->
          <div class="agreement-meta">
            <div class="meta-grid">
              <div class="meta-item">
                <div class="meta-icon">
                  <i class="el-icon-tickets"></i>
                </div>
                <div class="meta-content">
                  <div class="meta-label">状态</div>
                  <div class="meta-value">
                    <el-tag 
                      :type="getStatusType(agreement?.status)" 
                      effect="dark"
                      class="status-tag"
                    >
                      {{ getStatusText(agreement?.status) }}
                    </el-tag>
                  </div>
                </div>
              </div>
              
              <div class="meta-item">
                <div class="meta-icon">
                  <i class="el-icon-document-copy"></i>
                </div>
                <div class="meta-content">
                  <div class="meta-label">版本</div>
                  <div class="meta-value version-value">v{{ agreement?.version || '1.0' }}</div>
                </div>
              </div>
              
              <div class="meta-item">
                <div class="meta-icon">
                  <i class="el-icon-time"></i>
                </div>
                <div class="meta-content">
                  <div class="meta-label">创建时间</div>
                  <div class="meta-value">{{ formatDate(agreement?.createdAt) }}</div>
                </div>
              </div>
              
              <div class="meta-item">
                <div class="meta-icon">
                  <i class="el-icon-user"></i>
                </div>
                <div class="meta-content">
                  <div class="meta-label">创建人</div>
                  <div class="meta-value">
                    <div class="creator-info">
                      <span class="creator-avatar">{{ getInitial(agreement?.creator?.realName) }}</span>
                      <span class="creator-name">{{ agreement?.creator?.realName || '-' }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 协议内容 -->
        <div class="agreement-content">
          <div v-html="formatContent(agreement?.content)" class="content-display"></div>
        </div>
      </el-card>
      
      <!-- 错误状态 -->
      <div v-if="!loading && !agreement" class="error-section">
        <el-alert
          title="错误"
          type="error"
          description="无法加载协议信息，请稍后重试"
          show-icon
          class="error-alert"
        ></el-alert>
        <div class="error-actions">
          <el-button type="primary" @click="backToList">返回列表</el-button>
        </div>
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

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()
const agreement = ref(null)
const loading = ref(true)
const activeIndex = ref('/agreements')

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

// 获取姓名首字母
const getInitial = (name) => {
  return name ? name.charAt(0) : '?'
}

// 获取状态类型
const getStatusType = (status) => {
  switch (status) {
    case 'ACTIVE': return 'success'
    case 'DRAFT': return 'info'
    case 'INACTIVE': return 'danger'
    default: return 'info'
  }
}

// 获取状态文本
const getStatusText = (status) => {
  switch (status) {
    case 'ACTIVE': return '有效'
    case 'DRAFT': return '草稿'
    case 'INACTIVE': return '无效'
    default: return status || '-'
  }
}

// 格式化日期时间
const formatDate = (dateString) => {
  if (!dateString) return '-'
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 格式化协议内容
const formatContent = (content) => {
  // 将Markdown转换为HTML显示
  if (!content) return '<p class="no-content">暂无协议内容</p>'
  
  return content
    .replace(/^## (.*$)/gm, '<h2 class="section-title">$1</h2>')
    .replace(/^### (.*$)/gm, '<h3 class="subsection-title">$1</h3>')
    .replace(/^第([一二三四五六七八九十百千]+)条 (.*$)/gm, '<div class="article-clause"><span class="clause-number">第$1条</span><span class="clause-content">$2</span></div>')
    .replace(/\n/g, '<br>')
}

// 编辑协议
const editAgreement = () => {
  router.push(`/agreements/edit/${route.params.id}`)
}

// 打印协议
const printAgreement = () => {
  window.print()
}

// 返回列表
const backToList = () => {
  router.push('/agreements')
}

// 获取协议详情
const fetchAgreementDetail = async () => {
  try {
    loading.value = true
    const id = route.params.id
    // 模拟数据，实际应该从后端获取
    agreement.value = {
      id: id,
      title: '兼职技术人员项目协作协议',
      version: '1.0',
      status: 'ACTIVE',
      creator: { realName: '管理员' },
      createdAt: '2024-01-01T10:00:00',
      content: '## 第一章 总则\n\n第一条 为规范兼职技术人员的工作流程，明确双方权利义务，特制定本协议。\n\n第二条 本协议适用于所有通过任务管理系统参与项目的兼职技术人员。\n\n## 第二章 任务领取与开发流程\n\n第一条 兼职技术人员通过任务管理系统领取任务。\n\n第二条 任务领取后，技术人员应在24小时内确认并制定工作计划。\n\n第三条 开发过程中应定期提交进度报告，确保项目按时推进。\n\n## 第三章 报酬计算与支付\n\n第一条 报酬计算以任务复杂度和完成质量为依据。\n\n第二条 任务完成并通过验收后，系统自动计算并生成报酬清单。\n\n第三条 报酬支付周期为每月15日和30日，节假日顺延。\n\n## 第四章 时间、风险与质量管理\n\n第一条 兼职技术人员应在规定时间内完成任务。\n\n第二条 对于可能影响交付时间的风险，应提前48小时向项目经理报备。\n\n第三条 所有交付物必须通过代码审核和质量测试。'  
    }
  } catch (error) {
    console.error('获取协议详情失败', error)
    ElMessage.error('获取协议详情失败，请稍后重试')
  } finally {
    loading.value = false
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

// 监听路由变化
router.afterEach(() => {
  updateActiveIndex()
})

onMounted(() => {
  fetchAgreementDetail()
  updateActiveIndex()
})
</script>

<style scoped>
/* 全局样式 */
.agreement-detail-container {
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

.print-btn {
  border-radius: 8px;
  transition: all 0.3s ease;
}

.print-btn:hover {
  color: #67c23a;
  border-color: #67c23a;
}

/* 协议卡片 */
.agreement-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  border: none;
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: all 0.3s ease;
}

.agreement-card:hover {
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
}

/* 协议头部 */
.agreement-header {
  padding: 32px;
  border-bottom: 1px solid #f0f2f5;
  text-align: center;
}

.agreement-title {
  font-size: 32px;
  font-weight: 700;
  color: #303133;
  margin: 0 0 24px 0;
  line-height: 1.4;
  background: linear-gradient(135deg, #409eff, #667eea);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* 协议元信息 */
.agreement-meta {
  margin-top: 24px;
}

.meta-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 24px;
  width: 100%;
  max-width: 100%;
  margin: 0 auto;
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
  text-align: left;
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

.status-tag {
  padding: 6px 16px;
  font-size: 14px;
  border-radius: 6px;
}

.version-value {
  color: #409eff;
  font-weight: 700;
}

.creator-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.creator-avatar {
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

.creator-name {
  font-weight: 500;
}

/* 协议内容 */
.agreement-content {
  padding: 32px;
}

.content-display {
  font-size: 16px;
  line-height: 1.8;
  color: #303133;
}

/* 内容样式 */
.content-display h2.section-title {
  font-size: 24px;
  font-weight: 700;
  color: #409eff;
  margin-top: 40px;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 2px solid #e6f7ff;
}

.content-display h3.subsection-title {
  font-size: 20px;
  font-weight: 600;
  color: #667eea;
  margin-top: 30px;
  margin-bottom: 16px;
}

.content-display .article-clause {
  margin-bottom: 16px;
  padding-left: 12px;
  border-left: 4px solid #409eff;
}

.content-display .clause-number {
  font-weight: 700;
  color: #409eff;
  margin-right: 8px;
}

.content-display .clause-content {
  color: #606266;
  line-height: 1.8;
}

.content-display .no-content {
  text-align: center;
  color: #909399;
  font-style: italic;
  padding: 40px;
}

/* 错误状态 */
.error-section {
  text-align: center;
  padding: 48px 24px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.1);
}

.error-alert {
  margin-bottom: 24px;
}

.error-actions {
  margin-top: 24px;
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
  .edit-btn,
  .print-btn {
    flex: 1;
  }
  
  .agreement-title {
    font-size: 28px;
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
  
  .agreement-header {
    padding: 24px;
  }
  
  .agreement-title {
    font-size: 24px;
  }
  
  .meta-grid {
    grid-template-columns: 1fr;
    gap: 16px;
  }
  
  .agreement-content {
    padding: 20px;
  }
  
  .content-display h2.section-title {
    font-size: 20px;
  }
  
  .content-display h3.subsection-title {
    font-size: 18px;
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
  
  .agreement-header {
    padding: 16px;
  }
  
  .agreement-title {
    font-size: 22px;
  }
  
  .meta-item {
    padding: 12px;
  }
  
  .meta-icon {
    width: 36px;
    height: 36px;
    font-size: 16px;
  }
  
  .agreement-content {
    padding: 16px;
  }
  
  .content-display {
    font-size: 14px;
  }
}

/* 打印样式 */
@media print {
  .modern-nav,
  .page-header,
  .modern-footer {
    display: none !important;
  }
  
  .main-content {
    padding: 0;
  }
  
  .agreement-card {
    box-shadow: none;
    border: none;
    background: white;
  }
  
  .agreement-header {
    border-bottom: 1px solid #ddd;
  }
}

</style>