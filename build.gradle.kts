import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.ofSourceSet
import com.google.protobuf.gradle.plugins
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val grpcVersion: String by project
val grpcKotlinVersion: String by project
val koinKspVersion: String by project
val protobufVersion: String by project

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
    testReportAggregation(project(it.path))
  }
}

tasks.check {
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

  dependencies {
    // generate koin KSP sources
    ksp("io.insert-koin", "koin-ksp-compiler", koinKspVersion)
  }

  repositories {
    mavenCentral()
  }

  java {
    sourceCompatibility = JavaVersion.VERSION_1_8
  }

  sourceSets.main {
    // include generated KSP sources
    java.srcDirs("generated/ksp/main/kotlin")
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
    useJUnitPlatform()

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
  if (name == "common") {
    return@subprojects
  }

  apply(plugin = "application")
  apply(plugin = "com.github.johnrengelman.shadow")
  apply(plugin = "com.google.protobuf")

  tasks.build {
    dependsOn(tasks.withType<ShadowJar>())
  }

  // generate protobuffer sources
  protobuf {
    protoc {
      artifact = "com.google.protobuf:protoc:$protobufVersion"
    }

    plugins {
      id("grpc") {
        artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
      }
      id("grpcKt") {
        artifact = "io.grpc:protoc-gen-grpc-kotlin:$grpcKotlinVersion:jdk7@jar"
      }
    }

    generateProtoTasks {
      ofSourceSet("main").forEach {
        it.plugins {
          id("grpc")
          id("grpcKt")
        }
      }
    }
  }
}
