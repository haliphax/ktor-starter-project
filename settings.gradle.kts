enableFeaturePreview("VERSION_CATALOGS")

pluginManagement {
  val kotlinVersion: String by settings

  plugins {
    id("com.github.johnrengelman.shadow").version("7.1.2")
    id("com.google.devtools.ksp").version("1.7.22-1.0.8")
    id("com.google.protobuf").version("0.9.2")
    id("idea")
    id("org.jetbrains.kotlin.jvm").version(kotlinVersion)
    id("org.jetbrains.kotlin.plugin.serialization").version(kotlinVersion)
    id("org.jlleitschuh.gradle.ktlint").version("11.1.0")
    id("org.kordamp.gradle.jacoco").version("0.49.0")
    id("test-report-aggregation")
  }
}

dependencyResolutionManagement {
  val coroutinesVersion: String by settings
  val koinKspVersion: String by settings
  val koinVersion: String by settings
  val kotlinVersion: String by settings
  val kotestVersion: String by settings
  val ktorVersion: String by settings
  val grpcVersion: String by settings
  val grpcKotlinVersion: String by settings
  val logbackVersion: String by settings
  val protobufVersion: String by settings

  @Suppress("UnstableApiUsage")
  versionCatalogs {
    create("common") {
      library("javax-annotation", "javax.annotation", "javax.annotation-api")
        .version("1.3.2")
      library("koin", "io.insert-koin", "koin-core")
        .version(koinVersion)
      library("koin-annotations", "io.insert-koin", "koin-annotations")
        .version(koinKspVersion)
      library("koin-ktor", "io.insert-koin", "koin-ktor")
        .version("3.3.0")
      library("kotlin", "org.jetbrains.kotlin", "kotlin-bom")
        .version(kotlinVersion)
      library("kotlinx-s18n-json", "org.jetbrains.kotlinx", "kotlinx-serialization-json")
        .version("1.4.1")
      library("ksp", "io.insert-koin", "koin-ksp-compiler")
        .version(koinKspVersion)
      library("ktor-s18n-json", "io.ktor", "ktor-serialization-kotlinx-json")
        .version(ktorVersion)
      library("logback", "ch.qos.logback", "logback-classic")
        .version(logbackVersion)

      bundle(
        "all",
        listOf(
          "javax-annotation",
          "koin",
          "koin-annotations",
          "koin-ktor",
          "kotlin",
          "kotlinx-s18n-json",
          "ksp",
          "ktor-s18n-json",
          "logback"
        )
      )
    }

    create("client") {
      library("auth", "io.ktor", "ktor-client-auth")
        .version(ktorVersion)
      library("content-negotiation", "io.ktor", "ktor-client-content-negotiation")
        .version(ktorVersion)
      library("s18n", "io.ktor", "ktor-client-serialization")
        .version(ktorVersion)

      bundle(
        "all",
        listOf(
          "auth",
          "content-negotiation",
          "s18n"
        )
      )
    }

    create("coroutines") {
      library("core", "org.jetbrains.kotlinx", "kotlinx-coroutines-core")
        .version(coroutinesVersion)
      library("core-jvm", "org.jetbrains.kotlinx", "kotlinx-coroutines-core-jvm")
        .version(coroutinesVersion)

      bundle(
        "core",
        listOf(
          "core",
          "core-jvm"
        )
      )
    }

    create("proto") {
      library("grpc-kotlin-stub", "io.grpc", "grpc-kotlin-stub")
        .version(grpcKotlinVersion)
      library("grpc-protobuf", "io.grpc", "grpc-protobuf")
        .version(grpcVersion)
      library("grpc-stub", "io.grpc", "grpc-stub")
        .version(grpcVersion)
      library("protobuf-java", "com.google.protobuf", "protobuf-java")
        .version(protobufVersion)
      library("protobuf-kotlin", "com.google.protobuf", "protobuf-kotlin")
        .version(protobufVersion)

      bundle(
        "api",
        listOf(
          "grpc-kotlin-stub",
          "grpc-protobuf",
          "grpc-stub"
        )
      )

      bundle(
        "all",
        listOf(
          "grpc-kotlin-stub",
          "grpc-protobuf",
          "grpc-stub",
          "protobuf-java",
          "protobuf-kotlin"
        )
      )
    }

    create("server") {
      library("auth", "io.ktor", "ktor-server-auth")
        .version(ktorVersion)
      library("content-negotiation", "io.ktor", "ktor-server-content-negotiation")
        .version(ktorVersion)
      library("core", "io.ktor", "ktor-server-core")
        .version(ktorVersion)
      library("grpc-netty", "io.grpc", "grpc-netty-shaded")
        .version(grpcVersion)
      library("grpc-services", "io.grpc", "grpc-services")
        .version(grpcVersion)
      library("netty", "io.ktor", "ktor-server-netty")
        .version(ktorVersion)

      bundle(
        "grpc",
        listOf(
          "grpc-netty",
          "grpc-services",
          "netty"
        )
      )

      bundle(
        "http",
        listOf(
          "auth",
          "content-negotiation",
          "core",
          "netty"
        )
      )
    }

    create("test") {
      val junitVersion: String by settings

      library("junit-api", "org.junit.jupiter", "junit-jupiter-api")
        .version(junitVersion)
      library("junit-engine", "org.junit.jupiter", "junit-jupiter-engine")
        .version(junitVersion)
      library("koin-test", "io.insert-koin", "koin-test")
        .version(koinVersion)
      library("koin-test-junit", "io.insert-koin", "koin-test-junit5")
        .version(koinVersion)
      library("kotest-assertion", "io.kotest", "kotest-assertions-core")
        .version(kotestVersion)
      library("kotest-runner", "io.kotest", "kotest-runner-junit5-jvm")
        .version(kotestVersion)
      library("mockk", "io.mockk", "mockk")
        .version("1.13.4")
      library("server-test-host", "io.ktor", "ktor-server-test-host")
        .version(ktorVersion)

      bundle(
        "all",
        listOf(
          "junit-api",
          "koin-test",
          "koin-test-junit",
          "kotest-assertion",
          "kotest-runner",
          "mockk",
          "server-test-host"
        )
      )
    }
  }
}

rootProject.name = "root"
include(
  "grpc-server",
  "http-server",
  "proto",
  "common"
)
