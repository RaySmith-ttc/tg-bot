plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.setupapp)
    application
}

dependencies {
    implementation(projects.webappApp)
}

setupapp {

}

application {
    mainClass = "ru.raysmith.tgbot.webappapp.MainKt"
}
