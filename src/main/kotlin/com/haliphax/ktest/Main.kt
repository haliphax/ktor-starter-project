package com.haliphax.ktest

// 3rd party
import io.ktor.server.engine.*
import io.ktor.server.netty.*
// local
import com.haliphax.ktest.plugins.*

fun main() {
	embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
		configureRouting()
	}.start(wait = true)
}
