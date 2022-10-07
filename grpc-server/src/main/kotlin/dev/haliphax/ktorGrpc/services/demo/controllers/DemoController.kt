package dev.haliphax.ktorGrpc.services.demo.controllers

import org.koin.core.annotation.Single

@Single
class DemoController {
  fun test(input: String): String = "Hi, $input, I'm Dad"
}
