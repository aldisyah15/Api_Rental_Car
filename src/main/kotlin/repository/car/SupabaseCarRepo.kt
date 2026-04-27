package com.example.repository.car

import com.example.database.supabase
import com.example.model.Brand_car
import com.example.model.CarRespond
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import io.ktor.http.content.MultiPartData
import io.ktor.http.content.PartData

class SupabaseCarRepo() {
    suspend fun get_brands(): List<Brand_car> {
        val bucket = supabase.from("car").select().decodeList<Brand_car>()
        return bucket

    }

    suspend fun getCars(): List<CarRespond> {
        val response = supabase.from("car").select().decodeList<CarRespond>()
        return response
    }


    suspend fun postCar(url_logo: String, url_logo_file: ByteArray, sales_photo: String, sales_photo_file: ByteArray,vehicle_photo: String, vehicle_photo_file: ByteArray , newCar: CarRespond) {
        val bucket = supabase.storage.from("car")
        bucket.upload(url_logo, url_logo_file)
        bucket.upload(sales_photo, sales_photo_file)
        bucket.upload(vehicle_photo, vehicle_photo_file)

        val logo_url = bucket.publicUrl(url_logo)
        val sales_photo_url = bucket.publicUrl(sales_photo)
        val vehicle_photo_url = bucket.publicUrl(vehicle_photo)

        val updateCarNew = newCar.copy(
            url_logo = logo_url,
            sales_photo = sales_photo_url,
            vehicle_photo = vehicle_photo_url
        )

        supabase.from("car").insert(updateCarNew)
    }
}

