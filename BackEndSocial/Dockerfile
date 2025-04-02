# Build stage
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Sao chép toàn bộ dự án vào container
COPY . .

# Di chuyển đến thư mục BackEndSocial nơi chứa pom.xml
WORKDIR /app/BackEndSocial

# Thực hiện build Maven
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:17-jdk-slim
WORKDIR /app

# Sao chép file jar đã build từ stage build
COPY --from=build /app/BackEndSocial/target/BackEndSocial-0.0.1-SNAPSHOT.jar app.jar

# Expose port và chạy ứng dụng
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
