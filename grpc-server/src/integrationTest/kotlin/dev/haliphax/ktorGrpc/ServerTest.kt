package dev.haliphax.ktorGrpc

import dev.haliphax.ktorGrpc.proto.DemoRequest
import dev.haliphax.ktorGrpc.proto.DemoServiceGrpcKt.DemoServiceCoroutineStub
import io.grpc.Server
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.ksp.generated.module

class ServerTest : DescribeSpec({
  lateinit var demoServer: Server
  lateinit var demoServiceStub: DemoServiceCoroutineStub

  beforeContainer {
    startKoin {
      modules(TestDependencies().module)
      demoServer = koin.get()
      demoServiceStub = koin.get()
    }

    demoServer.start()
  }

  describe("DemoService") {
    it("responds appropriately") {
      val request = DemoRequest.newBuilder().setMessage("hungry").build()
      val response = demoServiceStub.demo(request)

      response.message shouldBe "Hi, hungry, I'm Dad"
    }
  }

  afterContainer {
    demoServer.shutdownNow()
    demoServer.awaitTermination()
    stopKoin()
  }
})
