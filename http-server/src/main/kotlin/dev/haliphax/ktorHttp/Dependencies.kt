package dev.haliphax.ktorHttp

import dev.haliphax.ktorGrpc.proto.DemoServiceGrpcKt.DemoServiceCoroutineStub
import io.grpc.ManagedChannelBuilder
import io.ktor.server.config.ApplicationConfig
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.dsl.koinApplication

@Module
@ComponentScan
class Dependencies {
  @Single
  fun demoService(config: ApplicationConfig): DemoServiceCoroutineStub {
    lateinit var service: DemoServiceCoroutineStub

    koinApplication {
      val grpcConfig = config.config("grpcEndpoint")
      val host = grpcConfig.property("host").getString()
      val port = grpcConfig.property("port").getString().toInt()
      val channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext()
        .build()
      service = DemoServiceCoroutineStub(channel)
    }

    return service
  }
}
