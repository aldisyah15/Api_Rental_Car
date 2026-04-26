package com.example.routing.car

import com.example.model.ApiRespondCar
import com.example.model.Brand_car
import com.example.repository.car.SupabaseCarRepo
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.CarRouting() {
    authenticate("jwt-auth") {
        route("/cars") {
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