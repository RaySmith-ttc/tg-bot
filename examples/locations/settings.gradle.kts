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
rootProject.name = "locations"

val lib = rootDir.resolve("../../lib")
if (lib.exists() && lib.resolve("../settings.gradle.kts").exists()) {
    includeBuild(lib.resolve("../")) {
        dependencySubstitution {
            substitute(module("ru.raysmith:tg-bot")).using(project(":lib"))
        }
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        mavenLocal()
    }

    versionCatalogs {
    }
}

