package dev.haliphax.ktorStarterProject.engine

import dev.haliphax.ktorStarterProject.logging.HasLog
import dev.haliphax.ktorStarterProject.reflection.toCallables
import io.ktor.server.application.Application
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.engine.ApplicationEngineEnvironment
import io.ktor.server.engine.addShutdownHook
import io.ktor.server.engine.applicationEngineEnvironment

object EngineMain : HasLog {
  private fun loadEnvironment(
    loadedModules: MutableMap<String, Application.() -> Unit>
  ): ApplicationEngineEnvironment =
    applicationEngineEnvironment {
      config = ApplicationConfig(null)
      this.modules.addAll(
        config.propertyOrNull("ktor.application.modules")?.let {
          it.getList().toCallables(classLoader).map { loaded ->
            loaded.toModule().apply {
              loadedModules[loaded.fullyQualifiedName] = this
            }
          }
        } ?: emptyList()
      )
    }

  @JvmStatic
  fun main(args: Array<String>) {
    val modules = mutableMapOf<String, Application.() -> Unit>()
    val environment = loadEnvironment(modules)
    val engine = GrpcApplicationEngine(environment)

    modules.forEach {
      it.value(environment.application)
      log.trace("Loaded module: ${it.key}")
    }

    engine.addShutdownHook {
      engine.stop(3000, 5000)
    }

    engine.start(true)
  }
}
