@file:Suppress("UnstableApiUsage")

rootProject.name = "tg-bot"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven { setUrl("https://plugins.gradle.org/m2/") }
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
dependencyResolutionManagement {
    repositories {
        mavenCentral()
        mavenRaySmith("utils")
    }

    versionCatalogs {

    }
}

fun RepositoryHandler.mavenRaySmith(name: String) {
    maven {
        url = uri("https://maven.pkg.github.com/raysmith-ttc/$name")
        credentials {
            username = System.getenv("GIT_USERNAME")
            password = System.getenv("GIT_TOKEN_READ")
        }
    }
}