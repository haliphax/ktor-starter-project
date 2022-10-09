@file:Suppress("UnstableApiUsage")

dependencies {
  implementation(project(":proto"))
  implementation(common.bundles.all)
  implementation(proto.bundles.api)
  implementation(server.bundles.http)
  implementation(server.grpc.netty)

  testImplementation(project(":common"))
  testImplementation(project(":grpc-server"))
  testImplementation(client.bundles.all)
  testImplementation(coroutines.bundles.core)
  testImplementation(test.bundles.all)
  testRuntimeOnly(test.junit.engine)
}

application {
  mainClass.set("io.ktor.server.netty.EngineMain")
}

tasks.jar {
  manifest {
    attributes(mapOf("Main-Class" to "io.ktor.server.netty.EngineMain"))
  }
}

tasks.jacocoTestReport {
  classDirectories.setFrom(
    files(
      classDirectories.files.map {
        fileTree(it) {
          exclude(
            "**/dev/haliphax/ktorHttp/modules/dependencies/DependenciesKt.class",
          )
        }
      }
    )
  )
}
