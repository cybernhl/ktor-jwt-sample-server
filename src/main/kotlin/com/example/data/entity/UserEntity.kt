package com.example.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class UserEntity(
    val username: String,
    val password: String,
    val userId: String
)
