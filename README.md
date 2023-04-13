# ktor-starter-project

A very basic [ktor] project with some convenient configuration and structure:

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
  - Runs [ktlint] check against source
  - Executes unit and integration tests
  - Delivers coverage reports to [GitHub Pages]


[ktor]: https://ktor.io
[gRPC]: https://grpc.io
[Koin]: https://insert-koin.io
[pluginManagement]: https://docs.gradle.org/current/userguide/plugins.html#sec:plugin_management
[Version catalog]: https://docs.gradle.org/current/userguide/platforms.html#sub:version-catalog
[MockK]: https://mock.io
[kotest]: https://kotest.io
[JaCoCo]: https://www.jacoco.org/jacoco
[Test report aggregation]: https://docs.gradle.org/current/userguide/test_report_aggregation_plugin.html
[GitHub Actions workflow]: https://docs.github.com/en/actions/using-workflows/about-workflows
[ktlint]: https://ktlint.github.io
[GitHub Pages]: https://pages.github.com

