dependencies {
  implementation(common.logback)
  implementation(common.koin)
  implementation(test.server.test.host)

  testImplementation(test.bundles.all)
}
