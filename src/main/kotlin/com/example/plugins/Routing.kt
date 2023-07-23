package com.example.plugins

import com.example.data.repository.AuthRepository
import com.example.routing.secret
import com.example.routing.signIn
import com.example.routing.signUp
import com.example.token.JwtTokenService
import com.example.token.TokenConfig
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.configureRouting(
    authRepository: AuthRepository,
    tokenService: JwtTokenService,
    tokenConfig: TokenConfig
) {
    routing {

        signUp(authRepository)
        signIn(authRepository, tokenService, tokenConfig)

        authenticate{
            secret()
        }
    }
}
