package dev.haliphax

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  java
}

// version targeting
java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
  toolchain { languageVersion.set(JavaLanguageVersion.of(17)) }
}

tasks.withType<KotlinCompile> { kotlinOptions.jvmTarget = "17" }
