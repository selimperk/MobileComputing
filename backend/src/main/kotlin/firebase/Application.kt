package com.myapp.firebase

import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.util.concurrent.ConcurrentHashMap

val fcmTokens: ConcurrentHashMap<String, Unit> = ConcurrentHashMap()


fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module).start(wait = true)
}

fun Application.module() {
    loadTokensFromFile()
    initFirebase()
    loopNotifications()
    configureRouting()
}