import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.ofSourceSet
import com.google.protobuf.gradle.plugins
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

val grpcVersion: String by project
val grpcKotlinVersion: String by project
val protobufVersion: String by project

plugins {
  id("com.google.protobuf")
}

dependencies {
  implementation(common.bundles.all)
  implementation(proto.bundles.all)
  implementation(server.bundles.grpc)

  // testImplementation(client.bundles.all)
  testImplementation(coroutines.bundles.core)
  testImplementation(test.bundles.all)
}

application {
  mainClass.set("dev.haliphax.ktorStarterProject.MainKt")
}

protobuf {
  protoc {
    artifact = "com.google.protobuf:protoc:$protobufVersion"
  }

  plugins {
    id("grpc") {
      artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
    }
    id("grpcKt") {
      artifact = "io.grpc:protoc-gen-grpc-kotlin:$grpcKotlinVersion:jdk7@jar"
    }
  }

  generateProtoTasks {
    ofSourceSet("main").forEach {
      it.plugins {
        id("grpc")
        id("grpcKt")
      }
    }
  }
}

tasks.jar {
  manifest {
    attributes(mapOf("Main-Class" to "dev.haliphax.ktorStarterProject.MainKt"))
  }
}
