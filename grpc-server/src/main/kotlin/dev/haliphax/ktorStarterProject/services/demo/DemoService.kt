package dev.haliphax.ktorStarterProject.services.demo

import dev.haliphax.ktorStarterProject.proto.DemoRequest
import dev.haliphax.ktorStarterProject.proto.DemoResponse
import dev.haliphax.ktorStarterProject.proto.DemoServiceGrpcKt
import dev.haliphax.ktorStarterProject.services.demo.controllers.DemoController
import io.grpc.BindableService
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
@Named("DemoService")
class DemoService(
  private val demoController: DemoController
) :
  DemoServiceGrpcKt.DemoServiceCoroutineImplBase(),
  // allows finding this component via BindableService binding qualifier
  BindableService {

  override suspend fun demo(request: DemoRequest): DemoResponse {
    val message = demoController.test()
    return DemoResponse.newBuilder().setMessage(message).build()
  }
}
