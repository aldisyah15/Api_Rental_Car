package com.example.repository.auth

import com.example.database.supabase
import com.example.model.Login
import com.example.model.Register
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from

class SupabaseAuthRepo() {
    suspend fun register(req: Register) {
        supabase.from("Register").insert(req)
        supabase.auth.signUpWith(Email) {
            email = req.email
            password = req.password
        }
    }

    suspend fun login(req: Login) {
        supabase.auth.signInWith(Email) {
            email = req.email
            password = req.password
        }
    }
}