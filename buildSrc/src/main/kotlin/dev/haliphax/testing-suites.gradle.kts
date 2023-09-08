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
  }

  // aggregate test suite
  create<JvmTestSuite>("allTest") {
    testType.set("all-test")

    val suites = testing.suites.withType<JvmTestSuite>().filter {
      it != this
    }

    // include class paths from all other testing suites
    suites.map {
      val set = sourceSets.named(it.name).get()
      sources.compileClasspath += set.compileClasspath
      sources.runtimeClasspath += set.runtimeClasspath
    }

    // include dependencies from all other testing suites
    dependencies {
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

    // configure test task
    targets.all {
      testTask {
        // include kotlin source code
        testClassesDirs = files(
          suites.map { "$buildDir/classes/kotlin/${it.name}" },
        )
        // destination file for execution data
        extensions.configure<JacocoTaskExtension> {
          setDestinationFile(file("$buildDir/jacoco/allTest.exec"))
        }
      }
    }
  }

  // JvmTestSuite configuration
  withType<JvmTestSuite> {
    useJUnitJupiter()
    dependencies { implementation(project()) }

    targets.all {
      testTask {
        testLogging {
          showCauses = true
          showStackTraces = true
          exceptionFormat = TestExceptionFormat.SHORT
          events(
            TestLogEvent.SKIPPED,
            TestLogEvent.FAILED,
          )
        }
      }
    }
  }
}

// configure testing suite source sets
java {
  sourceSets {
    testing.suites.forEach {
      named(it.name).configure {
        // include compiled classes from main source set
        val mainSrc = sourceSets.main.get()
        compileClasspath += mainSrc.compileClasspath
        runtimeClasspath += mainSrc.runtimeClasspath
        // include generated KSP sources
        java.srcDirs("build/generated/ksp/$name/kotlin")
      }
    }
  }
}
