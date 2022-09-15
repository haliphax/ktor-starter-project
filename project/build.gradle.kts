plugins {
	id("com.github.johnrengelman.shadow")
}

dependencies {
	implementation(basic.bundles.all)
	implementation(server.bundles.all)

	testImplementation(client.bundles.all)
	testImplementation(coroutines.bundles.core)
	testImplementation(test.bundles.all)
}

application {
	mainClass.set("io.ktor.server.netty.EngineMain")
}

tasks.jar {
	manifest {
		attributes(mapOf("Main-Class" to "io.ktor.server.netty.EngineMain"))
	}
}
