// stdlib
import kotlin.test.*
// 3rd party
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.testing.*
import kotlinx.coroutines.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
// local
import com.haliphax.ktest.plugins.Testing

class ApplicationTest {
	@Test
	fun testRoot() = testApplication {
		val client = createClient {
			install(ContentNegotiation) {
				json()
			}
		}
		val response: Testing = client.get("/").body()
		assertEquals(response.hello, true)
	}
}
