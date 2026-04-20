package com.example.repository.auth

import com.example.model.Login
import com.example.model.Register

interface AuthRepo {
    fun register(): List<Register>
    fun login(): List<Login>
}