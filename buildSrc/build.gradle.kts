plugins {
  `kotlin-dsl`
}

repositories {
  mavenCentral()
  maven { url = uri("https://plugins.gradle.org/m2/") }
}

dependencies {
  implementation("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:1.8.20-1.0.10")
  implementation("gradle.plugin.com.github.sakata1222:plugin:1.4.0")
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.20")
  implementation(kotlin("script-runtime"))
}
