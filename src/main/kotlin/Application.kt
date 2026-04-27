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

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
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
