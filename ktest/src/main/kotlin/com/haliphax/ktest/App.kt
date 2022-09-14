package com.haliphax.ktest

import com.haliphax.ktest.plugins.configureAuth
import com.haliphax.ktest.plugins.configureRouting
import com.haliphax.ktest.plugins.configureSerialization
import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
	configureAuth()
	configureRouting()
	configureSerialization()
}
