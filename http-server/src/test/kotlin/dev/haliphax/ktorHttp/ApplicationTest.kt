package dev.haliphax.ktorHttp

import dev.haliphax.ktorGrpc.proto.DemoResponse
import dev.haliphax.ktorGrpc.proto.DemoServiceGrpcKt.DemoServiceCoroutineStub
import dev.haliphax.ktorHttp.data.DemoData
import dev.haliphax.ktorHttp.helpers.adminClient
import dev.haliphax.ktorHttp.helpers.badClient
import dev.haliphax.ktorHttp.helpers.basicClient
import dev.haliphax.ktorHttp.helpers.jsonClient
import dev.haliphax.ktorHttp.helpers.koinTest
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.config.ApplicationConfig
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject
import org.koin.test.KoinTest

class ApplicationTest : KoinTest, DescribeSpec({
  describe("application") {
    it("should serve the homepage") {
      koinTest {
        val client = basicClient()
        val response = client.get("/")

        response.bodyAsText() shouldBe "Hello, world!"
      }
    }

    describe("/admin endpoint") {
      it("should refuse access given bad credentials") {
        koinTest {
          val client = badClient("admin")
          val response = client.get("/admin")

          response.status shouldBe HttpStatusCode.Unauthorized
        }
      }

      it("should refuse access given no credentials") {
        koinTest {
          val response = client.get("/admin")

          response.status shouldBe HttpStatusCode.Unauthorized
        }
      }

      it("should permit access given good credentials") {
        koinTest {
          val client = adminClient()
          val response = client.get("/admin")

          response.status shouldBe HttpStatusCode.OK
        }
      }
    }

    describe("/basic endpoint") {
      it("should refuse access given bad credentials") {
        koinTest {
          val client = badClient("user")
          val response = client.get("/basic")

          response.status shouldBe HttpStatusCode.Unauthorized
        }
      }

      it("should refuse access given no credentials") {
        koinTest {
          val response = client.get("/basic")

          response.status shouldBe HttpStatusCode.Unauthorized
        }
      }

      it("should permit access given admin credentials") {
        koinTest {
          val client = adminClient()
          val response = client.get("/basic")

          response.status shouldBe HttpStatusCode.OK
        }
      }

      it("should permit access given basic credentials") {
        koinTest {
          val client = basicClient()
          val response = client.get("/basic")

          response.status shouldBe HttpStatusCode.OK
        }
      }
    }

    describe("/data endpoint") {
      it("should return expected JSON data") {
        koinTest {
          val client = jsonClient()
          val response: DemoData = client.get("/data").body()

          response.message shouldBe "Testing"
        }
      }

      it("should accept valid JSON data") {
        koinTest {
          val client = jsonClient()
          val response: DemoData = client.post("/data") {
            contentType(ContentType.Application.Json)
            setBody(DemoData(message = "testing"))
          }.body()

          response.message shouldBe "Modified testing"
        }
      }

      it("should refuse invalid JSON data") {
        koinTest {
          val client = jsonClient()

          shouldThrowAny {
            runBlocking {
              client.post("/data") {
                contentType(ContentType.Application.Json)
                setBody("{\"badprop\":\"1\"}")
              }
            }
          }
        }
      }
    }

    describe("/grpc endpoint") {
      it("should call the gRPC service") {
        koinTest {
          val grpcStub: DemoServiceCoroutineStub
            by inject(DemoServiceCoroutineStub::class.java) {
              parametersOf(ApplicationConfig(null))
            }

          coEvery { grpcStub.demo(any()) } returns(
            DemoResponse.newBuilder().setMessage("Hi, hungry, I'm Dad").build()
            )

          jsonClient().post("/grpc") {
            contentType(ContentType.Application.Json)
            setBody(DemoData("hungry"))
          }

          coVerify { grpcStub.demo(any(), any()) }
        }
      }
    }
  }
})
