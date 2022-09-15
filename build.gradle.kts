import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	jacoco
	java
	id("com.github.johnrengelman.shadow") apply false
	id("org.jlleitschuh.gradle.ktlint")
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

	tasks.test {
		finalizedBy(tasks.jacocoTestReport)
	}

	tasks.jacocoTestReport {
		dependsOn(tasks.test)
	}

	tasks.withType<KotlinCompile> {
		kotlinOptions.jvmTarget = "1.8"
	}

	tasks.withType<Test> {
		useJUnitPlatform()
		testLogging.showStandardStreams = true

		testLogging {
			showCauses = true
			showStackTraces = true
			showStandardStreams = true
			events(
				TestLogEvent.PASSED,
				TestLogEvent.FAILED,
				TestLogEvent.SKIPPED
			)
			exceptionFormat = TestExceptionFormat.FULL
		}
	}
}

subprojects {
	apply(plugin = "application")
	apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
}
