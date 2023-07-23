package com.example

import com.example.data.repository.AuthRepository
import io.ktor.server.application.*
import com.example.plugins.*
import com.example.token.JwtTokenService
import com.example.token.TokenClaim
import com.example.token.TokenConfig
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {

    val tokenService = JwtTokenService()
    val tokenConfig = TokenConfig(
        audience = environment.config.property("jwt.audience").getString(),
        issuer = environment.config.property("jwt.issuer").getString(),
        expiresIn = 1000L * 60L * 60L * 24L,
        secret = System.getenv("JWT_SECRET")
    )

    val database = KMongo.createClient()
        .coroutine
        .getDatabase("jwt_example_db")

    val authRepository = AuthRepository(database)

    configureSecurity(tokenConfig)
    configureSerialization()
    configureRouting(authRepository, tokenService, tokenConfig)
}
