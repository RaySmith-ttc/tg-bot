import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.benManes.versions)
    alias(libs.plugins.dokka)
    kotlin("jvm") version "2.0.0"
    `maven-publish`
}

group = "ru.raysmith"
version = "1.0.0-beta.8"

repositories {
    mavenCentral()
    mavenLocal()
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/raysmith-ttc/utils")
        credentials {
            username = System.getenv("GIT_USERNAME")
            password = System.getenv("GIT_TOKEN_READ")
        }
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/raysmith-ttc/tg-bot")
            credentials {
                username = System.getenv("GIT_USERNAME")
                password = System.getenv("GIT_TOKEN_PUBLISH")
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}

java {
    withSourcesJar()
    withJavadocJar()
}

dependencies {
    // Kotlin
    implementation(libs.kotlinx.coroutines.core)

    // Logging
    implementation(libs.log4j)

    // Network
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.ktor.client)

    // Utils
    implementation(libs.raysmith.utils)

    // Testing
    testImplementation(kotlin("test"))
    testImplementation(libs.assertj.core)
    testImplementation(libs.konsist)
    testImplementation(libs.log4j.slf4j2.impl)

    // Webapp
    testImplementation(libs.ktor.server.core.jvm)
    testImplementation(libs.ktor.server.netty)
    testImplementation(libs.ktor.network.tlsCertificates)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-opt-in=kotlin.RequiresOptIn",
            "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
            "-Xcontext-receivers",
            "-XXLanguage:+UnitConversionsOnArbitraryExpressions",
        )
    }
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }

    withType<DependencyUpdatesTask> {
        gradleReleaseChannel = "current"
        rejectVersionIf {
            val version = candidate.version
            val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
            val regex = "^[0-9,.v-]+(-r)?$".toRegex()
            val isStable = stableKeyword || regex.matches(version)
            isStable.not()
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