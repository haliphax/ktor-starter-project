enableFeaturePreview("VERSION_CATALOGS")

pluginManagement {
	val kotlin_version: String by settings

	plugins {
		id("com.github.johnrengelman.shadow").version("7.1.2")
		id("org.jetbrains.kotlin.jvm").version(kotlin_version)
		id("org.jetbrains.kotlin.plugin.serialization").version(kotlin_version)
		id("org.jlleitschuh.gradle.ktlint").version("11.0.0")
	}
}

dependencyResolutionManagement {
	val coroutines_version: String by settings
	val kotlin_version: String by settings
	val kotest_version: String by settings
	val ktor_version: String by settings
	val logback_version: String by settings

	versionCatalogs {
		create("coroutines") {
			library("core", "org.jetbrains.kotlinx", "kotlinx-coroutines-core").version(coroutines_version)
			library("core-jvm", "org.jetbrains.kotlinx", "kotlinx-coroutines-core-jvm").version(coroutines_version)
			bundle("core", listOf("core", "core-jvm"))
		}
		create("basic") {
			library("kotlin", "org.jetbrains.kotlin", "kotlin-bom").version(kotlin_version)
			library("kotlinx-s18n-json", "org.jetbrains.kotlinx", "kotlinx-serialization-json").version("1.3.2")
			library("ktor-s18n-json", "io.ktor", "ktor-serialization-kotlinx-json").version(ktor_version)
			library("logback", "ch.qos.logback", "logback-classic").version(logback_version)
			bundle("all", listOf("kotlin", "kotlinx-s18n-json", "ktor-s18n-json", "logback"))
		}
		create("server") {
			library("auth", "io.ktor", "ktor-server-auth").version(ktor_version)
			library("content-negotiation", "io.ktor", "ktor-server-content-negotiation").version(ktor_version)
			library("core", "io.ktor", "ktor-server-core").version(ktor_version)
			library("netty", "io.ktor", "ktor-server-netty").version(ktor_version)
			bundle("all", listOf("auth", "content-negotiation", "core", "netty"))
		}
		create("client") {
			library("auth", "io.ktor", "ktor-client-auth").version(ktor_version)
			library("content-negotiation", "io.ktor", "ktor-client-content-negotiation").version(ktor_version)
			library("s18n", "io.ktor", "ktor-client-serialization").version(ktor_version)
			bundle("all", listOf("auth", "content-negotiation", "s18n"))
		}
		create("test") {
			library("server-host", "io.ktor", "ktor-server-test-host").version(ktor_version)
			library("kotlin-test", "org.jetbrains.kotlin", "kotlin-test").version(kotlin_version)
			library("kotest-assertion", "io.kotest", "kotest-assertions-core").version(kotest_version)
			library("kotest-runner", "io.kotest", "kotest-runner-junit5-jvm").version(kotest_version)
			library("mockk", "io.mockk", "mockk").version("1.12.7")
			bundle("all", listOf("server-host", "kotest-assertion", "kotest-runner", "mockk"))
		}
	}
}

rootProject.name = "root"
include("ktest")
