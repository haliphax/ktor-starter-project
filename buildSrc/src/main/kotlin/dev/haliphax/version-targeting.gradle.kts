package dev.haliphax

import org.gradle.api.JavaVersion
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  java
}

// version targeting
java {
  sourceCompatibility = JavaVersion.VERSION_19
  targetCompatibility = JavaVersion.VERSION_19
  toolchain { languageVersion.set(JavaLanguageVersion.of(19)) }
}

tasks.withType<KotlinCompile> { kotlinOptions.jvmTarget = "19" }
