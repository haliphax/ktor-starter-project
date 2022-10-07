package dev.haliphax.common.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface HasLog {
  val log: Logger get() = LoggerFactory.getLogger(this.javaClass)
}
