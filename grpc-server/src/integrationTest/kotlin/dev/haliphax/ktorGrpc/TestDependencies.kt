package dev.haliphax.ktorGrpc

import dev.haliphax.ktorGrpc.proto.DemoServiceGrpcKt.DemoServiceCoroutineStub
import dev.haliphax.ktorGrpc.services.demo.DemoService
import dev.haliphax.ktorGrpc.services.demo.controllers.DemoController
import io.grpc.Server
import io.grpc.inprocess.InProcessChannelBuilder
import io.grpc.inprocess.InProcessServerBuilder
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan
@Suppress("unused")
class TestDependencies {
  @Single
  fun demoServer(): Server =
    InProcessServerBuilder
      .forName("test")
      .directExecutor()
      .addService(DemoService(DemoController()))
      .build()

  @Single
  fun demoServiceStub(): DemoServiceCoroutineStub {
    val channel = InProcessChannelBuilder
      .forName("test")
      .directExecutor()
      .usePlaintext()
      .build()
    return DemoServiceCoroutineStub(channel)
  }
}
