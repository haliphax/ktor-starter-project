import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val koinKspVersion: String by project

plugins {
  id("com.github.johnrengelman.shadow") apply false
  id("com.google.devtools.ksp")
  id("com.google.protobuf") apply false
  id("org.jetbrains.kotlin.jvm")
  id("org.jetbrains.kotlin.plugin.serialization")
  id("org.jlleitschuh.gradle.ktlint")
  idea
  jacoco
  java
}

allprojects {
  apply(plugin = "com.google.devtools.ksp")
  apply(plugin = "idea")
  apply(plugin = "jacoco")
  apply(plugin = "org.jetbrains.kotlin.jvm")
  apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
  apply(plugin = "org.jlleitschuh.gradle.ktlint")
  apply(plugin = "java")

  dependencies {
    ksp("io.insert-koin", "koin-ksp-compiler", koinKspVersion)
  }

  group = "dev.haliphax"
  version = "unspecified"

  repositories {
    mavenCentral()
  }

  java {
    sourceCompatibility = JavaVersion.VERSION_1_8
  }

  sourceSets.main {
    java.srcDirs("build/generated/ksp/main/kotlin")
  }

  tasks.jacocoTestReport {
    reports {
      xml.required.set(true)
    }

    dependsOn(tasks.test)
  }

  afterEvaluate {
    tasks.jacocoTestReport {
      classDirectories.setFrom(
        files(
          classDirectories.files.map {
            fileTree(it) {
              exclude(
                "**/*$*$*.class",
                "**/dev/haliphax/ktorStarterProject/MainKt.class",
                "**/dev/haliphax/ktorStarterProject/Dependencies.class",
                "**/dev/haliphax/ktorStarterProject/proto/*",
                "**/generated/*"
              )
            }
          }
        )
      )
    }
  }

  tasks.test {
    finalizedBy(tasks.jacocoTestReport)
  }

  // enable reproducible builds
  tasks.withType<AbstractArchiveTask> {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
  }

  tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
  }

  tasks.withType<Test> {
    useJUnitPlatform()

    testLogging {
      showCauses = true
      showStackTraces = true
      showStandardStreams = true
      exceptionFormat = TestExceptionFormat.FULL
      events(
        TestLogEvent.PASSED,
        TestLogEvent.FAILED,
        TestLogEvent.SKIPPED
      )
    }
  }
}

subprojects {
  apply(plugin = "application")
  apply(plugin = "com.github.johnrengelman.shadow")

  tasks.build {
    dependsOn(tasks.findByName("shadowJar"))
  }
}
