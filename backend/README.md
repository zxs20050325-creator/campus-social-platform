# 校园轻社交平台 - 后端

这是校园轻社交 + 资源置换平台的后端服务，使用Java Spring Boot 3 + JPA + PostgreSQL。

## 技术栈

- Java Spring Boot 3
- Spring Security
- JWT认证
- JPA/Hibernate
- PostgreSQL
- Lombok

## 数据库模型

- User: 用户信息（学号、昵称、密码、头像URL、学校、联系方式）
- Item: 商品信息（标题、描述、图片、价格、分类、状态等）
- Post: 帖子信息（内容、图片、点赞数等）
- Comment: 评论信息（内容、关联帖子或商品等）

## API接口

### 认证接口

- `POST /api/auth/login` - 用户登录
- `POST /api/auth/register` - 用户注册

### 商品接口

- `GET /api/items` - 获取商品列表（分页）
- `POST /api/items` - 发布商品
- `PUT /api/items/{id}/status` - 更新商品状态
- `GET /api/items/my-items` - 获取当前用户商品列表

### 帖子接口

- `GET /api/posts` - 获取帖子列表（分页）
- `POST /api/posts` - 发布帖子
- `GET /api/posts/my-posts` - 获取当前用户帖子列表

## 环境配置

创建环境变量或在`application.yml`中配置：

```yaml
spring:
  datasource:
    url: jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:campus_social_db}
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:password}
  jpa:
    hibernate:
      ddl-auto: update
```

## 本地运行

1. 确保已安装Java 17+和Maven
2. 配置数据库连接
3. 运行应用：
   ```bash
   mvn spring-boot:run
   ```

应用将在`http://localhost:8080`启动。

## Docker部署

使用Docker部署是推荐的方式，因为它可以确保环境一致性。

### 构建Docker镜像

```bash
cd backend
docker build -t campus-social-backend .
```

### 运行Docker容器

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

### 使用Docker Compose

```bash
docker-compose up -d
```

## 部署到Render

1. 在Render中创建Web Service
2. 选择您的GitHub仓库
3. 如果Render支持Docker，则会自动使用Dockerfile进行构建
4. 设置环境变量：
   - `DB_HOST`: 数据库主机地址
   - `DB_PORT`: 数据库端口
   - `DB_NAME`: 数据库名称
   - `DB_USERNAME`: 数据库用户名
   - `DB_PASSWORD`: 数据库密码
   - `JWT_SECRET`: JWT密钥
5. 设置健康检查路径：`/actuator/health`

应用将在Render分配的URL上运行。