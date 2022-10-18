package dev.haliphax.common.testing

import io.kotest.core.config.AbstractProjectConfig

class KotestConfiguration : AbstractProjectConfig() {
  override var displayFullTestPath: Boolean? = true
  override val parallelism: Int = Runtime.getRuntime().availableProcessors()
}
