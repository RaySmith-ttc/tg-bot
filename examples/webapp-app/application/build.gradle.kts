plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.setupapp) // resources inheritance
    application
}

dependencies {
    implementation(projects.webapp)
}

application {
    mainClass = "ru.raysmith.tgbot.webappapp.MainKt"
}
