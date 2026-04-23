package com.example.model

class JwtConfig(
    val realm: String,
    val secret: String,
    val audience: String,
    val expiry: Long,
    val issuer: String
)