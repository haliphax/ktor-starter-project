package dev.haliphax.ktorHttp.helpers

import dev.haliphax.common.testing.koinTestApplication
import dev.haliphax.ktorHttp.Dependencies
import dev.haliphax.ktorHttp.TestDependencies
import io.ktor.server.testing.ApplicationTestBuilder
import org.koin.ksp.generated.module

fun koinTest(block: suspend ApplicationTestBuilder.() -> Unit) =
  koinTestApplication(
    listOf(Dependencies().module, TestDependencies().module),
    block
  )
