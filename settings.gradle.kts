@file:Suppress("UnstableApiUsage")

rootProject.name = "tg-bot"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven { setUrl("https://plugins.gradle.org/m2/") }
    }
}


fun Settings.include(project: String, setup: ProjectDescriptor.() -> Unit) {
    this.include(project).also {
        project(":${project.dropWhile { it == ':' }}").apply(setup)
    }
}

include("lib")
include("webapp-app")
include("webapp-app-application") {
    projectDir = file("webapp-app/application")
}

rootProject.children.forEach {
    it.buildFileName = it.name + ".gradle.kts"
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
dependencyResolutionManagement {
    repositories {
        mavenCentral()
        mavenRaySmith("utils")
    }

    versionCatalogs {
        create("kotlinWrappers") {
            val wrappersVersion = "2025.4.12"
            from("org.jetbrains.kotlin-wrappers:kotlin-wrappers-catalog:$wrappersVersion")
        }
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