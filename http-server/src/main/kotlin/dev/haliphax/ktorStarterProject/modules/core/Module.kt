package dev.haliphax.ktorStarterProject.modules.core

import io.ktor.server.application.Application

fun Application.module() {
  configureAuth()
  configureRouting()
  configureSerialization()
}
