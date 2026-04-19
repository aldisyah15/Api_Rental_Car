package com.example.routing

import com.example.model.Login
import com.example.model.Register
import com.example.repository.auth.SupabaseAuthRepo
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.engine.logError
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.authRouting() {
    route("/auth") {
        post("/register") {
            val dataRegisterBaru = call.receive<Register>()
            SupabaseAuthRepo().register(dataRegisterBaru)
            call.respondText("Berhasil menambahkan ke database!", status = HttpStatusCode.Created)
        }

        post("/login"){
            try {
                val dataLogin = call.receive<Login>()
                SupabaseAuthRepo().login(dataLogin)
                println(dataLogin)
                call.respondText("Berhasil Login", status = HttpStatusCode.OK)
            } catch (e: Exception) {
                call.respondText("$e", status = HttpStatusCode.InternalServerError)
            }
        }
    }
}