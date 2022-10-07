package dev.haliphax.ktorHttp.modules.core

import dev.haliphax.ktorHttp.Catalog
import dev.haliphax.ktorHttp.Dependencies
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.core.parameter.parametersOf
import org.koin.ksp.generated.module
import org.koin.ktor.plugin.Koin

@Suppress("unused") // application.conf
fun Application.core() {
  install(Koin) {
    printLogger()
    modules(Dependencies().module)
    Catalog.demoService = koin.get { parametersOf(environment.config) }
  }

  configureAuth()
  configureRouting()
  configureSerialization()
}
