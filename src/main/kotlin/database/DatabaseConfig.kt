package com.example.database

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

val supabase = createSupabaseClient(
    supabaseUrl = "https://ysprjhwzjnzseouoiweh.supabase.co",
    supabaseKey = "sb_publishable_UnAy3FS-hqUKr1WVRdtlpQ_Eg2Gyack"
) {
    install(Auth)
    install(Postgrest)
    install(Storage)
    //install other modules
}