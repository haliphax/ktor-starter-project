package dev.haliphax

import org.gradle.kotlin.dsl.dependencies

plugins {
  id("com.google.devtools.ksp")
  java
}

dependencies {
  // generate koin KSP sources
  val koinKspVersion: String by properties
  ksp("io.insert-koin", "koin-ksp-compiler", koinKspVersion)
}

sourceSets {
  main {
    // include generated KSP sources
    java.srcDirs("build/generated/ksp/main/kotlin")
  }
}
