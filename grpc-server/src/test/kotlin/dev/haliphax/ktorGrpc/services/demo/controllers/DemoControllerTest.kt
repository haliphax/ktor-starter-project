package dev.haliphax.ktorGrpc.services.demo.controllers

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class DemoControllerTest : DescribeSpec({
  val demoController = DemoController()

  it("should return the expected message") {
    demoController.test("hungry") shouldBe "Hi, hungry, I'm Dad"
  }
})
