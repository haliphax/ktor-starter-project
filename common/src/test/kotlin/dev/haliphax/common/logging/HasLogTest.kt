package dev.haliphax.common.logging

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class HasLogTest : DescribeSpec({
  describe("HasLogTest") {
    it("provides a logger for the given class") {
      class HasLogTest : HasLog

      val obj = HasLogTest()

      obj.log.name shouldBe (HasLogTest::class.java).name
    }
  }
})
