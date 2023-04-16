import jp.gr.java_conf.spica.plugin.gradle.jacoco.JacocoMarkdownTask
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import kotlin.io.path.createTempFile

// common coverage report exclusions
val jacocoExcludes = setOf(
  "**/*$*$*.class",
  "**/dev/haliphax/**/Dependencies*.class",
  "**/dev/haliphax/**/MainKt.class",
  "**/dev/haliphax/**/aliases/**",
  "**/dev/haliphax/common/testing/KotestConfiguration.class",
  "**/generated/**",
  "**/proto/**",
)

plugins {
  `jacoco-report-aggregation`
  `test-report-aggregation`
  id("com.github.sakata1222.jacoco-markdown")
  id("com.google.devtools.ksp")
  id("org.jetbrains.kotlin.jvm")
  id("org.jetbrains.kotlin.plugin.serialization") apply false

  // load for use in subprojects but do not apply these to the root project
  id("com.github.johnrengelman.shadow") apply false
  id("com.google.protobuf") apply false
}

// aggregate reports from all subprojects
dependencies {
  subprojects.forEach {
    jacocoAggregation(it)
    testReportAggregation(it)
  }
}

allprojects {
  group = "dev.haliphax"
  version = "unspecified"

  apply(plugin = "com.github.sakata1222.jacoco-markdown")
  apply(plugin = "jacoco")
  apply(plugin = "jacoco-report-aggregation")
  apply(plugin = "org.jetbrains.kotlin.jvm")
  apply(plugin = "test-report-aggregation")

  repositories { mavenCentral() }

  // version targeting
  java {
    sourceCompatibility = JavaVersion.VERSION_19
    targetCompatibility = JavaVersion.VERSION_19
    toolchain { languageVersion.set(JavaLanguageVersion.of(19)) }
  }

  tasks.withType<KotlinCompile> { kotlinOptions.jvmTarget = "19" }

  // lock dependency versions for all configurations
  dependencyLocking { lockAllConfigurations() }

  @Suppress("UnstableApiUsage")
  testing.suites {
    // integrationTest test suite
    create<JvmTestSuite>("integrationTest") {
      testType.set(TestSuiteType.INTEGRATION_TEST)
      dependencies { implementation(project()) }
    }

    // aggregate test suite
    create<JvmTestSuite>("allTest") {
      val suites = testing.suites.withType<JvmTestSuite>().filter {
        it != this
      }

      testType.set("all")

      // include class paths from main source set
      sources.compileClasspath += sourceSets.main.get().output
      sources.runtimeClasspath += sourceSets.main.get().output

      // include class paths from all other testing suites
      suites.map {
        val set = sourceSets.named(it.name).get()
        sources.compileClasspath += set.compileClasspath
        sources.runtimeClasspath += set.runtimeClasspath
      }

      dependencies {
        implementation(project())

        // include dependencies from all other testing suites
        suites.forEach { suite ->
          project.configurations.filter { config ->
            config.name.startsWith(suite.name)
          }.forEach { config ->
            config.dependencies.forEach { dependency ->
              implementation(dependency)
            }
          }
        }
      }

      targets.all {
        testTask {
          testClassesDirs = files(
            suites.map { "$buildDir/classes/kotlin/${it.name}" },
          )
          extensions.configure<JacocoTaskExtension> {
            destinationFile = file("$buildDir/jacoco/allTest.exec")
          }
        }
      }
    }

    // JvmTestSuite configuration
    withType<JvmTestSuite> {
      useJUnitJupiter()

      targets.all {
        testTask {
          testLogging {
            showCauses = true
            showStackTraces = true
            exceptionFormat = TestExceptionFormat.SHORT
            events(
              TestLogEvent.PASSED,
              TestLogEvent.FAILED,
            )
          }
        }
      }
    }
  }

  reporting.reports {
    // coverage report configuration
    @Suppress("UnstableApiUsage")
    withType<JacocoCoverageReport> {
      reportTask {
        reports {
          html.required.set(true)
          xml.required.set(true)
        }

        classDirectories.setFrom(
          classDirectories.asFileTree.matching { exclude(jacocoExcludes) }.files,
        )
      }
    }
  }

  // aggregate coverage report for allTest testing suite
  tasks.withType<JacocoReport>().named("allTestCodeCoverageReport") {
    val allSource = subprojects.map {
      it.sourceSets.main.get().allSource.sourceDirectories
    }
    dependsOn(tasks.named("allTestAggregateTestReport"))
    additionalSourceDirs.setFrom(allSource)
    classDirectories.setFrom(
      subprojects.map {
        fileTree("${it.buildDir}/classes").exclude(jacocoExcludes)
      },
    )
    executionData.setFrom(
      subprojects.map {
        fileTree(it.buildDir).include("/jacoco/allTest.exec")
      },
    )
    sourceDirectories.setFrom(allSource)
  }

  // aggregate coverage verification for allTest testing suite
  tasks.register<JacocoCoverageVerification>("allTestCoverageVerification") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP

    val allSource = subprojects.map {
      it.sourceSets.main.get().allSource.sourceDirectories
    }
    dependsOn(subprojects.map { it.tasks.named("allTest") })
    additionalSourceDirs.setFrom(allSource)
    classDirectories.setFrom(
      subprojects.map {
        fileTree("${it.buildDir}/classes").exclude(jacocoExcludes)
      },
    )
    executionData.setFrom(
      subprojects.map {
        fileTree(it.buildDir).include("/jacoco/allTest.exec")
      },
    )
    sourceDirectories.setFrom(allSource)

    violationRules.rule {
      limit { minimum = "0.7".toBigDecimal() }
    }
  }

  // markdown coverage report output task for allTest testing suite
  tasks.create<JacocoMarkdownTask>("allTestCodeCoverageReportMarkdown") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP

    dependsOn(tasks.withType<JacocoReport>().named("allTestCodeCoverageReport"))

    diffEnabled.set(false)
    jacocoXml.set(
      file("$buildDir/reports/jacoco/allTestCodeCoverageReport/allTestCodeCoverageReport.xml"),
    )
    outputJson.set(
      file("$buildDir/reports/jacoco/allTestCodeCoverageReport/allTestCodeCoverageReport.json"),
    )
    outputMd.set(
      file("$buildDir/reports/jacoco/allTestCodeCoverageReport/allTestCodeCoverageReport.md"),
    )
    // need a file specified here even though coverage diff is disabled
    previousJson.set(createTempFile().toFile())
  }

  tasks.check {
    dependsOn(
      tasks.named("allTest"),
      tasks.named("allTestCoverageVerification"),
    )
  }

  // task to lock all resolvable dependencies
  tasks.register("resolveAndLockAll") {
    notCompatibleWithConfigurationCache(
      "Filters configurations at execution time",
    )
    doFirst {
      require(gradle.startParameter.isWriteDependencyLocks)
    }
    doLast {
      configurations.filter {
        it.isCanBeResolved
      }.forEach { it.resolve() }
    }
  }
}

// subprojects config
subprojects {
  apply(plugin = "com.google.devtools.ksp")
  apply(plugin = "idea")
  apply(plugin = "org.jetbrains.kotlin.jvm")
  apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

  dependencies {
    // generate koin KSP sources
    val koinKspVersion: String by project
    ksp("io.insert-koin", "koin-ksp-compiler", koinKspVersion)
  }

  sourceSets {
    main {
      // include generated KSP sources
      java.srcDirs("build/generated/ksp/main/kotlin")
    }

    named("integrationTest") {
      // include compiled classes from main source set in integrationTest
      compileClasspath += sourceSets.main.get().compileClasspath
      // include generated KSP sources
      java.srcDirs("build/generated/ksp/integrationTest/kotlin")
    }
  }

  // enable reproducible builds
  tasks.withType<AbstractArchiveTask> {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
  }

  // skip non-application projects
  if (listOf("common", "proto").contains(name)) return@subprojects

  apply(plugin = "application")
  apply(plugin = "com.github.johnrengelman.shadow")
}

// do not build JAR file for root project
tasks.jar { isEnabled = false }
