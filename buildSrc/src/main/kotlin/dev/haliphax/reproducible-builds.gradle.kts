package dev.haliphax

// enable reproducible builds
tasks.withType<AbstractArchiveTask> {
  isPreserveFileTimestamps = false
  isReproducibleFileOrder = true
}

// lock dependency versions for all configurations
dependencyLocking { lockAllConfigurations() }

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
