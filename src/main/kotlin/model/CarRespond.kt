package com.example.model

import kotlinx.serialization.Serializable

data class CarRespond(
    val id_car: Int,
    val brand_name: String,
    val url_logo: String,
    val rental_price: String,
    val horse_power: String,
    val transmission: String,
    val vehicle_photo: String,
    val detail: String,
    val sales_name : String,
    val sales_photo: String,
    val contact_number_whatsapp: String
)
