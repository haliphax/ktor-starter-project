# Ktor reference project implementation

> A very basic [ktor][] project with some convenient configuration and structure

[![Documentation](https://img.shields.io/github/actions/workflow/status/haliphax/ktor-starter-project/docs.yml?branch=main&label=docs)](https://haliphax.github.io/ktor-starter-project/)
[![Build](https://img.shields.io/github/actions/workflow/status/haliphax/ktor-starter-project/build.yml?branch=main)](https://github.com/haliphax/ktor-starter-project/actions/workflows/build.yml)
[![Tests](https://haliphax.testspace.com/spaces/235991/badge?token=db4bdbe227a3e64b46470133161f59e543adbfaf)](https://haliphax.testspace.com/spaces/235991)
[![Coverage](https://img.shields.io/coverallsCoverage/github/haliphax/ktor-starter-project)](https://coveralls.io/github/haliphax/ktor-starter-project)

## Prerequisites

You will need a JDK that matches the version used by the project. (The current
version is `21`.) The project is primarily developed and tested against the
[Temurin][] JDK distribution from [Adoptium][].

## Features

- HTTP server with separate plugins for authentication, routing, and
  serialization
- Coroutine-based [gRPC][] server engine with demonstration service
- [Koin][] for annotation-driven dependency injection
- Shared Gradle plugins, project dependencies, and build configuration
  - [pluginManagement][] and scripted configuration for plugins
  - [Version catalog][] and scripted configuration for dependencies
- [MockK][] and [kotest][] libraries for coherent unit tests
- [JaCoCo][] test coverage plugin with reasonable configuration
- [Test report aggregation][] for collecting test reports for subprojects
- Unified `allTest` testing suite (contains all other testing suites), with
  associated coverage report and verification tasks
- [GitHub Actions workflow][] for pull requests and merges
  - Checks PR title for [gitmoji][] convention
  - Runs [ktlint][] and [prettier][] checks against files
  - Executes unit and integration tests
  - Test reports via [Testspace][]
  - Coverage reports via [Coveralls][]
    - Also added to PR as comment
    - Also added to workflow job summary
  - Builds project documentation with [dokka][]

[adoptium]: https://adoptium.net
[coveralls]: https://coveralls.io
[dokka]: https://kotl.in/dokka
[github actions workflow]: https://docs.github.com/en/actions/using-workflows/about-workflows
[gitmoji]: https://gitmoji.dev
[grpc]: https://grpc.io
[jacoco]: https://www.jacoco.org/jacoco
[koin]: https://insert-koin.io
[kotest]: https://kotest.io
[ktlint]: https://ktlint.github.io
[ktor]: https://ktor.io
[mockk]: https://mockk.io
[pluginmanagement]: https://docs.gradle.org/8.6/userguide/plugins.html#sec:plugin_management
[prettier]: https://prettier.io
[temurin]: https://adoptium.net/temurin/releases/?version=21
[test report aggregation]: https://docs.gradle.org/8.6/userguide/test_report_aggregation_plugin.html
[testspace]: https://testspace.com
[version catalog]: https://docs.gradle.org/8.6/userguide/platforms.html#sub:version-catalog
