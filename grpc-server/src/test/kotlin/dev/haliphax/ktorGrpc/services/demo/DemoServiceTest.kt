package dev.haliphax.ktorGrpc.services.demo

import dev.haliphax.ktorGrpc.proto.DemoRequest
import dev.haliphax.ktorGrpc.services.demo.controllers.DemoController
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class DemoServiceTest : DescribeSpec({
  describe("DemoService") {
    lateinit var demoService: DemoService
    lateinit var demoController: DemoController

    beforeContainer {
      demoController = mockk(relaxed = true)
      demoService = DemoService(
        demoController = demoController
      )
    }

    describe("test") {
      every { demoController.test() } returns("Testing")
      val request = DemoRequest.getDefaultInstance()
      val result = demoService.demo(request)

      it("should call DemoController.test") {
        verify { demoController.test() }
      }

      it("should return the message from DemoController.test") {
        result.message shouldBe "Testing"
      }
    }
  }
})
