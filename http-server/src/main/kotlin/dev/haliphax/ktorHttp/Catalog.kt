package dev.haliphax.ktorHttp

import dev.haliphax.ktorGrpc.proto.DemoServiceGrpcKt.DemoServiceCoroutineStub

object Catalog {
  lateinit var demoService: DemoServiceCoroutineStub
}
