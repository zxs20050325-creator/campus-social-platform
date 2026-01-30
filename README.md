# 校园轻社交 + 资源置换平台

这是一个前后端分离的校园社交和资源置换平台，前端使用Vue 3 + Vite + TypeScript + Element Plus，后端使用Java Spring Boot 3 + JPA + PostgreSQL。

## 项目结构

```
campus-social-platform/
├── backend/                 # 后端Spring Boot项目
│   ├── src/                 # 后端源码
│   ├── pom.xml             # Maven依赖管理
│   ├── database_schema.sql # 数据库表结构
│   └── ...
├── frontend/               # 前端Vue 3项目
│   ├── src/                # 前端源码
│   ├── package.json        # npm依赖管理
│   └── ...
├── .gitignore             # Git忽略文件配置
└── README.md              # 项目说明文档
```

## 功能特性

- 用户注册/登录（JWT认证）
- 商品发布、浏览、搜索（资源置换功能）
- 帖子发布、浏览（校园社交功能）
- 评论系统
- 响应式UI设计

## 技术栈

**前端：**
- Vue 3
- Vite
- TypeScript
- Element Plus
- Axios
- Vue Router

**后端：**
- Java Spring Boot 3
- Spring Security
- JWT
- JPA
- PostgreSQL

## 快速开始

### 后端设置

1. 确保已安装Java 17+和Maven
2. 创建PostgreSQL数据库
3. 配置环境变量：
   ```bash
   DB_HOST=localhost
   DB_PORT=5432
   DB_NAME=your_database_name
   DB_USERNAME=your_username
   DB_PASSWORD=your_password
   JWT_SECRET=your_jwt_secret
   ```
4. 启动后端服务：
   ```bash
   cd backend
   mvn spring-boot:run
   ```

### 前端设置

1. 确保已安装Node.js和npm
2. 安装依赖：
   ```bash
   cd frontend
   npm install
   ```
3. 配置环境变量：
   ```bash
   # .env文件
   VITE_API_URL=http://localhost:8080/api
   ```
4. 启动前端开发服务器：
   ```bash
   npm run dev
   ```

## 部署

### 部署到Zeabur（后端）

1. 在Zeabur中创建新服务
2. 选择Java Spring Boot部署
3. 配置环境变量（参考上面的环境变量列表）

### 部署到Vercel（前端）

1. 在Vercel中导入前端项目
2. 设置构建命令：`npm run build`
3. 设置输出目录：`dist`
4. 设置环境变量：`VITE_API_URL`为后端API地址

## API接口

- 用户认证：`POST /api/auth/login`, `POST /api/auth/register`
- 商品管理：`GET /api/items`, `POST /api/items`, `PUT /api/items/{id}/status`
- 帖子管理：`GET /api/posts`, `POST /api/posts`

## License

MIT