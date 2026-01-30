# 后端部署到 Render

## 部署步骤

1. **登录Render账户**
   - 访问 https://render.com
   - 使用GitHub账户登录

2. **创建Web Service**
   - 点击"New +"按钮
   - 选择"Web Service"
   - 选择您的`campus-social-platform`仓库

3. **配置部署参数**
   - **Environment**: 选择 Java
   - **Branch**: main
   - **Build Command**: `./mvnw --batch --quiet package -DskipTests`
   - **Start Command**: `java -jar target/campus-social-platform-0.0.1-SNAPSHOT.jar`

4. **配置环境变量**
   在Render控制台中配置以下环境变量：
   ```
   DB_HOST=ep-broad-cherry-ah6y33hi-pooler.c-3.us-east-1.aws.neon.tech
   DB_PORT=5432
   DB_NAME=neondb
   DB_USERNAME=neondb_owner
   DB_PASSWORD=npg_3HIQKXvV5MNB
   JWT_SECRET=your_secure_jwt_secret_here
   ```

5. **高级配置**
   - **Health Check Path**: `/actuator/health`
   - **Region**: 根据用户地理位置选择最接近的区域
   - **Plan Type**: Free (或选择Paid以获得更多资源)

## 验证部署

1. 确认应用成功启动
2. 访问 `https://your-app-name.onrender.com/actuator/health` 检查健康状态
3. 测试API端点以确保数据库连接正常

## 常见问题

1. **构建超时**
   - Render免费版构建时间限制为15分钟
   - 确保构建命令高效

2. **内存不足**
   - Render免费版提供512MB内存
   - 如遇内存不足问题，可优化JVM参数

3. **数据库连接问题**
   - 检查环境变量是否正确配置
   - 确认Neon.tech数据库允许外部连接

## 更新部署

- 推送代码到GitHub后，Render会自动触发重新构建
- 或在Render控制台手动触发重新部署

## 自定义域名（可选）

- 在Render控制台中添加自定义域名
- 按照提示配置DNS记录