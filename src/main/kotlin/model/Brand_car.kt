package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class Brand_car(
    val brand_name: String,
    val url_logo: String
)
