dependencies {
  implementation(project(":proto"))
  implementation(common.bundles.all)
  implementation(proto.bundles.api)
  implementation(server.bundles.grpc)

  testImplementation(coroutines.bundles.core)
  testImplementation(test.bundles.all)
}

application {
  mainClass.set("dev.haliphax.ktorGrpc.engine.EngineMain")
}

tasks.jar {
  manifest {
    attributes(mapOf("Main-Class" to "dev.haliphax.ktorGrpc.engine.EngineMain"))
  }
}
