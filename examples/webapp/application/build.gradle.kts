import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.kotlin.dsl.named

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.setupapp) // resources inheritance
    alias(libs.plugins.shadow)
    application
}

dependencies {
    implementation(projects.app)
}

application {
    mainClass = "ru.raysmith.tgbot.webappapp.application.MainKt"
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("webapp-showcase")
    }
}
