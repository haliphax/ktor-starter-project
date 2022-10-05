package dev.haliphax.ktorStarterProject.services.demo.controllers

import org.koin.core.annotation.Single

@Single
class DemoController {
  fun test(): String = "Testing"
}
