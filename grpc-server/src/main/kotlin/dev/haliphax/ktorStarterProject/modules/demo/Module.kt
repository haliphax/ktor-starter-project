package dev.haliphax.ktorStarterProject.modules.demo

import io.ktor.server.application.Application
import io.ktor.server.application.log

@Suppress("unused") // application.conf
fun Application.module() {
  log.info("I'm totally loaded, bruh!")
}
