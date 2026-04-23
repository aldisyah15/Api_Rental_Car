package com.example

import com.example.model.JwtConfig
import com.example.plugin.configureJwtAuthtentication
import com.example.routing.configureRouting
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.*

// main.kt
fun main(args: Array<String>) {
    // Biarkan EngineMain yang bekerja karena kamu sudah setting YAML & Docker dengan benar
    io.ktor.server.netty.EngineMain.main(args)
}

// Application.kt
fun Application.module() {
    // Ambil blok "jwt"
    val jwt = environment.config.config("jwt")

    val config = JwtConfig(
        realm = jwt.property("realm").getString(),
        secret = jwt.property("secret").getString(),
        audience = jwt.property("audience").getString(),
        expiry = jwt.property("expiry").getString().toLong(),
        issuer = jwt.property("issuer").getString()
    )

    install(CORS) {
        anyHost()
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowHeader(HttpHeaders.ContentType)
    }

    configureSerialization()
    configureJwtAuthtentication(config)
    configureRouting(config)
}
