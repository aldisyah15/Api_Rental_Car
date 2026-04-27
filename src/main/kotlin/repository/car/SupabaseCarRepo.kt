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


    suspend fun postCar() {

    }
}

