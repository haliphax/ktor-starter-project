@file:Suppress("UnstableApiUsage")

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
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
  id("test-report-aggregation")
  idea
  jacoco
  java
}

dependencies {
  // aggregate test reports from all subprojects
  subprojects.map {
    testReportAggregation(it)
  }
}

tasks.check {
  testing.suites.findByName("integrationTest")?.let {
    dependsOn(it)
  }

  dependsOn(tasks.testAggregateTestReport)
}

tasks.testAggregateTestReport {
  finalizedBy(jacocoMergedTestReport)
}

val jacocoMergedTestReport = tasks.create(
  "jacocoMergedTestReport",
  JacocoReport::class
) {
  group = "verification"

  afterEvaluate {
    fun Project.getReportTask() =
      this.tasks.withType<JacocoReport>().first()

    subprojects.map { dependsOn(it.tasks.jacocoTestReport) }

    sourceDirectories.setFrom(
      subprojects.map { it.getReportTask().sourceDirectories }
    )
    classDirectories.setFrom(
      subprojects.map { it.getReportTask().classDirectories }
    )
    executionData.setFrom(
      fileTree(".").include("**/build/jacoco/test.exec")
    )

    reports {
      html.required.set(true)
      xml.required.set(true)
    }
  }
}

allprojects {
  group = "dev.haliphax"
  version = "unspecified"

  apply(plugin = "com.google.devtools.ksp")
  apply(plugin = "idea")
  apply(plugin = "jacoco")
  apply(plugin = "java")
  apply(plugin = "org.jetbrains.kotlin.jvm")
  apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
  apply(plugin = "org.jlleitschuh.gradle.ktlint")

  repositories {
    mavenCentral()
  }

  dependencies {
    // generate koin KSP sources
    ksp("io.insert-koin", "koin-ksp-compiler", koinKspVersion)
    kspTest("io.insert-koin", "koin-ksp-compiler", koinKspVersion)
  }

  java {
    sourceCompatibility = JavaVersion.VERSION_1_8
  }

  ktlint {
    filter {
      exclude { it.file.path.contains("/generated/") }
    }
  }

  sourceSets.main {
    // include generated KSP sources
    java.srcDirs("build/generated/ksp/main/kotlin")
  }

  tasks.jacocoTestReport {
    dependsOn(tasks.test)

    classDirectories.setFrom(
      files(
        classDirectories.files.map {
          fileTree(it) {
            // ignore files
            exclude(
              "**/*$*$*.class",
              "**/dev/haliphax/*/Dependencies.class",
              "**/dev/haliphax/*/MainKt.class",
              "**/dev/haliphax/*/aliases/**",
              "**/generated/**",
              "**/proto/**"
            )
          }
        }
      )
    )
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
    useJUnitPlatform {
      includeEngines()
    }

    testLogging {
      showCauses = true
      showStackTraces = true
      exceptionFormat = TestExceptionFormat.SHORT
      events(
        TestLogEvent.PASSED,
        TestLogEvent.FAILED,
        TestLogEvent.SKIPPED
      )
    }
  }
}

subprojects {
  if (listOf("common", "proto").contains(name)) {
    return@subprojects
  }

  apply(plugin = "application")
  apply(plugin = "com.github.johnrengelman.shadow")

  tasks.build {
    dependsOn(tasks.withType<ShadowJar>())
  }
}
