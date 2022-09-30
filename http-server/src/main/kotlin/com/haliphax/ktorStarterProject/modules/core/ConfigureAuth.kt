package com.haliphax.ktorStarterProject.modules.core

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.UserPasswordCredential
import io.ktor.server.auth.basic

private fun validAdminCredentials(credentials: UserPasswordCredential) =
	credentials.name == "admin" && credentials.password == "password"

fun Application.configureAuth() {
	install(Authentication) {
		basic("auth-basic") {
			validate { credentials ->
				if (
					validAdminCredentials(credentials) ||
					(credentials.name == "user" && credentials.password == "password")
				) {
					UserIdPrincipal(credentials.name)
				} else {
					null
				}
			}
		}

		basic("auth-admin") {
			validate { credentials ->
				if (validAdminCredentials(credentials)) {
					UserIdPrincipal(credentials.name)
				} else {
					null
				}
			}
		}
	}
}
