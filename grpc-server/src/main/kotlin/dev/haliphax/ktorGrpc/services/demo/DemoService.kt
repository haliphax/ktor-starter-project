package dev.haliphax.ktorGrpc.services.demo

import dev.haliphax.ktorGrpc.proto.DemoRequest
import dev.haliphax.ktorGrpc.proto.DemoResponse
import dev.haliphax.ktorGrpc.proto.DemoServiceGrpcKt
import dev.haliphax.ktorGrpc.services.demo.controllers.DemoController
import io.grpc.BindableService
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
@Named("DemoService")
class DemoService(
  private val demoController: DemoController,
) :
  DemoServiceGrpcKt.DemoServiceCoroutineImplBase(),
  // allows finding this component via BindableService binding qualifier
  BindableService {

  override suspend fun demo(request: DemoRequest): DemoResponse {
    val input = request.message
    val output = demoController.test(input)
    return DemoResponse.newBuilder().setMessage(output).build()
  }
}
