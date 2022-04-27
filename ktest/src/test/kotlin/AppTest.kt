// 3rd party
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlin.test.*
// local
import com.haliphax.ktest.data.*

class ApplicationTest {
	@Test
	fun testRoot() = testApplication {
		val client = createClient {
			install(ContentNegotiation) {
				json()
			}
		}
		val response: TestData = client.get("/").body()
		assertEquals(response.hello, true)
	}
}
