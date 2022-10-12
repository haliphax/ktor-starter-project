@file:Suppress("UnstableApiUsage")

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  `test-report-aggregation`
  application
  id("com.github.johnrengelman.shadow") apply false
  id("com.google.devtools.ksp")
  id("com.google.protobuf") apply false
  id("org.jetbrains.kotlin.jvm")
  id("org.jetbrains.kotlin.plugin.serialization")
  id("org.jlleitschuh.gradle.ktlint")
  id("org.kordamp.gradle.jacoco")
  idea
  java
}

// aggregate reports from all subprojects
dependencies {
  subprojects.forEach {
    testReportAggregation(it)
  }
}

allprojects {
  group = "dev.haliphax"
  version = "unspecified"

  apply(plugin = "com.google.devtools.ksp")
  apply(plugin = "idea")
  apply(plugin = "java")
  apply(plugin = "org.jetbrains.kotlin.jvm")
  apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
  apply(plugin = "org.jlleitschuh.gradle.ktlint")

  repositories { mavenCentral() }

  dependencies {
    // generate koin KSP sources
    val koinKspVersion: String by project
    ksp("io.insert-koin", "koin-ksp-compiler", koinKspVersion)
  }

  config {
    coverage {
      jacoco {
        excludes = setOf(
          "**/*$*$*.class",
          "**/dev/haliphax/*/Dependencies.class",
          "**/dev/haliphax/*/MainKt.class",
          "**/dev/haliphax/*/aliases/**",
          "**/dev/haliphax/common/testing/KotestConfiguration.class",
          "**/generated/**",
          "**/proto/**"
        )
      }
    }
  }

  java { sourceCompatibility = JavaVersion.VERSION_1_8 }

  ktlint.filter { exclude { it.file.path.contains("/generated/") } }

  testing.suites.create<JvmTestSuite>("integrationTest") {
    testType.set(TestSuiteType.INTEGRATION_TEST)
    dependencies { implementation(project) }
  }

  sourceSets.main {
    // include generated KSP sources
    java.srcDirs("build/generated/ksp/main/kotlin")
  }

  sourceSets.named("integrationTest") {
    compileClasspath += sourceSets.main.get().compileClasspath
  }

  // enable reproducible builds
  tasks.withType<AbstractArchiveTask> {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
  }

  tasks.withType<KotlinCompile> { kotlinOptions.jvmTarget = "1.8" }

  afterEvaluate {
    tasks.withType<Test> {
      useJUnitPlatform { includeEngines() }
      testLogging {
        showCauses = true
        showStackTraces = true
        exceptionFormat = TestExceptionFormat.SHORT
        events(
          TestLogEvent.PASSED,
          TestLogEvent.FAILED
        )
      }
    }

    tasks.withType<JacocoReport> {
      reports {
        html.required.set(true)
        xml.required.set(true)
      }
    }
  }
}

subprojects {
  if (listOf("common", "proto").contains(name)) { return@subprojects }

  apply(plugin = "application")
  apply(plugin = "com.github.johnrengelman.shadow")

  tasks.build { dependsOn(tasks.withType<ShadowJar>()) }
  tasks.jar {
    manifest {
      attributes(mapOf("mainClass" to application.mainClass))
    }
  }
}

afterEvaluate {
  val allTest = tasks.register<TestReport>("allTest") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    dependsOn(
      tasks.named("integrationTestAggregateTestReport"),
      tasks.testAggregateTestReport
    )
    finalizedBy(tasks.named("aggregateJacocoReport"))
    testResults.setFrom(
      subprojects.map { it.tasks.withType<Test>() }.flatten()
    )
    destinationDirectory.set(file("$buildDir/reports/tests/all"))
  }

  tasks.check { dependsOn(allTest) }
}
