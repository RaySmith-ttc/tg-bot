import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.benManes.versions)
}

subprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        compilerOptions {
            freeCompilerArgs.addAll(
                "-opt-in=kotlin.RequiresOptIn",
                "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
                "-Xcontext-parameters",
                "-Xcontext-sensitive-resolution",
                "-XXLanguage:+UnitConversionsOnArbitraryExpressions", // TODO is it required? provide comment
            )
        }
    }
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