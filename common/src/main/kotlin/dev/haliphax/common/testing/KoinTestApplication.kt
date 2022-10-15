package dev.haliphax.common.testing

import io.ktor.server.testing.ApplicationTestBuilder
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module

fun koinTestApplication(
  modules: List<Module>,
  block: suspend ApplicationTestBuilder.() -> Unit
) {
  ApplicationTestBuilder().apply {
    startKoin {
      printLogger()
      modules(modules)
    }

    runBlocking { block(this@apply) }
    stopKoin()
  }
}

fun koinTestApplication(
  vararg modules: Module,
  block: suspend ApplicationTestBuilder.() -> Unit
) =
  koinTestApplication(modules.asList(), block)
