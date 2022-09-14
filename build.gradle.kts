import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlin_version: String by project

plugins {
	id("com.github.johnrengelman.shadow") apply false
	id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
	id("org.jetbrains.kotlin.jvm")
	id("org.jetbrains.kotlin.plugin.serialization") apply false
}

allprojects {
	repositories {
		mavenCentral()
	}
	tasks.withType<KotlinCompile> {
		kotlinOptions.jvmTarget = "1.8"
	}
}

subprojects {
	apply(plugin = "application")
	apply(plugin = "jacoco")
	apply(plugin = "com.github.johnrengelman.shadow")
	apply(plugin = "org.jlleitschuh.gradle.ktlint")
	apply(plugin = "org.jetbrains.kotlin.jvm")
	apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
}
