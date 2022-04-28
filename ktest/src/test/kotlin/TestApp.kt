package com.haliphax.ktest.test

// 3rd party
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*
// local
import com.haliphax.ktest.data.*
import com.haliphax.ktest.test.clients.*

class TestApp {
	@Test
	fun testAuthBadAdmin() = testApplication {
		val client = badClient()
		val response = client.get("/admin")
		assertEquals(response.status, HttpStatusCode.Unauthorized)
	}

	@Test
	fun testAuthBadBasic() = testApplication {
		val client = badClient()
		val response = client.get("/basic")
		assertEquals(response.status, HttpStatusCode.Unauthorized)
	}

	@Test
	fun testAuthGoodAdmin() = testApplication {
		val client = adminClient()
		val response = client.get("/admin")
		assertEquals(response.status, HttpStatusCode.OK)
	}

	@Test
	fun testAuthGoodBasic() = testApplication {
		val client = basicClient()
		val response = client.get("/basic")
		assertEquals(response.status, HttpStatusCode.OK)
	}

	@Test
	fun testAuthUnauthorized() = testApplication {
		var response = client.get("/admin")
		assertEquals(response.status, HttpStatusCode.Unauthorized)
	}

	@Test
	fun testDataGet() = testApplication {
		var client = jsonClient()
		val response: TestData = client.get("/data").body()
		assertEquals(response.hello, true)
	}

	@Test
	fun testDataPost() = testApplication {
		val client = jsonClient()
		val response: TestData = client.post("/data") {
			contentType(ContentType.Application.Json)
			setBody(TestData(hello = false))
		}.body()
		assertEquals(response.hello, false)
	}
}
