package dev.haliphax.ktorGrpc.services.demo

import dev.haliphax.ktorGrpc.proto.DemoRequest
import dev.haliphax.ktorGrpc.services.demo.controllers.DemoController
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class DemoServiceTest : DescribeSpec(
  {
    describe("DemoService") {
      lateinit var demoService: DemoService
      lateinit var demoController: DemoController

      beforeEach {
        demoController = mockk(relaxed = true)
        demoService = DemoService(
          demoController = demoController,
        )
      }

      it("should call DemoController.test") {
        val request = DemoRequest.newBuilder().setMessage("test").build()
        demoService.demo(request)

        verify { demoController.test(any()) }
      }

      it("should return the message from DemoController.test") {
        every { demoController.test(any()) } returns "test"

        val request = DemoRequest.newBuilder().setMessage("test").build()
        val result = demoService.demo(request)

        result.message shouldBe "test"
      }
    }
  },
)
