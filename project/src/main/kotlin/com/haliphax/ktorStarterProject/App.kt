package com.haliphax.ktorStarterProject

import com.haliphax.ktorStarterProject.plugins.configureAuth
import com.haliphax.ktorStarterProject.plugins.configureRouting
import com.haliphax.ktorStarterProject.plugins.configureSerialization
import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
	configureAuth()
	configureRouting()
	configureSerialization()
}
