# 校园轻社交平台 - 前端

这是校园轻社交 + 资源置换平台的前端，使用Vue 3 + Vite + TypeScript + Element Plus。

## 技术栈

- Vue 3
- Vite
- TypeScript
- Element Plus
- Vue Router
- Axios

## 功能模块

- 用户登录/注册
- 资源市场（商品展示与交易）
- 校园社交（帖子发布与浏览）
- 个人中心（管理个人信息和发布内容）

## 项目结构

```
src/
├── components/     # 通用组件
├── views/          # 页面组件
├── router/         # 路由配置
├── utils/          # 工具函数（如axios封装）
├── assets/         # 静态资源
└── App.vue         # 根组件
```

## 环境配置

创建 `.env` 文件配置API地址：

```bash
# .env
VITE_API_URL=http://localhost:8080/api
```

## 本地运行

1. 确保已安装Node.js和npm
2. 安装依赖：
   ```bash
   npm install
   ```
3. 启动开发服务器：
   ```bash
   npm run dev
   ```

应用将在`http://localhost:3000`启动。

## 部署到Vercel

项目已配置`vercel.json`，可以直接部署到Vercel：

1. 在Vercel中导入项目
2. 设置构建命令为`npm run build`
3. 输出目录为`dist`

## API调用

项目使用Axios封装进行API请求，自动携带JWT Token：
- 登录后Token保存在localStorage
- 请求拦截器自动添加Authorization头部
- 响应拦截器处理401错误并跳转到登录页