@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.gradle.kotlin.dsl.support.kotlinCompilerOptions
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JsModuleKind
import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    java
    signing
    `maven-publish`
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.benManes.versions)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.seskar)
}

group = "ru.raysmith"
version = "1.0.0-beta.11"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
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

    jvm {
        withJava()
        withSourcesJar()
    }

    js(IR) {
        compilerOptions {
//            moduleKind = JsModuleKind.MODULE_UMD
            target = "es2015"
        }

        browser {
            webpackTask {
                mainOutputFileName.set("tg-bot.js")
            }
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
                export = false
            }
        }
        nodejs()
        binaries.executable()
    }

    sourceSets {
        commonMain {

        }

        jvmMain {
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
            }
        }

        jvmTest {
            dependencies {
                // Testing
                implementation(kotlin("test"))
                implementation(libs.assertj.core)
                implementation(libs.konsist)
                implementation(libs.log4j.slf4j2.impl)

                // Webapp
                implementation(libs.ktor.server.core.jvm)
                implementation(libs.ktor.server.netty)
                implementation(libs.ktor.network.tlsCertificates)
            }
        }

        jsMain {

            dependencies {
                implementation(kotlinWrappers.node)
                implementation(kotlinWrappers.react)
                implementation(libs.seskar.core)

                implementation(npm("crypto-js", "^4.2.0"))
            }
        }
    }
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }

    named<DependencyUpdatesTask>("dependencyUpdates").configure {
        val regex = "^[0-9,.v-]+(-r)?$".toRegex()
        val stableList = listOf("RELEASE", "FINAL", "GA")

        rejectVersionIf {
            val stableKeyword = stableList.any { candidate.version.uppercase().contains(it) }
            val isStable = stableKeyword || regex.matches(candidate.version)
            isStable.not()
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
            from(components["kotlin"])
        }

        withType<MavenPublication> {
            artifactId = artifactId.replace("^${project.name}".toRegex(), "tg-bot")
        }
    }
}
