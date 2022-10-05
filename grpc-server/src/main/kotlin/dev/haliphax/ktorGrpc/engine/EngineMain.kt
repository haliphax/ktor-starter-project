package dev.haliphax.ktorGrpc.engine

import dev.haliphax.ktorGrpc.logging.HasLog
import io.ktor.server.application.Application
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.engine.ApplicationEngineEnvironment
import io.ktor.server.engine.addShutdownHook
import io.ktor.server.engine.applicationEngineEnvironment
import org.koin.dsl.koinApplication

typealias Module = Application.() -> Unit

object EngineMain : HasLog {
  private lateinit var environment: ApplicationEngineEnvironment

  private fun mappedModules(modules: List<String>): Map<String, Module> =
    modules.associate { fqn ->
      val dotIndex = fqn.lastIndexOf('.')
      val className = fqn.substring(0, dotIndex)
      val methodName = fqn.substring(dotIndex + 1)
      val klass = environment.classLoader.loadClass(className)

      @Suppress("UNCHECKED_CAST")
      Pair(fqn, { klass.methods.first { it.name == methodName } } as Module)
    }

  private fun loadEnvironment(modules: MutableMap<String, Module>): ApplicationEngineEnvironment =
    applicationEngineEnvironment {
      config = ApplicationConfig(null)
      this.modules.addAll(
        config.propertyOrNull("ktor.application.modules")?.let {
          mappedModules(it.getList()).map { module ->
            module.value.apply { modules[module.key] = this }
          }
        } ?: emptyList()
      )
    }

  @JvmStatic
  fun main(
    args: Array<String>
  ) {
    koinApplication {
      val modules = mutableMapOf<String, Module>()
      environment = loadEnvironment(modules)
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
}
