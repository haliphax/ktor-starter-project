package com.haliphax.ktest.plugins

// stdlib
import kotlinx.serialization.json.*
// 3rd party
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
