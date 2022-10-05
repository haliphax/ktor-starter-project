package dev.haliphax.ktorGrpc

import io.grpc.BindableService
import io.grpc.protobuf.services.ProtoReflectionService
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named

@Module
@ComponentScan
@Suppress("unused")
class Dependencies {
  @Factory
  @Named("ProtoReflectionService")
  fun protoReflectionService(): BindableService =
    ProtoReflectionService.newInstance()
}
