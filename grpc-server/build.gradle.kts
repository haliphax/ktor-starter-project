dependencies {
  implementation(common.bundles.all)
  implementation(proto.bundles.all)
  implementation(server.bundles.grpc)

  testImplementation(coroutines.bundles.core)
  testImplementation(test.bundles.all)
}

application {
  mainClass.set("dev.haliphax.ktorStarterProject.MainKt")
}

tasks.jar {
  manifest {
    attributes(mapOf("Main-Class" to "dev.haliphax.ktorStarterProject.MainKt"))
  }
}
