import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlinx.jsPlainObjects)
    alias(libs.plugins.seskar)
    alias(libs.plugins.benManes.versions)
}

kotlin {
    jvm {
        binaries {
            executable {
                mainClass.set("ru.raysmith.tgbot.webappapp.MainKt")
            }
        }
    }
    js {
        useEsModules()
        browser {
            val webpackMode = System.getProperty("WEBPACK_MODE")?.let {
                KotlinWebpackConfig.Mode.valueOf(it.uppercase())
            } ?: run {
                logger.warn("WEBPACK_MODE system property is not set, defaulting to DEVELOPMENT mode")
                KotlinWebpackConfig.Mode.DEVELOPMENT
            }

            webpackTask {
                mode = webpackMode
                mainOutputFileName = "webapp.js"
                args += listOf(
                    "--env", "WEBAPP_GUARD_ENABLED=${System.getenv("WEBAPP_GUARD_ENABLED")?.toBoolean() ?: true}",
                )

                println(configDirectory?.absolutePath)
            }
//            commonWebpackConfig {
//                mode = webpackMode
//                cssSupport {
//                    enabled.set(true)
//                }
//                export = false
////                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
////                    static = (static ?: mutableListOf()).apply {
////                        add(project.rootDir.path)
////                        add(project.projectDir.path)
////                    }
////                }
//            }
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
                implementation(kotlin("stdlib-js"))

                implementation(kotlinWrappers.js)
                implementation(kotlinWrappers.react)
                implementation(kotlinWrappers.reactDom)
                implementation(kotlinWrappers.reactUse)
                implementation(kotlinWrappers.reactCore)
                implementation(kotlinWrappers.tanstack.reactRouter)
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
        val webpackTaskName = if (isProd) "jsBrowserProductionWebpack" else "jsBrowserDevelopmentWebpack"
        val webpackTask = getByName<KotlinWebpack>(webpackTaskName)
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

fun isLibProjectPresent() = rootDir.resolve("../../settings.gradle.kts").let {
    it.exists() && it.readLines().contains("rootProject.name = \"tg-bot\"")
}
fun lib() = if (isLibProjectPresent()) libs.raysmith.tgBot.build else libs.raysmith.tgBot
