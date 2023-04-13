# Ktor starter project

A very basic [ktor] project with some convenient configuration and structure

## Prerequisites

You will need a JDK that matches the version used by the project. (The current
version is `19`.) The project is primarily developed and tested against the
[Temurin] JDK distribution from [Adoptium].

## Features

- HTTP server with separate plugins for authentication, routing, and
  serialization
- Coroutine-based [gRPC] server engine with demonstration service
- [Koin] for annotation-driven dependency injection
- Shared Gradle plugins, project dependencies, and build configuration
  - [pluginManagement] and scripted configuration for plugins
  - [Version catalog] and scripted configuration for dependencies
- [MockK] and [kotest] libraries for coherent unit tests
- [JaCoCo] test coverage plugin with reasonable configuration
- [Test report aggregation] for collecting test reports for subprojects
- Unified `allTest` testing suite (contains all other testing suites), with
  associated coverage report and verification tasks
- [GitHub Actions workflow] for pull requests and merges
  - Runs [ktlint] and [prettier] checks against files
  - Executes unit and integration tests
  - Stores test and coverage reports as [artifacts] for perusal

[adoptium]: https://adoptium.net
[artifacts]: https://docs.github.com/en/actions/using-workflows/storing-workflow-data-as-artifacts#about-workflow-artifacts
[github actions workflow]: https://docs.github.com/en/actions/using-workflows/about-workflows
[grpc]: https://grpc.io
[jacoco]: https://www.jacoco.org/jacoco
[koin]: https://insert-koin.io
[kotest]: https://kotest.io
[ktlint]: https://ktlint.github.io
[ktor]: https://ktor.io
[mockk]: https://mock.io
[pluginmanagement]: https://docs.gradle.org/current/userguide/plugins.html#sec:plugin_management
[prettier]: https://prettier.io
[temurin]: https://adoptium.net/temurin/releases/?version=19
[test report aggregation]: https://docs.gradle.org/current/userguide/test_report_aggregation_plugin.html
[version catalog]: https://docs.gradle.org/current/userguide/platforms.html#sub:version-catalog
