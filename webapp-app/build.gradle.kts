import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.benManes.versions)
    alias(libs.plugins.seskar)
    application
}

application {
    mainClass = "ru.raysmith.tgbot.webappapp.MainKt"
}

kotlin {
    jvm {
        withJava()
    }

    js(IR) {
        compilerOptions {
            target = "es2015"
            freeCompilerArgs.apply {
                add("-Xsuppress-warning=UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
            }
        }
        useCommonJs()
        browser {
            nodejs()
            webpackTask {
                mainOutputFileName.set("webappapp.js")
                args += listOf(
                    "--env", "TG_BOT_TOKEN=${System.getenv("TG_BOT_TOKEN")}",
                    "--env", "WEBAPP_GUARD_ENABLED=${System.getenv("WEBAPP_GUARD_ENABLED")?.toBoolean() ?: true}",
                )
            }
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
                export = false
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
            dependencies {
                implementation(projects.lib)

                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.bundles.ktor.server)
                implementation(libs.ktor.network.tlsCertificates)

                implementation(libs.log4j.core)
                implementation(libs.slf4j.api)
                implementation(libs.log4j.slf4j2.impl)
            }
        }

        jsMain {
            dependencies {
                implementation(projects.lib)
                implementation(libs.kotlinx.coroutines.core)

                implementation(kotlinWrappers.react)
                implementation(kotlinWrappers.reactDom)
                implementation(kotlinWrappers.reactRouter)
                implementation(kotlinWrappers.node)
                implementation(kotlinWrappers.mui.material)
                implementation(kotlinWrappers.mui.iconsMaterial)
                implementation(kotlinWrappers.emotion)

                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.js)

                implementation(npm("webpack-bundle-analyzer", "^4.10.2"))
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


//    getByName<JavaExec>("run") {
//        classpath(getByName<Jar>("jvmJar"))
//    }
//    println("jvmRun: ${findByName("jvmRun")}")
//
//    withType<org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrLink> {
//        compilerOptions.moduleKind.set(org.jetbrains.kotlin.gradle.dsl.JsModuleKind.MODULE_COMMONJS)
//    }

}