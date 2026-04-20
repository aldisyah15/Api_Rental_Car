package com.example

import com.example.routing.configureRouting
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.serialization.json.Json

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        install(io.ktor.server.plugins.contentnegotiation.ContentNegotiation) {
            json()
        }
        configureRouting()
    }.start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureHTTP()
    configureRouting()
}
