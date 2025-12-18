import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { fileURLToPath, URL } from 'node:url'

export default defineConfig({
  plugins: [vue()],

  // ==========================================
  // 核心修复：配置代理
  // ==========================================
  server: {
    proxy: {
      // 捕获所有以 /api 开头的请求
      '/api': {
        // 目标地址：您的后端运行在 8080 端口
        target: 'http://localhost:8080',

        // 允许跨域请求（必须）
        changeOrigin: true,

        // 【重要】重写路径：将请求中的 /api/ 替换为空，
        // 以避免出现 /api/api/ 错误。
        // 因为您的后端 application.yml 已经有 context-path: /api
        // 所以这里不需要 pathRewrite
        // 但是为了兼容性，我们确保前端发送的请求就是 /api/xxx
        // 示例：前端 /api/class -> 后端 http://localhost:8080/api/class
        // rewrite: (path) => path.replace(/^\/api/, '/api'),
        // 鉴于您的后端配置 /api 已经包含了 context-path，通常直接转发即可
      }
    }
  }
  // ==========================================
});
