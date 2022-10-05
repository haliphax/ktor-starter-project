package dev.haliphax.ktorStarterProject

import dev.haliphax.ktorStarterProject.services.demo.DemoService
import dev.haliphax.ktorStarterProject.services.demo.controllers.DemoController
import io.grpc.BindableService
import io.grpc.protobuf.services.ProtoReflectionService
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named

@Module
@ComponentScan
class Dependencies {
  @Factory
  @Named("ProtoReflectionService")
  @Suppress("unused")
  fun protoReflectionService(): BindableService =
    ProtoReflectionService.newInstance()

  @Factory
  @Named("DemoService")
  @Suppress("unused")
  fun demoService(demoController: DemoController): BindableService =
    DemoService(demoController)
}
