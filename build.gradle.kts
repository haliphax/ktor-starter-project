// test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("com.github.johnrengelman.shadow") apply false
	id("org.jetbrains.kotlin.jvm")
	id("org.jetbrains.kotlin.plugin.serialization")
	id("org.jlleitschuh.gradle.ktlint")
	jacoco
	java
}

allprojects {
	apply(plugin = "jacoco")
	apply(plugin = "org.jetbrains.kotlin.jvm")
	apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
	apply(plugin = "org.jlleitschuh.gradle.ktlint")
	apply(plugin = "java")

	java {
		sourceCompatibility = JavaVersion.VERSION_17
	}

	repositories {
		mavenCentral()
	}

	tasks.jacocoTestReport {
		dependsOn(tasks.test)
	}

	afterEvaluate {
		tasks.jacocoTestReport {
			classDirectories.setFrom(
				files(
					classDirectories.files.map {
						fileTree(it) {
							exclude(
								"**/*$*$*.class",
								"**/com/haliphax/ktorStarterProject/MainKt.class"
							)
						}
					}
				)
			)
		}
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

	tasks.build {
		dependsOn(tasks.findByName("shadowJar"))
	}
}
