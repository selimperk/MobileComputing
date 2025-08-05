package com.myapp.firebase.cloudmessaging

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.receiveText
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        post("/token") {
            val token = call.receiveText()

            if (token.isBlank()) {
                call.respond(HttpStatusCode.BadRequest, "Token cannot be empty.")
                return@post
            }

            fcmTokens[token] = Unit
            saveTokensToFile()
            println("New FCM Token registered: $token. Total tokens: ${fcmTokens.size}")
            call.respond(HttpStatusCode.OK, "Token successfully registered.")
        }

    }
}