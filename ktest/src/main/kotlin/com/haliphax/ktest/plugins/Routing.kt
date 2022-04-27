package com.haliphax.ktest.plugins

// 3rd party
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
// local
import com.haliphax.ktest.data.*

fun Application.configureRouting() {
	routing {
		get("/") {
			val obj = TestData(hello = true)
			call.respond(obj)
		}
	}
}
