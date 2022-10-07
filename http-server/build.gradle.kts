dependencies {
  api(project(":proto"))
  implementation(common.bundles.all)
  implementation(proto.bundles.api)
  implementation(server.bundles.http)
  implementation(server.grpc.netty)

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
