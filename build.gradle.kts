import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlinx.serialization)
    kotlin("jvm") version "2.0.0-Beta1"
    id("maven-publish")
}

group = "ru.raysmith"
version = "1.0.0-beta.1"

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

    // Network
    val kotlinxSerializationVersion = "1.5.1"
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.ktor.client.jvm)

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