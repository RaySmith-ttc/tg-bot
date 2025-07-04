import ru.raysmith.tgbot.gradle.HtmlDiffTask

plugins {
    signing
    `maven-publish`
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.seskar)
}

group = "ru.raysmith"
version = "1.0.0-beta.18"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

kotlin {
    jvm("jvm") {
        withSourcesJar()
    }

    js(IR) { // TODO need seskar update
        compilerOptions {
            target = "es2015"
            freeCompilerArgs.addAll (
//                "-Xsuppress-warning=UNCHECKED_CAST_TO_EXTERNAL_INTERFACE",
            )
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
            }
        }
    }
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }

    register<HtmlDiffTask>("apiDiff") {
        group = "help"
        description = "Creates html diff pages of telegram api for last saved revision and current state"
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
