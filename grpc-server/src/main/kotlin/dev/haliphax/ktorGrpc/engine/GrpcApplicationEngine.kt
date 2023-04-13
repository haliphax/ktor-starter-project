package dev.haliphax.ktorGrpc.engine

import dev.haliphax.common.logging.HasLog
import dev.haliphax.ktorGrpc.Dependencies
import dev.haliphax.ktorGrpc.interceptors.RequestInterceptor
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
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.ksp.generated.module
import java.util.concurrent.TimeUnit

class GrpcApplicationEngine(
  environment: ApplicationEngineEnvironment,
) : BaseApplicationEngine(environment), HasLog {
  lateinit var server: Server
  private val configuration: Configuration

  internal class Configuration : BaseApplicationEngine.Configuration() {
    lateinit var services: List<String>

    fun loadConfiguration(config: ApplicationConfig): Configuration {
      val deploymentConfig = config.config("ktor.deployment")
      loadCommonConfiguration(deploymentConfig)

      val applicationConfig = config.config("ktor.application")
      services = applicationConfig.propertyOrNull("services")?.getList()
        ?: emptyList()

      return this
    }
  }

  init {
    configuration = Configuration().loadConfiguration(environment.config)

    koinApplication {
      modules(Dependencies().module)
      server = NettyServerBuilder
        .forAddress(
          NetworkAddress(
            environment.config.host,
            environment.config.port,
          ),
        )
        .apply {
          configuration.services.forEach { name ->
            val service = koin.get<BindableService>(named(name))
            addService(service)
            log.trace("Loaded service: $service")
          }
        }
        .intercept(RequestInterceptor)
        .build()
    }
  }

  override fun start(wait: Boolean): ApplicationEngine {
    server.start()
    log.info("Server listening on port ${server.port}")

    if (wait) {
      server.awaitTermination()
      log.info("Stopping")
    }

    return this
  }

  override fun stop(gracePeriodMillis: Long, timeoutMillis: Long) {
    log.info("Stopping")
    environment.monitor.raise(ApplicationStopPreparing, environment)
    server.awaitTermination(gracePeriodMillis, TimeUnit.MILLISECONDS)
    server.shutdownNow()
  }
}
