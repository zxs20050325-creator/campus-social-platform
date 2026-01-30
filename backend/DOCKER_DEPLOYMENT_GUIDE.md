# Docker部署指南

## 概述

使用Docker容器化部署是部署Spring Boot应用的最佳实践，它具有以下优势：

- 环境隔离，避免依赖冲突
- 可移植性强，一次构建，到处运行
- 版本控制，易于回滚
- 资源占用小，启动快

## 本地测试部署

### 1. 构建Docker镜像

```bash
cd backend
docker build -t campus-social-backend .
```

### 2. 运行Docker容器

```bash
docker run -p 8080:8080 \
  -e DB_HOST=ep-broad-cherry-ah6y33hi-pooler.c-3.us-east-1.aws.neon.tech \
  -e DB_PORT=5432 \
  -e DB_NAME=neondb \
  -e DB_USERNAME=neondb_owner \
  -e DB_PASSWORD=npg_3HIQKXvV5MNB \
  -e JWT_SECRET=your_secure_jwt_secret_here \
  campus-social-backend
```

### 3. 使用Docker Compose（推荐）

```bash
docker-compose up -d
```

## 部署到支持Docker的平台

### Render

1. 在Render仪表板中选择"New +" -> "Web Service"
2. 选择您的GitHub仓库
3. Render会自动检测Dockerfile并使用它进行构建
4. 在环境变量部分配置数据库和JWT信息

### Heroku

1. 安装Heroku CLI
2. 登录Heroku账户
3. 创建新应用
4. 使用Heroku Container Registry部署：

```bash
heroku container:login
heroku create your-app-name
heroku container:push web -a your-app-name
heroku container:release web -a your-app-name
heroku config:set DB_HOST=ep-broad-cherry-ah6y33hi-pooler.c-3.us-east-1.aws.neon.tech -a your-app-name
heroku config:set DB_PORT=5432 -a your-app-name
heroku config:set DB_NAME=neondb -a your-app-name
heroku config:set DB_USERNAME=neondb_owner -a your-app-name
heroku config:set DB_PASSWORD=npg_3HIQKXvV5MNB -a your-app-name
heroku config:set JWT_SECRET=your_secure_jwt_secret_here -a your-app-name
```

### AWS Elastic Beanstalk

1. 构建镜像并推送到Elastic Container Registry
2. 在Elastic Beanstalk中创建新应用
3. 选择容器化部署选项

## Docker最佳实践

1. 使用多阶段构建减小镜像体积
2. 不要在镜像中硬编码敏感信息
3. 使用非root用户运行应用
4. 合理设置健康检查

## 故障排除

1. **构建失败**: 检查Dockerfile语法和依赖项
2. **数据库连接失败**: 检查环境变量配置
3. **端口不可访问**: 检查端口映射和防火墙设置
4. **内存不足**: 调整JVM参数 `-Xmx` 和 `-Xms`

## 高级配置

如果需要调整JVM参数，可以在运行容器时设置JAVA_OPTS环境变量：

```bash
docker run -p 8080:8080 \
  -e JAVA_OPTS="-Xmx512m -Xms256m" \
  -e DB_HOST=... \
  campus-social-backend
```