package com.example.routing

import com.example.database.supabase
import com.example.model.Register
import com.example.repository.auth.SupabaseAuthRepo
import io.github.jan.supabase.postgrest.from
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        authRouting()
    }
}
