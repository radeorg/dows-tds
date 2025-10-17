# 协作协议管理系统 - 功能需求文档

## 1. 产品概述

TaskHub协作协议管理系统是一个专为兼职技术人员项目协作设计的现代化管理平台，旨在简化协议管理、任务分配与跟踪、用户管理等核心业务流程。系统提供直观的用户界面，支持实时数据更新和多角色协作。

## 2. 系统架构

### 2.1 技术栈
- 前端：Vue 3 + Element Plus
- 后端：RESTful API服务
- 数据库：关系型数据库

### 2.2 核心模块
- 首页（HomeView）：提供系统概览和快速访问入口
- 任务管理（Tasks）：任务的创建、编辑、查看和状态跟踪
- 协议管理（Agreements）：协议的创建、编辑、查看和状态管理
- 用户管理（Users）：用户信息管理和权限控制
- 认证与授权：用户登录、登出和权限验证

## 3. 功能需求

### 3.1 首页（HomeView）

#### 3.1.1 核心功能
- 展示系统概述信息
- 提供创建新协议的快捷入口
- 提供浏览协议列表的快捷入口
- 显示统计数据（协议总数等）

#### 3.1.2 界面元素
- 导航栏：包含系统logo、主导航项（首页、协议管理、任务管理、用户管理）和用户操作区
- 英雄区域：系统标题、副标题和快速操作按钮
- 统计数据卡片：显示关键业务指标

### 3.2 任务管理模块

#### 3.2.1 任务列表（TaskListView）

##### 3.2.1.1 核心功能
- 展示任务列表，支持分页显示
- 提供任务搜索功能（按任务名称）
- 提供任务过滤功能（按状态、优先级）
- 支持任务查看、编辑、删除操作
- 支持任务数据导出功能

##### 3.2.1.2 数据获取
- 通过`/api/tasks`接口获取任务列表
- 支持查询参数：name（任务名称）、status（状态）、priority（优先级）
- 数据适配：将后端API返回的数据转换为前端显示所需格式

##### 3.2.1.3 数据模型映射
| 后端字段 | 前端字段 | 转换说明 |
|---------|---------|--------|
| id | id | 直接映射 |
| name | title | 字段名转换 |
| description | description | 直接映射，提供默认值空字符串 |
| responsiblePerson.id | assigneeId | 嵌套对象字段提取 |
| responsiblePerson.realName | assigneeName | 嵌套对象字段提取，默认"未分配" |
| status | status | 转换为小写适配前端 |
| priority | priority | 转换为小写适配前端 |
| estimatedTime | duration | 格式化为"X天"，默认"未知" |
| reward | amount | 转换为字符串，默认"0" |
| paymentStatus | paymentStatus | 转换为小写适配前端 |
| deadline | dueDate | 直接映射 |
| createdAt | createdAt | 直接映射 |
| updatedAt | updateTime | 直接映射，或使用createdAt作为默认值 |
| progress | progress | 直接映射，默认0 |
| - | isOverdue | 计算字段：判断是否过期 |

#### 3.2.2 任务详情（TaskDetailView）

##### 3.2.2.1 核心功能
- 展示任务详细信息
- 支持查看任务评论
- 提供返回列表、编辑任务的操作入口

##### 3.2.2.2 数据获取
- 通过`/api/tasks/{id}`接口获取任务详情
- 通过`/api/comments/task/{id}`接口获取任务评论

#### 3.2.3 任务创建/编辑（TaskEditView）

##### 3.2.3.1 核心功能
- 创建新任务或编辑现有任务
- 表单验证
- 提交任务数据到后端

##### 3.2.3.2 表单字段
- 任务名称（title）：必填，文本输入
- 任务描述（description）：文本区域输入
- 负责人（assigneeId）：下拉选择，从用户列表中选择
- 任务状态（status）：单选框，可选值：pending、in_progress、completed
- 优先级（priority）：下拉选择，可选值：low、medium、high
- 截止日期（dueDate）：日期选择器
- 完成进度（progress）：进度条和数字输入，仅在状态为进行中时显示

### 3.3 协议管理模块

#### 3.3.1 协议列表（AgreementListView）

##### 3.3.1.1 核心功能
- 展示协议列表
- 提供协议搜索功能（按协议标题）
- 提供协议过滤功能（按状态）
- 支持创建新协议操作

##### 3.3.1.2 统计功能
- 显示协议总数
- 显示有效协议数量
- 显示草稿协议数量
- 显示无效协议数量

#### 3.3.2 协议详情（AgreementDetailView）

