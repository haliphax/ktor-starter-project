dependencies {
  implementation(common.logback)
  implementation(common.koin)
  implementation(test.kotest.runner)
  implementation(test.server.test.host)

  testImplementation(test.bundles.all)
}
