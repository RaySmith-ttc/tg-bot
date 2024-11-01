plugins {
    `kotlin-dsl`
}

dependencies {
    gradleApi()
    implementation(libs.raysmith.htmldiff)
    implementation(libs.jsoup)
}
