package com.haliphax.ktorStarterProject.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.basic

fun Application.configureAuth() {
	install(Authentication) {
		basic("auth-basic") {
			realm = "Basic access"
			validate { credentials ->
				if (credentials.name != "user" || credentials.password != "password") {
					null
				} else {
					UserIdPrincipal(credentials.name)
				}
			}
		}

		basic("auth-admin") {
			realm = "Admin access"
			validate { credentials ->
				if (credentials.name != "admin" || credentials.password != "password") {
					null
				} else {
					UserIdPrincipal(credentials.name)
				}
			}
		}
	}
}
