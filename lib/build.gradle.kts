import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.benManes.versions)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlin.multiplatform)
    `maven-publish`
}

group = "ru.raysmith"
version = "1.0.0-beta.9"

//publishing {
//    repositories {
//        maven {
//            name = "GitHubPackages"
//            url = uri("https://maven.pkg.github.com/raysmith-ttc/tg-bot")
//            credentials {
//                username = System.getenv("GIT_USERNAME")
//                password = System.getenv("GIT_TOKEN_PUBLISH")
//            }
//        }
//    }
//    publications {
//        register<MavenPublication>("gpr") {
//            from(components["java"])
//        }
//    }
//}

//java {
//    withSourcesJar()
//    withJavadocJar()
//}


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
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    js(IR) {
        useCommonJs()
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
        binaries.executable()
    }

    sourceSets {
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
                implementation(libs.kotlin.node)
//                implementation(libs.kotlin.react)
                implementation(libs.seskar.core)

                implementation(npm("crypto-js", "^4.2.0"))
            }
        }
    }
}

//tasks {
//    withType<Test> {
//        useJUnitPlatform()
//    }
//
//    withType<DependencyUpdatesTask> {
//        gradleReleaseChannel = "current"
//        rejectVersionIf {
//            val version = candidate.version
//            val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
//            val regex = "^[0-9,.v-]+(-r)?$".toRegex()
//            val isStable = stableKeyword || regex.matches(version)
//            isStable.not()
//        }
//    }
//}