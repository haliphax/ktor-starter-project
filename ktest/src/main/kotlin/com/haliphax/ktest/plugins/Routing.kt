package com.haliphax.ktest.plugins

// 3rd party
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
// local
import com.haliphax.ktest.data.*

fun Application.configureRouting() {
	routing {
		authenticate("auth-admin") {
			get("/admin") {
				call.respondText("Authorized")
			}
		}

		authenticate("auth-basic") {
			get("/basic") {
				call.respondText("Authorized")
			}
		}

		get("/") {
			call.respondText("Hello, world!")
		}

		get("/data") {
			val obj = TestData(hello = true)
			call.respond(obj)
		}
	}
}
