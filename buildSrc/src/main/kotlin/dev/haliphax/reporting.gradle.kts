package dev.haliphax

import jp.gr.java_conf.spica.plugin.gradle.jacoco.JacocoMarkdownTask

// common coverage report exclusions
val jacocoExcludes =
  setOf(
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
  jacoco
  java
}

dependencies {
  subprojects.forEach {
    jacocoAggregation(it)
  }
}

reporting.reports {
  // coverage report configuration
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

val subprojectBuildDirs =
  subprojects.map {
    it.layout.buildDirectory.get().asFile
  }

// aggregate coverage report for allTest testing suite
afterEvaluate {
  tasks.withType<JacocoReport>().named("allTestCodeCoverageReport") {
    val allSource =
      subprojects.map {
        it.sourceSets.main.get().allSource.sourceDirectories
      }
    dependsOn(tasks.named("allTestAggregateTestReport"))
    additionalSourceDirs.setFrom(allSource)
    classDirectories.setFrom(
      subprojectBuildDirs.map {
        fileTree("$it/classes").exclude(jacocoExcludes)
      },
    )
    executionData.setFrom(
      subprojectBuildDirs.map {
        fileTree(it).include("/jacoco/allTest.exec")
      },
    )
    sourceDirectories.setFrom(allSource)
  }
}

// aggregate coverage verification for allTest testing suite
tasks.register<JacocoCoverageVerification>("allTestCoverageVerification") {
  group = LifecycleBasePlugin.VERIFICATION_GROUP

  val allSource =
    subprojects.map {
      it.sourceSets.main.get().allSource.sourceDirectories
    }
  dependsOn(subprojects.map { it.tasks.named("allTest") })
  additionalSourceDirs.setFrom(allSource)
  classDirectories.setFrom(
    subprojectBuildDirs.map {
      fileTree("$it/classes").exclude(jacocoExcludes)
    },
  )
  executionData.setFrom(
    subprojectBuildDirs.map {
      fileTree(it).include("/jacoco/allTest.exec")
    },
  )
  sourceDirectories.setFrom(allSource)

  violationRules.rule {
    limit { minimum = "0.7".toBigDecimal() }
  }
}

// markdown coverage report output task for allTest testing suite
tasks.register<JacocoMarkdownTask>("allTestCodeCoverageReportMarkdown") {
  group = LifecycleBasePlugin.VERIFICATION_GROUP
  val build = layout.buildDirectory.get().asFile

  dependsOn(tasks.withType<JacocoReport>().named("allTestCodeCoverageReport"))

  diffEnabled.set(false)
  jacocoXml.set(
    file("$build/reports/jacoco/allTestCodeCoverageReport/allTestCodeCoverageReport.xml"),
  )
  outputJson.set(
    file("$build/reports/jacoco/allTestCodeCoverageReport/allTestCodeCoverageReport.json"),
  )
  outputMd.set(
    file("$build/reports/jacoco/allTestCodeCoverageReport/allTestCodeCoverageReport.md"),
  )
  // need a file specified here even though coverage diff is disabled
  previousJson.set(kotlin.io.path.createTempFile().toFile())
}
