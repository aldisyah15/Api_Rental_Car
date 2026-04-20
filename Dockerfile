FROM gradle:8.11-jdk21 AS builder
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew build -x test --no-daemon

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Mengambil file jar yang bukan 'plain'
COPY --from=builder /app/build/libs/*[!plain].jar app.jar

ENV PORT=8080
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]