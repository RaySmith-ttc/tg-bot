import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.seskar)
}

kotlin {
    jvm()
    js(IR) {
        compilerOptions {
            target = "es2015"
        }
        useCommonJs()
        browser {
            nodejs()
            webpackTask {
                mode = KotlinWebpackConfig.Mode.DEVELOPMENT
                mainOutputFileName.set("webappapp.js")
                args += listOf(
                    "--env", "WEBAPP_GUARD_ENABLED=${System.getenv("WEBAPP_GUARD_ENABLED")?.toBoolean() ?: true}",
                )
            }
            commonWebpackConfig {
                mode = KotlinWebpackConfig.Mode.DEVELOPMENT
                cssSupport {
                    enabled.set(true)
                }
                export = false
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        add(project.rootDir.path)
                        add(project.projectDir.path)
                    }
                }
            }
        }
        binaries.executable()
    }


    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.raysmith.utils)
            }
        }

        jvmMain {
            languageSettings {
                optIn("kotlinx.serialization.ExperimentalSerializationApi")
            }
            dependencies {

                // bundles
                implementation(libs.bundles.ktor.server)

                // libs
                implementation(lib())
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.logback)
            }
        }

        jsMain {
            dependencies {
                implementation(lib())

                implementation(kotlinWrappers.react)
                implementation(kotlinWrappers.reactDom)
                implementation(kotlinWrappers.reactRouter)
                implementation(kotlinWrappers.node)
                implementation(kotlinWrappers.mui.material)
                implementation(kotlinWrappers.mui.iconsMaterial)
                implementation(kotlinWrappers.emotion.styled)
                implementation(kotlinWrappers.emotion.react)
                implementation(npm("webpack-bundle-analyzer", "^4.10.2"))

                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.ktor.client.js)

            }
        }
    }
}

tasks {
    getByName<ProcessResources>("jvmProcessResources") {
        val isProd = System.getenv("env") == "prod"
        val webpackTask = getByName<KotlinWebpack>(if (isProd) "jsBrowserProductionWebpack" else "jsBrowserDevelopmentWebpack")
        dependsOn(webpackTask)
        from(File(webpackTask.outputDirectory.asFile.get(), webpackTask.mainOutputFileName.get()))
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    withType<KotlinJsCompile>().configureEach {
        compilerOptions {
            target.set("es2015")
        }
    }
}

// ------------------------------------------------------- Utils -------------------------------------------------------

fun isLibProjectPresent() = rootDir.resolve("../../lib/settings.gradle.kts").exists()
fun lib() = if (isLibProjectPresent()) libs.raysmith.tgBot.build else libs.raysmith.tgBot
