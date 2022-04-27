package com.haliphax.ktest.plugins

import io.ktor.server.auth.*
import io.ktor.server.application.*

fun Application.configureAuth() {
	install(Authentication) {
		basic("auth-basic") {
			realm = "Basic access"
			validate { credentials ->
				if (credentials.name != "user" || credentials.password != "password") {
					null
				}
				else {
					UserIdPrincipal(credentials.name)
				}
			}
		}

		basic("auth-admin") {
			realm = "Admin access"
			validate { credentials ->
				if (credentials.name != "admin" || credentials.password != "password")
				{
					null
				}
				else {
					UserIdPrincipal(credentials.name)
				}
			}
		}
	}
}
