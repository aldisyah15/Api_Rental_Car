package com.example.routing.car

import com.example.database.supabase
import com.example.model.ApiRespondCar
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

fun Route.CarRouting() {
    authenticate("jwt-auth") {
        route("/cars") {

            post("/") {
                val res = SupabaseCarRepo()
                var multipart = call.receiveMultipart()
                var brand_name = ""
                var rental_price = ""
                var horse_power = ""
                var transmission = ""
                var detail = ""
                var sales_name = ""
                var contact_number_whatsapp = ""

                var url_logo: ByteArray? = null
                var fileName_url_logo = ""

                var sales_photo: ByteArray? = null
                var fileName_sales_photo = ""

                var vehicle_photo: ByteArray? = null
                var fileName_vehicle_photo = ""

                var fileName = ""
                var fileBytes: ByteArray? = null
                multipart.forEachPart { part ->
                    when (part) {
                        is PartData.FormItem -> {
                            if (part.name == "brand_name") brand_name = part.value
                            if (part.name == "rental_price") rental_price = part.value
                            if (part.name == "horse_power") horse_power = part.value
                            if (part.name == "transmission") transmission = part.value
                            if (part.name == "sales_name") sales_name = part.value
                            if (part.name == "contact_number_whatsapp") contact_number_whatsapp = part.value
                        }

                        is PartData.FileItem ->  {
                            // Proses upload ke Supabase Storage
                            fileName = "${System.currentTimeMillis()}-${part.originalFileName}"
                            fileBytes = part.streamProvider().readBytes()
                        }
                        else -> part.dispose()
                    }

                    if (fileBytes != null) {
                        val bucket = supabase.storage.from("car-bucket")
                        bucket.upload(fileName, fileBytes)
                        val publicUrl = bucket.publicUrl(fileName)

                        if (part.name == "vehicle_photo") {
                            fileName_vehicle_photo = publicUrl
                        } else if (part.name == "url_logo") {
                            fileName_url_logo = publicUrl
                        }else if (part.name == "sales_photo"){
                            fileName_sales_photo = publicUrl
                        }

                        val newCar = CarRespond(
                            id_car = null,
                            brand_name = brand_name,
                            url_logo = fileName_url_logo,
                            rental_price = rental_price.toInt(),
                            horse_power = horse_power,
                            transmission = transmission,
                            vehicle_photo = fileName_vehicle_photo,
                            detail = detail,
                            sales_name = sales_name,
                            sales_photo = fileName_sales_photo,
                            contact_number_whatsapp = contact_number_whatsapp,
                        )
                        supabase.from("car").insert(newCar)
                        call.respond(HttpStatusCode.Created, "Berhasil simpan mobil dengan foto!")
                    } else {
                        call.respond(HttpStatusCode.BadRequest, "File foto tidak ditemukan")
                    }
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
