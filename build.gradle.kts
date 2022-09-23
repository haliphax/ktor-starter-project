import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("com.github.johnrengelman.shadow") apply false
	id("org.jetbrains.kotlin.jvm")
	id("org.jetbrains.kotlin.plugin.serialization") apply false
	id("org.jlleitschuh.gradle.ktlint")
	jacoco
	java
}

allprojects {
	apply(plugin = "jacoco")
	apply(plugin = "java")
	apply(plugin = "org.jetbrains.kotlin.jvm")
	apply(plugin = "org.jlleitschuh.gradle.ktlint")

	repositories {
		mavenCentral()
	}

	java {
		sourceCompatibility = JavaVersion.VERSION_17
	}

	tasks.withType<KotlinCompile> {
		kotlinOptions.jvmTarget = "1.8"
	}

	tasks.test {
		finalizedBy(tasks.jacocoTestReport)
	}

	tasks.jacocoTestReport {
		dependsOn(tasks.test)
	}

	tasks.withType<Test> {
		useJUnitPlatform()

		testLogging {
			showCauses = true
			showStackTraces = true
			showStandardStreams = true
			exceptionFormat = TestExceptionFormat.FULL
			events(
				TestLogEvent.PASSED,
				TestLogEvent.FAILED,
				TestLogEvent.SKIPPED
			)
		}
	}
}

subprojects {
	apply(plugin = "application")
	apply(plugin = "com.github.johnrengelman.shadow")
	apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

	// enable reproducible builds
	tasks.withType<AbstractArchiveTask> {
		isPreserveFileTimestamps = false
		isReproducibleFileOrder = true
	}
}
