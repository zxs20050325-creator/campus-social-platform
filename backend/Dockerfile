# 使用官方推荐的Amazon Corretto 17作为基础镜像
FROM amazoncorretto:17-alpine

# 安装构建所需的工具
RUN apk add --no-cache \
    curl \
    tar \
    gzip \
    bash \
    maven

# 设置工作目录
WORKDIR /app

# 复制pom.xml并下载依赖（利用Docker缓存层优化）
COPY pom.xml .

# 下载依赖包
RUN mvn dependency:go-offline -q

# 复制源代码
COPY src src

# 构建应用
RUN mvn package -DskipTests -q
RUN ls -la target/

# 检查构建产物
RUN ls -la target/campus-social-platform-0.0.1-SNAPSHOT.jar

# 暴露端口
EXPOSE 8080

# 启动应用
ENTRYPOINT ["java", "-jar", "target/campus-social-platform-0.0.1-SNAPSHOT.jar"]