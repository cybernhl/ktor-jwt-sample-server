package com.example.data.repository

import com.example.data.entity.UserEntity
import com.example.data.request.AuthRequest
import org.litote.kmongo.eq
import org.litote.kmongo.coroutine.CoroutineDatabase

class AuthRepository(database: CoroutineDatabase) {
    private val users = database.getCollection<UserEntity>()

    suspend fun signIn(request: AuthRequest): UserEntity?{
        return users.findOne(UserEntity::username eq request.username)
    }

    suspend fun signUp(data: UserEntity): Boolean{
        return users.insertOne(data).wasAcknowledged()
    }
}