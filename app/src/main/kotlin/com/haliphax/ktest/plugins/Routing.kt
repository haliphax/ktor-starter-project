package com.haliphax.ktest.plugins

// stdlib
import kotlinx.serialization.*
// 3rd party
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

@Serializable data class Testing(val hello: Boolean)

fun Application.configureRouting() {
	routing {
		get("/") {
			val obj = Testing(hello = true)
			call.respond(obj)
		}
	}
}