##### 3.3.2.1 核心功能
- 展示协议详细内容
- 显示协议元信息（状态、版本、时间等）
- 提供返回列表、编辑协议、打印协议的操作入口

##### 3.3.2.2 状态管理
- 支持协议状态显示（草稿、有效、无效）
- 不同状态使用不同颜色标签区分

### 3.4 用户管理模块

#### 3.4.1 核心功能
- 用户信息管理
- 用户列表展示
- 用户创建和编辑

### 3.5 认证与授权

#### 3.5.1 核心功能
- 用户登录
- 用户登出
- 会话管理
- 权限控制

#### 3.5.2 登出功能
- 确认对话框
- 清除本地存储（user、token）
- 重定向到登录页面

## 4. 数据模型

### 4.1 任务（Task）
- id: 唯一标识符
- name: 任务名称
- description: 任务描述
- status: 任务状态（NOT_STARTED, IN_PROGRESS, REVIEWING, COMPLETED）
- priority: 优先级（LOW, MEDIUM, HIGH, URGENT）
- responsiblePerson: 负责人信息（嵌套对象）
- estimatedTime: 预估时间
- reward: 奖励金额
- paymentStatus: 支付状态（NOT_PAID, PARTIALLY_PAID, PAID）
- deadline: 截止日期
- createdAt: 创建时间
- updatedAt: 更新时间
- progress: 完成进度

### 4.2 协议（Agreement）
- id: 唯一标识符
- title: 协议标题
- content: 协议内容
- status: 协议状态（DRAFT, ACTIVE, INACTIVE）
- version: 协议版本
- createdAt: 创建时间
- updatedAt: 更新时间

### 4.3 用户（User）
- id: 唯一标识符
- realName: 真实姓名
- department: 部门

## 5. 界面原型

### 5.1 通用组件
- 导航栏：包含logo、导航项和用户菜单
- 页面头部：包含页面标题、副标题和操作按钮
- 统计卡片：显示关键业务指标
- 搜索区域：包含搜索框和过滤条件
- 表格：展示数据列表，支持分页

### 5.2 样式设计
- 现代化UI设计
- 响应式布局
- 动画效果（卡片悬停、过渡动画等）
- 状态颜色标识（成功、警告、错误等）

## 6. API接口

### 6.1 任务相关API
- GET /api/tasks - 获取任务列表
- GET /api/tasks/{id} - 获取任务详情
- POST /api/tasks - 创建新任务
- PUT /api/tasks/{id} - 更新任务
- DELETE /api/tasks/{id} - 删除任务
- GET /api/comments/task/{id} - 获取任务评论

### 6.2 协议相关API
- GET /api/agreements - 获取协议列表
- GET /api/agreements/{id} - 获取协议详情
- POST /api/agreements - 创建新协议
- PUT /api/agreements/{id} - 更新协议
- DELETE /api/agreements/{id} - 删除协议

### 6.3 用户相关API
- GET /api/users - 获取用户列表
- GET /api/users/{id} - 获取用户详情
- POST /api/users - 创建新用户
- PUT /api/users/{id} - 更新用户
- DELETE /api/users/{id} - 删除用户

## 7. 错误处理

### 7.1 数据获取错误
- 显示友好的错误提示
- 记录错误日志
- 提供重试机制（可选）

### 7.2 表单验证错误
- 实时验证
- 错误提示显示在对应字段旁
- 提交时整体验证

## 8. 性能优化

### 8.1 路由懒加载
- 使用Vue的路由懒加载功能，提高首屏加载速度

### 8.2 数据缓存
- 适当使用本地存储缓存数据，减少API调用

### 8.3 分页加载
- 采用分页加载大量数据，提高性能和用户体验

## 9. 部署与维护

### 9.1 环境要求
- Node.js 14+
- npm 6+

### 9.2 构建命令
- 开发环境：npm run dev
- 生产环境构建：npm run build

## 10. 附录

### 10.1 状态码映射
| 后端状态 | 前端状态 |
|---------|--------|
| NOT_STARTED | pending |
| IN_PROGRESS | in_progress |
| REVIEWING | reviewing |
| COMPLETED | completed |

### 10.2 优先级映射
| 后端优先级 | 前端优先级 |
|----------|----------|
| LOW | low |
| MEDIUM | medium |
| HIGH | high |
| URGENT | urgent |

### 10.3 支付状态映射
| 后端支付状态 | 前端支付状态 |
|------------|------------|
| NOT_PAID | unpaid |
| PARTIALLY_PAID | partially_paid |
| PAID | paid |