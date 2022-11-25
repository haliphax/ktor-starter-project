import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  `test-report-aggregation`
  application
  id("com.google.devtools.ksp")
  id("org.jetbrains.kotlin.jvm")
  id("org.jetbrains.kotlin.plugin.serialization")
  id("org.jlleitschuh.gradle.ktlint")
  id("org.kordamp.gradle.jacoco")
  java

  // load for use in subprojects but do not apply these to the root project
  id("com.github.johnrengelman.shadow") apply false
  id("com.google.protobuf") apply false
}

// aggregate reports from all subprojects
dependencies { subprojects.forEach { testReportAggregation(it) } }

allprojects {
  group = "dev.haliphax"
  version = "unspecified"

  apply(plugin = "com.google.devtools.ksp")
  apply(plugin = "idea")
  apply(plugin = "java")
  apply(plugin = "org.jetbrains.kotlin.jvm")
  apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
  apply(plugin = "org.jlleitschuh.gradle.ktlint")

  repositories { mavenCentral() }

  dependencies {
    // generate koin KSP sources
    val koinKspVersion: String by project
    ksp("io.insert-koin", "koin-ksp-compiler", koinKspVersion)
  }

  // version targeting
  java { sourceCompatibility = JavaVersion.VERSION_1_8 }
  tasks.withType<KotlinCompile> { kotlinOptions.jvmTarget = "1.8" }

  // exclude generated source from ktlint targets
  ktlint.filter { exclude { it.file.path.contains("/generated/") } }

  // jacoco exclusions
  config.coverage.jacoco.excludes = setOf(
    "**/*$*$*.class",
    "**/dev/haliphax/*/Dependencies*.class",
    "**/dev/haliphax/*/MainKt.class",
    "**/dev/haliphax/*/aliases/**",
    "**/dev/haliphax/common/testing/KotestConfiguration.class",
    "**/generated/**",
    "**/proto/**"
  )

  // integrationTest test suite
  @Suppress("UnstableApiUsage")
  testing.suites.create<JvmTestSuite>("integrationTest") {
    testType.set(TestSuiteType.INTEGRATION_TEST)
    dependencies { implementation(project()) }
  }

  // include generated KSP sources
  sourceSets.main { java.srcDirs("build/generated/ksp/main/kotlin") }

  // integrationTest source set
  sourceSets.named("integrationTest") {
    // include compiled classes from main source set
    compileClasspath += sourceSets.main.get().compileClasspath
  }

  // enable reproducible builds
  tasks.withType<AbstractArchiveTask> {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
  }

  // testing configuration
  tasks.withType<Test> {
    useJUnitPlatform { includeEngines() }
    testLogging {
      showCauses = true
      showStackTraces = true
      exceptionFormat = TestExceptionFormat.SHORT
      events(
        TestLogEvent.PASSED,
        TestLogEvent.FAILED
      )
    }
  }

  // common jacoco report settings
  tasks.withType<JacocoReport> {
    reports {
      html.required.set(true)
      xml.required.set(true)
    }
  }
}

// subprojects shadowJar config
subprojects {
  // skip non-application projects
  if (listOf("common", "proto").contains(name)) return@subprojects

  apply(plugin = "application")
  apply(plugin = "com.github.johnrengelman.shadow")

  tasks.jar {
    manifest { attributes(mapOf("mainClass" to application.mainClass)) }
  }
}

afterEvaluate {
  // task to run all test and coverage report tasks for entire project
  val allTest = tasks.register<TestReport>("allTest") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    dependsOn(
      tasks.named("integrationTestAggregateTestReport"),
      tasks.testAggregateTestReport
    )
    finalizedBy(tasks.named("aggregateJacocoReport"))
    @Suppress("UnstableApiUsage")
    testResults.setFrom(
      subprojects.map { it.tasks.withType<Test>() }.flatten()
    )
    @Suppress("UnstableApiUsage")
    destinationDirectory.set(file("$buildDir/reports/tests/all"))
  }

  tasks.check { dependsOn(allTest) }
}
