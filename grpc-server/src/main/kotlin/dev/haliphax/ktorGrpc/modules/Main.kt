package dev.haliphax.ktorGrpc.modules

import dev.haliphax.ktorGrpc.Dependencies
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.ksp.generated.module
import org.koin.ktor.plugin.Koin

@Suppress("unused") // application.conf
fun Application.main() {
  install(Koin) {
    modules(Dependencies().module)
  }
}
