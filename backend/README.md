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