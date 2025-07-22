# Use a base image with Java (JDK 21, nếu anh dùng JDK 21)
FROM eclipse-temurin:21-jdk-alpine

# Tạo thư mục chứa app trong container
WORKDIR /app

# Copy file JAR vào container
COPY target/*.jar app.jar

# Expose port (ví dụ 8080)
EXPOSE 8080

# Lệnh chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
