import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';

// FullCalendar 的样式会在组件内部处理，这里只处理 Element Plus 及其图标
import * as ElementPlusIconsVue from '@element-plus/icons-vue';

const app = createApp(App);

// 注册所有 Element Plus 图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}

app.use(router);
app.use(ElementPlus);

// ！！！重要提示：Axios 基础路径配置 ！！！
// 请确保你的 axios 实例配置了 baseURL 或通过代理指向了后端 http://localhost:8080/api

app.mount('#app');
