# 使用官方OpenJDK 17作为基础镜像
FROM openjdk:17-jre-slim

# 设置工作目录
WORKDIR /app

# 复制pom.xml并下载依赖（利用Docker缓存层优化）
COPY pom.xml .

# 下载依赖包
RUN ./mvnw dependency:go-offline -q

# 复制源代码
COPY src src

# 构建应用
RUN ./mvnw package -DskipTests -q
RUN ls -la target/

# 暴露端口
EXPOSE 8080

# 启动应用
ENTRYPOINT ["java", "-jar", "target/campus-social-platform-0.0.1-SNAPSHOT.jar"]