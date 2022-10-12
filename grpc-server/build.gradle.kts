@file:Suppress("UnstableApiUsage")

dependencies {
  implementation(project(":proto"))
  implementation(project(":common"))
  implementation(common.bundles.all)
  implementation(proto.bundles.api)
  implementation(server.bundles.grpc)

  testImplementation(coroutines.bundles.core)
  testImplementation(test.bundles.all)

  integrationTestImplementation(client.bundles.all)
  integrationTestImplementation(coroutines.bundles.core)
  integrationTestImplementation(server.grpc.netty)
  integrationTestImplementation(test.bundles.all)
}

application {
  mainClass.set("dev.haliphax.ktorGrpc.engine.EngineMain")
}
