package com.example.model

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.github.jan.supabase.auth.jwt.JwtHeader
import java.util.Date

fun generateToken(config: JwtConfig, email: String): String {
return JWT.create()
    .withAudience(config.audience)
    .withIssuer(config.issuer)
    .withClaim("email", email)
    .withExpiresAt(Date(System.currentTimeMillis() + config.expiry))
    .sign(Algorithm.HMAC256(config.secret))
}