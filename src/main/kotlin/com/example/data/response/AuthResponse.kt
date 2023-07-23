package com.example.data.response

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val username: String,
    val userId: String,
    val token: String
)
