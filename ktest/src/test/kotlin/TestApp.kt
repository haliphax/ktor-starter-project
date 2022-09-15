package com.haliphax.ktest.test

import com.haliphax.ktest.data.TestData
import com.haliphax.ktest.test.clients.adminClient
import com.haliphax.ktest.test.clients.badClient
import com.haliphax.ktest.test.clients.basicClient
import com.haliphax.ktest.test.clients.jsonClient
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
import io.ktor.server.testing.testApplication
import kotlinx.coroutines.runBlocking

class TestApp : DescribeSpec({
	describe("application") {
		it("should serve the homepage") {
			testApplication {
				val client = basicClient()
				val response = client.get("/")

				response.bodyAsText() shouldBe "Hello, world!"
			}
		}

		describe("/admin endpoint") {
			it("should refuse access given bad credentials") {
				testApplication {
					val client = badClient("Admin access", "admin")
					val response = client.get("/admin")

					response.status shouldBe HttpStatusCode.Unauthorized
				}
			}

			it("should refuse access given no credentials") {
				testApplication {
					val response = client.get("/admin")

					response.status shouldBe HttpStatusCode.Unauthorized
				}
			}

			it("should permit access given good credentials") {
				testApplication {
					val client = adminClient()
					val response = client.get("/admin")

					response.status shouldBe HttpStatusCode.OK
				}
			}
		}

		describe("/basic endpoint") {
			it("should refuse access given bad credentials") {
				testApplication {
					val client = badClient("Basic access", "user")
					val response = client.get("/basic")

					response.status shouldBe HttpStatusCode.Unauthorized
				}
			}

			it("should refuse access given no credentials") {
				testApplication {
					val response = client.get("/basic")

					response.status shouldBe HttpStatusCode.Unauthorized
				}
			}

			it("should permit access given good credentials") {
				testApplication {
					val client = basicClient()
					val response = client.get("/basic")

					response.status shouldBe HttpStatusCode.OK
				}
			}
		}

		describe("/data endpoint") {
			it("should return expected JSON data") {
				testApplication {
					val client = jsonClient()
					val response: TestData = client.get("/data").body()

					response.hello shouldBe true
				}
			}

			it("should accept valid JSON data") {
				testApplication {
					val client = jsonClient()
					val response: TestData = client.post("/data") {
						contentType(ContentType.Application.Json)
						setBody(TestData(hello = false))
					}.body()

					response.hello shouldBe false
				}
			}

			it("should refuse invalid JSON data") {
				testApplication {
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
	}
})
