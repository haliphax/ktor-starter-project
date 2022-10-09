package dev.haliphax.ktorHttp

import dev.haliphax.ktorGrpc.proto.DemoServiceGrpcKt.DemoServiceCoroutineStub
import io.ktor.server.config.ApplicationConfig
import io.mockk.mockk
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan
@Suppress("unused")
class TestDependencies {
  @Single
  fun demoServiceStub(config: ApplicationConfig): DemoServiceCoroutineStub =
    mockk(relaxed = true)
}
