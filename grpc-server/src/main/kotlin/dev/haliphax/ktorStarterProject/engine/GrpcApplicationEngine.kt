package dev.haliphax.ktorStarterProject.engine

import dev.haliphax.ktorStarterProject.logging.HasLog
import dev.haliphax.ktorStarterProject.reflection.LoadedMethod
import dev.haliphax.ktorStarterProject.reflection.toCallables
import dev.haliphax.ktorStarterProject.services.demo.DemoService
import dev.haliphax.ktorStarterProject.services.interceptors.RequestInterceptor
import io.grpc.BindableService
import io.grpc.Server
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder
import io.ktor.server.application.ApplicationStopPreparing
import io.ktor.server.application.host
import io.ktor.server.application.port
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.ApplicationEngineEnvironment
import io.ktor.server.engine.BaseApplicationEngine
import io.ktor.server.engine.loadCommonConfiguration
import io.ktor.util.network.NetworkAddress
import java.util.concurrent.TimeUnit
import kotlin.reflect.full.companionObjectInstance

class GrpcApplicationEngine(
  environment: ApplicationEngineEnvironment
) : BaseApplicationEngine(environment), HasLog {
  val server: Server

  class Configuration(
    var services: List<LoadedMethod> = emptyList()
  ) : BaseApplicationEngine.Configuration() {
    fun loadConfiguration(
      config: ApplicationConfig,
      classLoader: ClassLoader
    ): Configuration {
      val deploymentConfig = config.config("ktor.deployment")
      loadCommonConfiguration(deploymentConfig)

      val applicationConfig = config.config("ktor.application")
      applicationConfig.propertyOrNull("services")?.getList()?.let {
        services = it.toCallables(classLoader)
      }

      return this
    }
  }

  private val configuration: Configuration

  init {
    configuration = Configuration().loadConfiguration(environment.config, environment.classLoader)
    server = NettyServerBuilder
      .forAddress(
        NetworkAddress(
          environment.config.host,
          environment.config.port
        )
      )
      .apply {
        configuration.services.forEach { method ->
          val service = method.klass.methods.first {
            it.name == method.callable
          }
            .invoke(
              method.klass.kotlin.companionObjectInstance
            ) as BindableService

          addService(service)
          log.trace("Loaded service: $service")
        }
      }
      .addService(DemoService())
      .intercept(RequestInterceptor)
      .build()
  }

  override fun start(wait: Boolean): ApplicationEngine {
    server.start()
    log.info("Server listening on port ${server.port}")

    if (wait) {
      server.awaitTermination()
    }

    return this
  }

  override fun stop(gracePeriodMillis: Long, timeoutMillis: Long) {
    log.info("Stopping: $server")
    environment.monitor.raise(ApplicationStopPreparing, environment)
    server.awaitTermination(gracePeriodMillis, TimeUnit.MILLISECONDS)
    server.shutdownNow()
  }
}
