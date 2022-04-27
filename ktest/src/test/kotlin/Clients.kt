package com.haliphax.ktest.test.clients

// 3rd party
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*

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
