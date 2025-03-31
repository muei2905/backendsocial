# Sử dụng OpenJDK 17
FROM openjdk:17-jdk-slim

# Đặt thư mục làm việc
WORKDIR /app

# Copy toàn bộ dự án vào container
COPY . .

# Cấp quyền thực thi cho mvnw
RUN chmod +x mvnw

# Build ứng dụng
RUN ./mvnw clean package -DskipTests

# Chạy ứng dụng
CMD ["java", "-jar", "target/*.jar"]
