package dev.haliphax.ktorStarterProject.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface HasLog {
  val log: Logger get() = LoggerFactory.getLogger(this.javaClass)
}
