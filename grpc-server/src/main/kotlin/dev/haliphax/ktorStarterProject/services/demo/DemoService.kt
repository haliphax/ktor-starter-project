package dev.haliphax.ktorStarterProject.services.demo

import dev.haliphax.ktorStarterProject.proto.DemoRequest
import dev.haliphax.ktorStarterProject.proto.DemoResponse
import dev.haliphax.ktorStarterProject.proto.DemoServiceGrpcKt
import dev.haliphax.ktorStarterProject.services.demo.controllers.DemoController

class DemoService(
  private val demoController: DemoController = DemoController()
) :
  DemoServiceGrpcKt.DemoServiceCoroutineImplBase() {

  companion object {
    @JvmStatic
    @Suppress("unused") // application.conf
    fun newInstance(): DemoService = DemoService()
  }

  override suspend fun demo(request: DemoRequest): DemoResponse {
    val message = demoController.test()
    return DemoResponse.newBuilder().setMessage(message).build()
  }
}
