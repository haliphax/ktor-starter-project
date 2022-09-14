package com.haliphax.ktest.test

import com.haliphax.ktest.data.TestData
import com.haliphax.ktest.test.clients.adminClient
import com.haliphax.ktest.test.clients.badClient
import com.haliphax.ktest.test.clients.basicClient
import com.haliphax.ktest.test.clients.jsonClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals

class TestApp {
	@Test
	fun testLoadHomepage() = testApplication {
		val client = basicClient()
		val response = client.get("/")
		assertEquals(response.bodyAsText(), "Hello, world!")
	}

	@Test
	fun testAuthBadAdmin() = testApplication {
		val client = badClient("Admin access", "admin")
		val response = client.get("/admin")
		assertEquals(response.status, HttpStatusCode.Unauthorized)
	}

	@Test
	fun testAuthBadBasic() = testApplication {
		val client = badClient("Basic access", "user")
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
