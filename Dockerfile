# ---------- Stage 1: Build ----------
FROM gradle:8.5-jdk17 AS builder
WORKDIR /app

# Copy semua file project
COPY . .

# Build fat jar (shadowJar)
RUN gradle shadowJar --no-daemon

# ---------- Stage 2: Run ----------
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy hasil build dari stage builder
COPY --from=builder /app/build/libs/*all.jar app.jar

# Port yang digunakan Cloud Run
ENV PORT=8080

# Expose port
EXPOSE 8080

# Run aplikasi
CMD ["java", "-jar", "app.jar"]