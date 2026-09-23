<template>
  <router-view></router-view>
</template>

<style>
/* 引入 Element Plus 暗黑模式变量 */
@import 'element-plus/theme-chalk/dark/css-vars.css';

/* 基础重置 */
html,
body,
#app {
  margin: 0;
  padding: 0;
  height: 100%;
  width: 100%;
}

/* 默认变量（亮色模式） */
:root {
  --bg-color: #ffffff;
  --text-color: #0f172a;
  --text-sub-color: #475569;
  --card-bg: #fafafa;
  --card-border: rgba(0, 0, 0, 0.05);
  --header-bg: rgba(255, 255, 255, 0.92);
  --section-bg: #f1eeee;
  --brand-orange: #f2590b;
}
</style>
