package dev.haliphax.ktorStarterProject.reflection

import io.ktor.server.application.Application

class LoadedMethod(
  val klass: Class<*>,
  val callable: String,
  val fullyQualifiedName: String
) {
  fun toModule(): Application.() -> Unit {
    return {
      val method = klass.methods.first { it.name == callable }
      method.invoke(this, this)
    }
  }
}

fun List<String>.toCallables(classLoader: ClassLoader): List<LoadedMethod> =
  this.map { fqn ->
    val dotIndex = fqn.lastIndexOf('.')
    val className = fqn.substring(0, dotIndex)
    val methodName = fqn.substring(dotIndex + 1)
    val klass = classLoader.loadClass(className)

    check(klass.methods.any { it.name == methodName }) {
      "Method not found: $fqn"
    }

    LoadedMethod(klass, methodName, fqn)
  }
