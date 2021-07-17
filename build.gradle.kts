plugins {
    id("org.jetbrains.kotlin.plugin.serialization") version "1.4.10"
    kotlin("jvm") version "1.5.10"
}

group = "ru.raysmith"
version = "0.0.1"

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

java {
    withSourcesJar()
    withJavadocJar()
}

dependencies {
    // Kotlin
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.2.2")
    implementation("junit:junit:4.13.1")
    implementation("org.junit.jupiter:junit-jupiter:5.7.0")

    // Logging
    val slf4jVersion = "1.7.26"
    implementation("log4j:log4j:1.2.17")
    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    implementation("org.slf4j:slf4j-log4j12:$slf4jVersion")

    // Network

    val kotlinxSerializationVersion = "1.0.1"
    implementation("com.squareup.retrofit2:retrofit:2.7.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.7.2")
    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinxSerializationVersion")

    // Utils
    implementation("ru.raysmith:utils:1.0.0-rc.5")

    // Testing
    testImplementation("org.assertj:assertj-core:3.19.0")
    testImplementation("org.mockito:mockito-core:3.+")
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
