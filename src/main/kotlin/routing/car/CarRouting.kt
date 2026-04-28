package com.example.routing.car

import com.example.database.supabase
import com.example.model.ApiRespondCar
import com.example.model.ApiResponse
import com.example.model.CarRespond
import com.example.repository.car.SupabaseCarRepo
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

fun Route.CarRouting() {
    authenticate("jwt-auth") {
        route("/cars") {

            post("/") {
                val res = SupabaseCarRepo()
                val multipart = call.receiveMultipart()

                // Penampung data
                var brand_name = ""
                var rental_price = ""
                var horse_power = ""
                var transmission = ""
                var detail = ""
                var sales_name = ""
                var contact_number_whatsapp = ""

                var url_logo_url = ""
                var sales_photo_url = ""
                var vehicle_photo_url = ""

                // 1. LOOP PERTAMA: Kumpulkan semua data dan upload file
                multipart.forEachPart { part ->
                    when (part) {
                        is PartData.FormItem -> {
                            when (part.name) {
                                "brand_name" -> brand_name = part.value
                                "rental_price" -> rental_price = part.value
                                "horse_power" -> horse_power = part.value
                                "transmission" -> transmission = part.value
                                "sales_name" -> sales_name = part.value
                                "contact_number_whatsapp" -> contact_number_whatsapp = part.value
                                "detail" -> detail = part.value
                            }
                        }
                        is PartData.FileItem -> {
                            val fileName = "${System.currentTimeMillis()}-${part.originalFileName}"
                            val fileBytes = part.streamProvider().readBytes()

                            // Langsung upload ke storage begitu file didapat
                            val bucket = supabase.storage.from("car_photo")
                            bucket.upload(fileName, fileBytes)
                            val publicUrl = bucket.publicUrl(fileName)

                            // Simpan URL-nya ke variabel yang tepat
                            when (part.name) {
                                "url_logo" -> url_logo_url = publicUrl
                                "sales_photo" -> sales_photo_url = publicUrl
                                "vehicle_photo" -> vehicle_photo_url = publicUrl
                            }
                        }
                        else -> part.dispose()
                    }
                    part.dispose()
                }

                // 2. DI LUAR LOOP: Baru lakukan insert ke database
                val dataToInsert = buildJsonObject {
                    put("brand_name", brand_name)
                    put("url_logo", url_logo_url)
                    put("rental_price", rental_price.toIntOrNull() ?: 0)
                    put("horse_power", horse_power)
                    put("transmission", transmission)
                    put("vehicle_photo", vehicle_photo_url)
                    put("detail", detail)
                    put("sales_name", sales_name)
                    put("sales_photo", sales_photo_url)
                    put("contact_number_whatsapp", contact_number_whatsapp)
                }

                println("Final Data to Insert: $dataToInsert")

                try {
                    if (brand_name.isNotEmpty()) {
                        supabase.from("car").insert(dataToInsert)
                        call.respond(HttpStatusCode.Created, "Berhasil simpan mobil dan 3 foto!")
                    } else {
                        call.respond(HttpStatusCode.BadRequest, "Data tidak lengkap")
                    }
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "Error: ${e.message}")
                }
            }
            }

            get("/") {
                val res = SupabaseCarRepo()

                call.respond(
                    status = HttpStatusCode.OK,
                    ApiRespondCar(
                        succes = true,
                        data = res.getCars()
                    )
                )

            }

            get("/brands") {
                val res = SupabaseCarRepo()
                call.respond(
                    status = HttpStatusCode.OK,
                    ApiRespondCar(
                        succes = true,
                        data = res.get_brands()
                    )
                )
            }
        }
    }
