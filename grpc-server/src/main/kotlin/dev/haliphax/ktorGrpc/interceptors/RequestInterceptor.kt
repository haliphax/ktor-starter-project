package dev.haliphax.ktorGrpc.interceptors

import dev.haliphax.common.logging.HasLog
import io.grpc.Metadata
import io.grpc.ServerCall
import io.grpc.ServerCallHandler
import io.grpc.ServerInterceptor

object RequestInterceptor : ServerInterceptor, HasLog {
  override fun <ReqT : Any, RespT : Any> interceptCall(
    call: ServerCall<ReqT, RespT>,
    headers: Metadata,
    next: ServerCallHandler<ReqT, RespT>
  ): ServerCall.Listener<ReqT> {
    log.info(call.methodDescriptor.fullMethodName)
    return next.startCall(call, headers)
  }
}
