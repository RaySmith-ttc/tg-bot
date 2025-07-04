plugins {
    kotlin("jvm") version libs.versions.kotlin
    `kotlin-dsl`
}

dependencies {
    gradleApi()
    implementation(libs.raysmith.htmldiff)
    implementation(libs.jsoup)
}
