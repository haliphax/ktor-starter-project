plugins {
  id("org.jetbrains.kotlin.jvm")
  java

  // load for use in subprojects but do not apply these to the root project
  id("org.jetbrains.kotlin.plugin.serialization") apply false
  id("com.github.johnrengelman.shadow") apply false
  id("com.google.protobuf") apply false
}

allprojects {
  group = "dev.haliphax"
  version = "unspecified"

  repositories {
    mavenCentral()
  }

  apply(plugin = "dev.haliphax.ksp")
  apply(plugin = "dev.haliphax.reporting")
  apply(plugin = "dev.haliphax.reproducible-builds")
  apply(plugin = "dev.haliphax.testing-suites")
  apply(plugin = "dev.haliphax.version-targeting")

  dependencies {
    constraints {
      implementation("net.bytebuddy", "byte-buddy", "[1.10.21,)") {
        because("previous versions do not support this project's JDK")
      }
    }
  }
}

// subprojects config
subprojects {
  apply(plugin = "org.jetbrains.kotlin.jvm")
  apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

  // skip non-application projects
  if (listOf("common", "proto").contains(name)) return@subprojects

  apply(plugin = "application")
  apply(plugin = "com.github.johnrengelman.shadow")
}

tasks.named("check") {
  dependsOn(
    tasks.named("allTest"),
    tasks.named("allTestCoverageVerification"),
  )
}

// do not build JAR file for root project
tasks.jar { isEnabled = false }
