package dev.haliphax

import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
  `test-report-aggregation`
  java
}

// aggregate reports from all subprojects
dependencies {
  subprojects.forEach {
    testReportAggregation(it)
  }
}

testing.suites {
  // integrationTest test suite
  create<JvmTestSuite>("integrationTest") {
    testType.set(TestSuiteType.INTEGRATION_TEST)
    dependencies { implementation(project()) }
  }

  // aggregate test suite
  create<JvmTestSuite>("allTest") {
    val suites = testing.suites.withType<JvmTestSuite>().filter {
      it != this
    }

    testType.set("all")

    // include class paths from main source set
    sources.compileClasspath += sourceSets.main.get().output
    sources.runtimeClasspath += sourceSets.main.get().output

    // include class paths from all other testing suites
    suites.map {
      val set = sourceSets.named(it.name).get()
      sources.compileClasspath += set.compileClasspath
      sources.runtimeClasspath += set.runtimeClasspath
    }

    dependencies {
      implementation(project())

      // include dependencies from all other testing suites
      suites.forEach { suite ->
        project.configurations.filter { config ->
          config.name.startsWith(suite.name)
        }.forEach { config ->
          config.dependencies.forEach { dependency ->
            implementation(dependency)
          }
        }
      }
    }

    targets.all {
      testTask {
        testClassesDirs = files(
          suites.map { "$buildDir/classes/kotlin/${it.name}" },
        )
        extensions.configure<JacocoTaskExtension> {
          setDestinationFile(file("$buildDir/jacoco/allTest.exec"))
        }
      }
    }
  }

  // JvmTestSuite configuration
  withType<JvmTestSuite> {
    useJUnitJupiter()

    targets.all {
      testTask {
        testLogging {
          showCauses = true
          showStackTraces = true
          exceptionFormat = TestExceptionFormat.SHORT
          events(
            TestLogEvent.PASSED,
            TestLogEvent.FAILED,
          )
        }
      }
    }
  }
}

java {
  sourceSets {
    named("integrationTest") {
      // include compiled classes from main source set in integrationTest
      compileClasspath += sourceSets.main.get().compileClasspath
      // include generated KSP sources
      java.srcDirs("build/generated/ksp/integrationTest/kotlin")
    }
  }
}
