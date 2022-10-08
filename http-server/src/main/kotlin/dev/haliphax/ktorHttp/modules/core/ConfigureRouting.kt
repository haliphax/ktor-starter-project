package dev.haliphax.ktorHttp.modules.core

import dev.haliphax.ktorGrpc.proto.DemoRequest
import dev.haliphax.ktorGrpc.proto.DemoServiceGrpcKt.DemoServiceCoroutineStub
import dev.haliphax.ktorHttp.data.DemoData
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.koin.core.parameter.parametersOf
import org.koin.ktor.ext.getKoin

fun Application.configureRouting() {
  routing {
    val koin = application.getKoin()
    val demoService: DemoServiceCoroutineStub = koin.get {
      parametersOf(application.environment.config)
    }

    authenticate("auth-admin") {
      get("/admin") {
        call.respondText("Authorized")
      }
    }

    authenticate("auth-admin", "auth-basic") {
      get("/basic") {
        call.respondText("Authorized")
      }
    }

    get("/") {
      call.respondText("Hello, world!")
    }

    get("/data") {
      val obj = DemoData(message = "Testing")
      call.respond(obj)
    }

    post("/data") {
      val obj: DemoData = call.receive()
      call.respond(obj.copy(message = "Modified ${obj.message}"))
    }

    post("/grpc") {
      val obj: DemoData = call.receive()
      val grpcRequest =
        DemoRequest.newBuilder().setMessage(obj.message).build()
      val response = demoService.demo(grpcRequest)
      call.respond(response.message)
    }
  }
}
