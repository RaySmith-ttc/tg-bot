pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://redirector.kotlinlang.org/maven/bootstrap")
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
        maven("https://redirector.kotlinlang.org/maven/bootstrap")
    }
}