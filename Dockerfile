# ---------- Stage 1: Build ----------
# Menggunakan Gradle 8.11 dan JDK 17 sesuai kebutuhan plugin Shadow kamu
FROM gradle:8.11-jdk17 AS builder
WORKDIR /app

# Copy semua file project
COPY . .

# Build fat jar (shadowJar)
# Menggunakan ./gradlew lebih disarankan agar versinya konsisten dengan laptopmu
RUN chmod +x gradlew && ./gradlew shadowJar --no-daemon

# ---------- Stage 2: Run ----------
# Menggunakan JRE 17 yang lebih ringan dan stabil
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy hasil build dari stage builder
# Kita ambil file yang berakhiran -all.jar dan menamainya app.jar
COPY --from=builder /app/build/libs/*all.jar app.jar

# Konfigurasi Port untuk Cloud Run
ENV PORT=8080
EXPOSE 8080

# Run aplikasi
# Menggunakan exec form ["java", "-jar", "app.jar"] adalah best practice
CMD ["java", "-jar", "app.jar"]