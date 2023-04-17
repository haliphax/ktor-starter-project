package dev.haliphax

import jp.gr.java_conf.spica.plugin.gradle.jacoco.JacocoMarkdownTask
import org.gradle.language.base.plugins.LifecycleBasePlugin
import org.gradle.testing.jacoco.plugins.JacocoCoverageReport
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.tasks.JacocoReport

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

// aggregate coverage report for allTest testing suite
afterEvaluate {
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
tasks.register<JacocoMarkdownTask>("allTestCodeCoverageReportMarkdown") {
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
  previousJson.set(kotlin.io.path.createTempFile().toFile())
}
