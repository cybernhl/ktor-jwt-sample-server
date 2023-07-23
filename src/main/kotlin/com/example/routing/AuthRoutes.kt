package com.example.routing

import com.example.data.entity.UserEntity
import com.example.data.repository.AuthRepository
import com.example.data.request.AuthRequest
import com.example.data.response.AuthResponse
import com.example.token.JwtTokenService
import com.example.token.TokenClaim
import com.example.token.TokenConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId

fun Route.signIn(
    authRepository: AuthRepository,
    tokenService: JwtTokenService,
    tokenConfig: TokenConfig,

){
    post("signIn"){
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val successfulAuth = authRepository.signIn(request)
        if (successfulAuth == null){
            call.respond(HttpStatusCode.Conflict, "用户不存在")
            return@post
        }

        if (successfulAuth.password != request.password || successfulAuth.username != request.username){
            call.respond(HttpStatusCode.Conflict, "登入信息错误")
            return@post
        }

        val claims = listOf(
            TokenClaim("username", successfulAuth.username),
            TokenClaim("userId", successfulAuth.userId),
        )
        val token = tokenService.generate(tokenConfig, claims)

        val response = AuthResponse(
            username = successfulAuth.username,
            userId = successfulAuth.userId,
            token = token
        )

        call.respond(
            HttpStatusCode.OK,
            message = response
        )
    }
}

fun Route.signUp(
    authRepository: AuthRepository
){
    post("signUp"){
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val userEntity = UserEntity(username = request.username, password = request.password, userId = ObjectId().toString())
        val successfulSignUp = authRepository.signUp(userEntity)
        if (!successfulSignUp){
            call.respond(HttpStatusCode.Conflict, message = "注册失败")
            return@post
        }

        call.respond(HttpStatusCode.OK)
    }
}

fun Route.secret(){
    get("secret"){
        val userId = call.principal<JWTPrincipal>()?.getClaim("userId", String::class)

        call.respond(
            HttpStatusCode.OK,
            message = userId!!
        )
    }
}