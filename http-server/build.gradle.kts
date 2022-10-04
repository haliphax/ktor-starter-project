dependencies {
  implementation(common.bundles.all)
  implementation(server.bundles.http)

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
