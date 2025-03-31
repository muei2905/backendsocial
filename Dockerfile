# Sử dụng hình ảnh OpenJDK làm base image
FROM openjdk:17-jdk-slim

# Đặt biến môi trường cho vùng làm việc
WORKDIR /app

# Copy file JAR vào container
COPY target/myapp.jar app.jar

# Chạy ứng dụng
CMD ["java", "-jar", "app.jar"]
