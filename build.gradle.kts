import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.benManes.versions)
}

tasks {
    named<DependencyUpdatesTask>("dependencyUpdates").configure {
        val regex = "^[0-9,.v-]+(-rc)?$".toRegex()
        val stableList = listOf("RELEASE", "FINAL", "GA")

        rejectVersionIf {
            val stableKeyword = stableList.any { candidate.version.uppercase().contains(it) }
            val isStable = stableKeyword || regex.matches(candidate.version)
            isStable.not()
        }
    }
}