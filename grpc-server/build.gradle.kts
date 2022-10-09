@file:Suppress("UnstableApiUsage")

dependencies {
  implementation(project(":common"))
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

testing {
  suites {
    create("integrationTest", JvmTestSuite::class) {
      dependencies {
        implementation(project(":common"))
        implementation(project(":grpc-server"))
        implementation(project(":proto"))
        implementation(client.bundles.all)
        implementation(common.bundles.all)
        implementation(coroutines.bundles.core)
        implementation(proto.bundles.api)
        implementation(server.grpc.netty)
        implementation(test.bundles.all)
      }
    }
  }
}

tasks.jar {
  manifest {
    attributes(mapOf("Main-Class" to "dev.haliphax.ktorGrpc.engine.EngineMain"))
  }
}
