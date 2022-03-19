import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.0"
    kotlin("jvm") version "1.6.0"
    id("maven-publish")
}

group = "ru.raysmith"
version = "0.0.1-alpha.84"

repositories {
    mavenCentral()
    jcenter()
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/raysmith-ttc/utils")
        credentials {
            username = System.getenv("git.username")
            password = System.getenv("git.read-token")
        }
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/raysmith-ttc/tg-bot")
            credentials {
                username = System.getenv("git.username")
                password = System.getenv("git.publish-token")
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
    @Suppress("UnstableApiUsage") withSourcesJar()
    @Suppress("UnstableApiUsage") withJavadocJar()
}

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0-native-mt")

    // Logging
    implementation("org.apache.logging.log4j:log4j:2.17.0")

    // Network
    val kotlinxSerializationVersion = "1.3.2"
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinxSerializationVersion")

    // Utils
    implementation("ru.raysmith:utils:1.2.2-rc.3")

    // Extensions for
    implementation("org.jetbrains.exposed:exposed-core:0.37.3")

    // Testing
    testImplementation("org.assertj:assertj-core:3.22.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testImplementation("org.mockito:mockito-core:4.3.1")

    val slf4jVersion = "1.7.36"
    testImplementation("org.slf4j:slf4j-api:$slf4jVersion")
    testImplementation("org.slf4j:slf4j-log4j12:$slf4jVersion")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
            freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlinx.serialization.ExperimentalSerializationApi"
            targetCompatibility = JavaVersion.VERSION_1_8.toString()
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }
    }
    withType<Test> {
        useJUnitPlatform()
    }
}