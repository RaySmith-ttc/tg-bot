import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlinx.serialization)
    kotlin("jvm") version "2.0.0-Beta2"
    id("maven-publish")
}

group = "ru.raysmith"
version = "1.0.0-beta.3"

repositories {
    mavenCentral()
    mavenLocal()
    jcenter()
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
//    implementation(libs.log4j.core)
    implementation("org.apache.logging.log4j:log4j:2.19.0")
    implementation("io.ktor:ktor-client-logging-jvm:2.3.4")
    implementation("io.ktor:ktor-client-okhttp-jvm:2.3.4")

    // Network
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.ktor.client.jvm)
    implementation(libs.ktor.client.okhttp)

    // Utils
    implementation(libs.raysmith.utils)

    // Extensions for
    api(libs.exposed.core)

    // Testing
    testImplementation("org.assertj:assertj-core:3.23.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
    testImplementation("org.mockito:mockito-core:4.8.0")
    testImplementation(libs.konsist)
    testImplementation(libs.ktor.server.core.jvm)
    testImplementation(libs.ktor.server.netty)
    testImplementation(libs.ktor.network.tlsCertificates)
    testImplementation(libs.log4j.slf4j18.impl)
//    testImplementation(libs.slf4j.api)
//    testImplementation(libs.slf4j.reload4j)
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
            freeCompilerArgs += "-opt-in=kotlinx.serialization.ExperimentalSerializationApi"
            freeCompilerArgs += "-Xcontext-receivers"
            freeCompilerArgs += "-XXLanguage:+UnitConversionsOnArbitraryExpressions"
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }
    }
    withType<Test> {
        useJUnitPlatform()
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