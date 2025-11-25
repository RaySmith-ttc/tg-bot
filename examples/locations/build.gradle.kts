plugins {
    kotlin("jvm") version libs.versions.kotlin
    application
    alias(libs.plugins.benManes.versions)
}

group = "ru.raysmith.tgbot.examples"
version = "1.0"

application {
    mainClass.set("ru.raysmith.tgbot.examples.locations.MainKt")
}

dependencies {

    // bundles
    implementation(libs.bundles.database)

    // libs
    implementation(if (isLibProjectPresent()) "ru.raysmith:tg-bot" else libs.raysmith.tgBot)
    implementation(libs.logback)
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
    sourceSets.all {
        languageSettings {
            enableLanguageFeature("ContextParameters")
        }
    }
}

// ------------------------------------------------------- Utils -------------------------------------------------------

fun isLibProjectPresent() = rootDir.resolve("../../lib/settings.gradle.kts").exists()
