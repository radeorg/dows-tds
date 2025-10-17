import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'home',
    component: () => import('../views/HomeView.vue')
  },
  {
    path: '/agreements',
    name: 'agreements',
    component: () => import('../views/AgreementListView.vue')
  },
  {
    path: '/agreement/:id',
    name: 'agreement-detail',
    component: () => import('../views/AgreementDetailView.vue')
  },
  {
    path: '/agreements/new',
    name: 'agreement-create',
    component: () => import('../views/AgreementEditView.vue')
  },
  {
    path: '/agreements/edit/:id',
    name: 'agreement-edit',
    component: () => import('../views/AgreementEditView.vue')
  },
  {
    path: '/tasks',
    name: 'tasks',
    component: () => import('../views/TaskListView.vue')
  },
  {
    path: '/tasks/:id',
    name: 'task-detail',
    component: () => import('../views/TaskDetailView.vue')
  },
  {
    path: '/tasks/new',
    name: 'task-create',
    component: () => import('../views/TaskEditView.vue')
  },
  {
    path: '/tasks/edit/:id',
    name: 'task-edit',
    component: () => import('../views/TaskEditView.vue')
  },
  {
    path: '/users',
    name: 'users',
    component: () => import('../views/UserListView.vue')
  },
  {
    path: '/users/new',
    name: 'user-create',
    component: () => import('../views/UserEditView.vue')
  },
  {
    path: '/users/edit/:id',
    name: 'user-edit',
    component: () => import('../views/UserEditView.vue')
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/LoginView.vue')
  },
  // 通配符路由，处理所有未匹配的路径
  {
    path: '/:pathMatch(.*)*',
    redirect: '/' // 重定向到首页
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

export default router