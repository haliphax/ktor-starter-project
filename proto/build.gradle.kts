import com.google.protobuf.gradle.id

val grpcVersion: String by project
val grpcKotlinVersion: String by project
val protobufVersion: String by project

plugins {
  id("com.google.protobuf")
  kotlin("jvm")
}

dependencies {
  implementation(proto.bundles.all)
}

// generate protobuffer sources
protobuf {
  protoc {
    artifact = "com.google.protobuf:protoc:$protobufVersion"
  }

  plugins {
    id("grpc") {
      artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
    }
    id("grpcKt") {
      artifact = "io.grpc:protoc-gen-grpc-kotlin:$grpcKotlinVersion:jdk8@jar"
    }
  }

  generateProtoTasks.all().forEach {
    it.plugins {
      id("grpc")
      id("grpcKt")
    }
  }
}

// declare explicit dependency to satisfy testing suites
tasks.filter {
  it.name.matches(Regex("^extractInclude[a-zA-Z]Proto$"))
}.forEach {
  it.apply { dependsOn(tasks.compileKotlin) }
}
