package com.example.routing.car

import com.example.model.ApiRespondCar
import com.example.model.Brand_car
import com.example.model.CarRespond
import com.example.repository.car.SupabaseCarRepo
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.util.valuesOf
import io.ktor.utils.io.readByte
import io.ktor.utils.io.readByteArray

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
                multipart.forEachPart { part ->
                    when (part) {
                        is PartData.FormItem -> {
                            if (part.name == "brand_name") brand_name = part.value
                            if (part.name == "rental_price") rental_price = part.value
                            if (part.name == "horse_power") horse_power = part.value
                            if (part.name == "transmission") transmission = part.value
                            if (part.name == "vehicle_photo") vehicle_photo = part.value
                            if (part.name == "sales_name") sales_name = part.value
                            if (part.name == "sales_photo") sales_photo = part.value
                            if (part.name == "contact_number_whatsapp") contact_number_whatsapp = part.value
                        }

                        is PartData.FileItem -> {
                            if (part.name == "url_logo") url_logo = part
                            val fileName = "${System.currentTimeMillis()}-${part.originalFileName}"
                            val fileBytes = part.streamProvider().readBytes()
                        }

                        else -> part.dispose
                    }
                }

                if (url_logo != null || sales_photo != null || vehicle_photo != null) {
                    val carObject = CarRespond(
                        brand_name = brand_name,
                        url_logo = fileName_url_logo, //url
                        rental_price = rental_price.toInt(),
                        contact_number_whatsapp = contact_number_whatsapp,
                        detail = detail,
                        horse_power = horse_power,
                        sales_name = sales_name,
                        sales_photo = fileName_sales_photo, //url
                        transmission = transmission,
                        vehicle_photo = fileName_vehicle_photo, // url
                        id_car = null
                    )
                    res.postCar(fileName = fileName, filebytes = fileBytes, newCar = carObject)
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
}