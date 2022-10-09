package dev.haliphax.ktorHttp.modules.core

import io.ktor.server.application.Application

@Suppress("unused") // application.conf
fun Application.core() {
  configureAuth()
  configureRouting()
  configureSerialization()
}
