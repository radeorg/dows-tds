<template>
  <router-view></router-view>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html,
body,
#app {
  height: 100%;
  width: 100%;
  overflow-x: hidden;
}

body {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
}

/* 全局主内容区域样式 */
.main-content {
  padding: 24px;
  width: 100%;
  max-width: 100%;
  min-height: calc(100vh - 60px); /* 减去头部导航的高度 */
}

/* 确保所有卡片组件都是全屏宽度 */
.el-card {
  width: 100%;
  max-width: 100%;
}

/* 确保Element Plus的布局容器是全屏的 */
.el-container {
  width: 100%;
  height: 100%;
}

/* 确保主内容区域是全屏的 */
.el-main {
  padding: 0;
  width: 100%;
  max-width: 100%;
  overflow-x: hidden;
}
</style>
