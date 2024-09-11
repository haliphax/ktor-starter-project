import org.gradle.api.Project.GRADLE_PROPERTIES
import java.util.Properties
import kotlin.io.path.inputStream

// load gradle.properties from root project
Properties().apply {
  load(rootDir.toPath().resolveSibling(GRADLE_PROPERTIES).inputStream())
  forEach { (key, value) -> ext[key.toString()] = value }
}

plugins {
  `kotlin-dsl`
}

repositories {
  mavenCentral()
  maven { url = uri("https://plugins.gradle.org/m2/") }
}

dependencies {
  val kotlinVersion: String by project

  implementation(
    "com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:$kotlinVersion-1.0.25",
  )
  implementation("gradle.plugin.com.github.sakata1222:plugin:1.4.0")
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
  implementation(kotlin("script-runtime"))
}
