pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "webapp-app"

val lib = rootDir.resolve("../../lib")
if (lib.exists() && lib.resolve("../settings.gradle.kts").exists()) {
    includeBuild(lib.resolve("../")) {
        dependencySubstitution {
            substitute(module("ru.raysmith:tg-bot")).using(project(":lib"))
        }
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        mavenLocal()
    }

    versionCatalogs {
        create("kotlinWrappers") {
            val wrappersVersion = "2025.4.12"
            from("org.jetbrains.kotlin-wrappers:kotlin-wrappers-catalog:$wrappersVersion")
        }
    }
}

include("application")
include("webapp")

