package com.example.routing

import com.example.model.ApiResponse
import com.example.model.ApiResponseToken
import com.example.model.JwtConfig
import com.example.model.Login
import com.example.model.Register
import com.example.model.generateToken
import com.example.repository.auth.SupabaseAuthRepo
import io.github.jan.supabase.auth.user.UserSession
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.engine.logError
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.authRouting(config: JwtConfig) {
    route("/auth") {
        post("/register") {
            val dataRegisterBaru = call.receive<Register>()
            SupabaseAuthRepo().register(dataRegisterBaru)
            val token = generateToken(config, dataRegisterBaru.email)

            call.respond(
                HttpStatusCode.OK,
                ApiResponseToken(
                    success = true,
                    message = "Berhasil",
                    token = token
                )
            )



            call.respond(
                HttpStatusCode.BadRequest,
                ApiResponse(
                    success = false,
                    message = "Gagal"
                )
            )
        }

        post("/login"){

                val dataLogin = call.receive<Login>()
                SupabaseAuthRepo().login(dataLogin)
                println(dataLogin)
                call.respond(
                    HttpStatusCode.OK,
                    ApiResponse(
                        true,
                        "Berhasil Login"
                    )
                )

            call.respond(
                HttpStatusCode.BadRequest,
                ApiResponse(
                    false,
                    "Email or Password wrong"
                )
            )

        }


    }

    authenticate("jwt-auth") {
        route("/car") {
            get("/brands") {
val principal = call.principal<JWTPrincipal>()
                val email = principal?.payload?.getClaim("email")?.asString()
                call.respondText("hello, ${email}")
            }
        }
    }
}