package com.example.plugin

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.model.ApiResponse
import com.example.model.JwtConfig
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTCredential
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.response.respond

fun Application.configureJwtAuthtentication(config: JwtConfig) {
    install(Authentication) {
        jwt(name = "jwt-auth") {
            realm = config.realm
            val JwtVerifier = JWT
                .require(Algorithm.HMAC256(config.secret))
                .withAudience(config.audience)
                .withIssuer(config.issuer)
                .build()

            verifier(JwtVerifier)
            validate { JWTCredential ->
                val username = JWTCredential.payload.getClaim("email").asString()
                if (!username.isNullOrBlank()) {
                    JWTPrincipal(JWTCredential.payload)
                } else {
                    null
                }
            }
            
            challenge { _, _ ->
                call.respond(
                    status = HttpStatusCode.Unauthorized,
                    ApiResponse(
                        false,
                        "Token Tidak Valid"
                    )
                )
            }
        }
    }
}