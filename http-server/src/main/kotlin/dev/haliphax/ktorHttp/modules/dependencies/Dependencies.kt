package dev.haliphax.ktorHttp.modules.dependencies

import dev.haliphax.ktorHttp.Dependencies
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.ksp.generated.module
import org.koin.ktor.plugin.Koin

fun Application.dependencies() {
  install(Koin) {
    printLogger()
    modules(Dependencies().module)
  }
}
