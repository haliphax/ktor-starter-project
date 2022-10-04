package dev.haliphax.ktorStarterProject

import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.ApplicationTestBuilder

fun ApplicationTestBuilder.adminClient() = createClient {
  install(Auth) {
    basic {
      credentials {
        BasicAuthCredentials("admin", "password")
      }
    }
  }
}

fun ApplicationTestBuilder.badClient(username: String = "bad") = createClient {
  install(Auth) {
    basic {
      credentials {
        BasicAuthCredentials(username, "bad")
      }
    }
  }
}

fun ApplicationTestBuilder.basicClient() = createClient {
  install(Auth) {
    basic {
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
