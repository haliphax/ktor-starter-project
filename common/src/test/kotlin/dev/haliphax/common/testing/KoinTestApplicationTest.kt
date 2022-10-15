package dev.haliphax.common.testing

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.ktor.server.testing.ApplicationTestBuilder
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject

class KoinTestApplicationTest : DescribeSpec({
  describe("koinTestApplication") {
    it("should inject the appropriate components") {
      val testModule = module {
        val testObject = single(named("test")) { "test" }
      }

      fun koinTest(block: ApplicationTestBuilder.() -> Unit) =
        koinTestApplication(testModule) {}

      koinTest {
        val obj1: String by inject(String::class.java, named("test"))
        val obj2: String by inject(String::class.java, named("test"))

        obj1 shouldBe "test"
        obj2 shouldBe obj1
      }
    }
  }
})
