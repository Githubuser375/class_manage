<template>
  <el-container class="main-layout">
    <el-aside width="200px">
      <el-menu :default-active="$route.path" :router="true" class="el-menu-vertical-demo">
        <h3 class="menu-title">课程管理系统</h3>
        <el-menu-item index="/schedule">
          <el-icon><Calendar /></el-icon>
          <span>排课日历</span>
        </el-menu-item>
        <el-menu-item index="/course">
          <el-icon><List /></el-icon>
          <span>课程管理</span>
        </el-menu-item>
        <el-menu-item index="/employee">
          <el-icon><User /></el-icon>
          <span>员工管理</span>
        </el-menu-item>
        <el-menu-item index="/category">
          <el-icon><Setting /></el-icon>
          <span>分类管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<style>
html, body, #app, .main-layout {
  height: 100%;
  margin: 0;
  padding: 0;
}
.menu-title {
  height: 60px;
  line-height: 60px;
  text-align: center;
  color: #303133;
  font-size: 16px;
  border-bottom: 1px solid #ebeef5;
}
</style>
