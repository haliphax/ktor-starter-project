package dev.haliphax.ktorGrpc.engine

import dev.haliphax.common.logging.HasLog
import dev.haliphax.ktorGrpc.aliases.Module
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.engine.ApplicationEngineEnvironment
import io.ktor.server.engine.addShutdownHook
import io.ktor.server.engine.applicationEngineEnvironment
import org.koin.dsl.koinApplication
import java.lang.reflect.Method

object EngineMain : HasLog {
  private var loadedModules = mutableMapOf<String, Module>()

  private fun loadEnvironment(): ApplicationEngineEnvironment =
    applicationEngineEnvironment {
      config = ApplicationConfig(null)
      this.modules.addAll(
        config.propertyOrNull("ktor.application.modules")?.let {
          mapModules(classLoader, it.getList()).map { module ->
            module.value.apply { loadedModules[module.key] = this }
          }
        } ?: emptyList()
      )
    }

  private fun mapModules(
    classLoader: ClassLoader,
    modules: List<String>
  ): Map<String, Module> =
    modules.associateWith { fqn ->
      val dotIndex = fqn.lastIndexOf('.')
      val className = fqn.substring(0, dotIndex)
      val methodName = fqn.substring(dotIndex + 1)
      val klass = classLoader.loadClass(className)
      val module = klass.methods.first { it.name == methodName }
      moduleWrapper(module)
    }

  private fun moduleWrapper(method: Method): Module =
    { method(this, this) }

  @JvmStatic
  fun main(args: Array<String>) {
    koinApplication {
      val environment = loadEnvironment()
      val engine = GrpcApplicationEngine(environment)

      loadedModules.forEach {
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
