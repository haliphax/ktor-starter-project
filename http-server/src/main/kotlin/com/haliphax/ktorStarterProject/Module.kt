package com.haliphax.ktorStarterProject

import com.haliphax.ktorStarterProject.plugins.configureAuth
import com.haliphax.ktorStarterProject.plugins.configureRouting
import com.haliphax.ktorStarterProject.plugins.configureSerialization
import io.ktor.server.application.Application

fun Application.module() {
	configureAuth()
	configureRouting()
	configureSerialization()
}
