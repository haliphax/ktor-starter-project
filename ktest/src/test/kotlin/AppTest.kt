// 3rd party
import io.ktor.client.call.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.events.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.testing.*
import kotlin.test.*
import kotlinx.serialization.json.*
// local
import com.haliphax.ktest.data.*

fun ApplicationTestBuilder.adminClient() = createClient {
	install(Auth) {
		basic {
			realm = "Admin access"
			credentials {
				BasicAuthCredentials("admin", "password")
			}
		}
	}
}

fun ApplicationTestBuilder.basicClient() = createClient {
	install(Auth) {
		basic {
			realm = "Basic access"
			credentials {
				BasicAuthCredentials("user", "password")
			}
		}
	}
}

fun ApplicationTestBuilder.jsonClient() = createClient {
	install(ContentNegotiation) {
		json()
	}
}

class AppTest {
	@Test
	fun testAuthBadAdmin() = testApplication {
		var response = client.get("/admin")
		assertEquals(response.status, HttpStatusCode.Unauthorized)
	}

	@Test
	fun testAuthBadBasic() = testApplication {
		var response = client.get("/basic")
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
	fun testData() = testApplication {
		var client = jsonClient()
		val response: TestData = client.get("/data").body()
		assertEquals(response.hello, true)
	}
}
