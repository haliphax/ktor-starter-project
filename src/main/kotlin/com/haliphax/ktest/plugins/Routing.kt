package com.haliphax.ktest.plugins

// 3rd party
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
	routing {
		get("/") {
			call.respondText("Hello, world!")
		}
	}
}
