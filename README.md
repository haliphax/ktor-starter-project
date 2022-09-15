# ktor-starter-project

A very basic [ktor][] project with some convenient configuration and structure:

- HTTP server with separate plugins for authentication, routing, and
  serialization
- Shared Gradle plugins, project dependencies, and build configuration
  - [pluginManagement][] and scripted configuration for plugins
  - [Version catalog][] and scripted configuration for dependencies
- [MockK][] and [kotest][] libraries for coherent unit tests
- [JaCoCo][] test coverage plugin with reasonable configuration
- [GitHub Actions workflow][] for pull requests and merges
  - Runs [ktlint][] check against source
  - Executes unit tests
  - Delivers coverage reports to [GitHub Pages][]


[ktor]: https://ktor.io
[Version catalog]: https://docs.gradle.org/current/userguide/platforms.html#sub:version-catalog
[pluginManagement]: https://docs.gradle.org/current/userguide/plugins.html#sec:plugin_management
[MockK]: https://mock.io
[kotest]: https://kotest.io
[JaCoCo]: https://www.jacoco.org/jacoco
[GitHub Actions workflow]: https://docs.github.com/en/actions/using-workflows/about-workflows
[ktlint]: https://ktlint.github.io
[GitHub Pages]: https://pages.github.com

