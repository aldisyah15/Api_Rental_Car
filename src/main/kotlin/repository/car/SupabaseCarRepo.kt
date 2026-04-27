package com.example.repository.car

import com.example.database.supabase
import com.example.model.Brand_car
import com.example.model.CarRespond
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage

class SupabaseCarRepo() {
    suspend fun get_brands(): List<Brand_car> {
        val bucket = supabase.from("car").select().decodeList<Brand_car>()
        return bucket

    }

    suspend fun getCars(): List<CarRespond> {
        val response = supabase.from("car").select().decodeList<CarRespond>()
        return response
    }


    suspend fun postCar(
        url_logo_fileName: String, url_logo_byte: ByteArray?, sales_photo_fileName: String, sales_photo_Byte: ByteArray?,
        vehicle_photo_FileName: String, vehicle_photo_Byte: ByteArray?, newCar: CarRespond
    ) {
        val bucket = supabase.storage.from("car")
        if (url_logo_byte != null) {
            bucket.upload(url_logo_fileName, url_logo_byte)
        }
        if (sales_photo_Byte != null) {
            bucket.upload(sales_photo_fileName, sales_photo_Byte)
        }
        if (vehicle_photo_Byte != null) {
            bucket.upload(vehicle_photo_FileName, vehicle_photo_Byte)
        }

        val logo_url = bucket.publicUrl(url_logo_fileName)
        val sales_photo_url = bucket.publicUrl(sales_photo_fileName)
        val vehicle_photo_url = bucket.publicUrl(vehicle_photo_FileName)

        val updateCarNew = newCar.copy(
            url_logo = logo_url,
            sales_photo = sales_photo_url,
            vehicle_photo = vehicle_photo_url
        )

        supabase.from("car").insert(updateCarNew)
    }
}

