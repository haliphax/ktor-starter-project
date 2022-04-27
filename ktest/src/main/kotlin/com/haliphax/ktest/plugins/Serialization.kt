package com.haliphax.ktest.plugins

// 3rd party
import kotlinx.serialization.json.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*

fun Application.configureSerialization() {
	install(ContentNegotiation) {
		json(Json {
			prettyPrint = true
			isLenient = true
		})
	}
}
