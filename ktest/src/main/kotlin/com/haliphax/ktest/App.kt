package com.haliphax.ktest

// 3rd party
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
// local
import com.haliphax.ktest.plugins.*

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
	configureAuth()
	configureRouting()
	configureSerialization()
}
