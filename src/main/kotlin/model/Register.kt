package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class Register(
    val name: String,
    val email: String,
    val phone_number: String,
    val password: String
)