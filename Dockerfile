# ---------- Stage 1: Build ----------
# Gunakan JDK 21 agar sesuai dengan jvmToolchain(21)
FROM gradle:8.11-jdk21 AS builder
WORKDIR /app

# Copy semua file project
COPY . .

# Beri izin eksekusi gradlew
RUN chmod +x gradlew

# Gunakan perintah build standar (bukan shadowJar)
RUN ./gradlew build -x test --no-daemon

# ---------- Stage 2: Run ----------
# Gunakan JRE 21 yang lebih ringan untuk menjalankan aplikasi
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Ambil file jar hasil build.
# Ktor 3.x biasanya meletakkan jar di build/libs/
COPY --from=builder /app/build/libs/*.jar app.jar

# Cloud Run butuh port 8080
ENV PORT=8080
EXPOSE 8080

# Jalankan aplikasi menggunakan EngineMain sesuai konfigurasi kamu
CMD ["java", "-jar", "app.jar"]