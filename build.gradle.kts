import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlin_version: String by project

plugins {
	java
	id("com.github.johnrengelman.shadow") apply false
	id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
	id("org.jetbrains.kotlin.jvm")
	id("org.jetbrains.kotlin.plugin.serialization") apply false
}

allprojects {
	apply(plugin = "jacoco")
	apply(plugin = "java")
	apply(plugin = "com.github.johnrengelman.shadow")
	apply(plugin = "org.jlleitschuh.gradle.ktlint")
	apply(plugin = "org.jetbrains.kotlin.jvm")

	repositories {
		mavenCentral()
	}

	java {
		sourceCompatibility = JavaVersion.VERSION_17
	}

	tasks.withType<KotlinCompile> {
		kotlinOptions.jvmTarget = "1.8"
	}
}

subprojects {
	apply(plugin = "application")
	apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
}
