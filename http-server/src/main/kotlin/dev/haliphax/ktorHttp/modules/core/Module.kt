package dev.haliphax.ktorHttp.modules.core

import io.ktor.server.application.Application

fun Application.module() {
  configureAuth()
  configureRouting()
  configureSerialization()
}
