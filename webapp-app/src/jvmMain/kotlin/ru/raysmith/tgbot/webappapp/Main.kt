package ru.raysmith.tgbot.webappapp

import io.ktor.http.*
import io.ktor.network.tls.certificates.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.routing.*
import io.ktor.util.logging.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.handler.base.isCommand
import ru.raysmith.tgbot.model.bot.BotCommand
import ru.raysmith.tgbot.model.network.menubutton.WebAppInfo
import ru.raysmith.tgbot.network.TelegramApi
import java.io.File

@OptIn(DelicateCoroutinesApi::class, ExperimentalSerializationApi::class)
fun main() {

    val keyStoreFile = File("build/keystore.jks")
    val keyStore = buildKeyStore {
        certificate("sampleAlias") {
            password = "foobar"
            domains = listOf("127.0.0.1", "0.0.0.0", "localhost", "192.168.1.194")
        }
    }
    keyStore.saveToFile(keyStoreFile, "123456")

    GlobalScope.launch {
        val prettyPrintJson = Json(TelegramApi.json) {
            prettyPrint = true
            prettyPrintIndent = " "
        }

        Bot()
            .onError { Bot.logger.error(it) }
            .start {
            handleCommand {
                isCommand(BotCommand.START) {
                    send {
                        text = "site"
                        inlineKeyboard {
                            row {
                                button {
                                    text = "Open"
                                    webApp = WebAppInfo("https://192.168.1.194:8443")
                                }
                            }
                        }
                    }
                }
            }

            handleMessage {
                if (message.webAppData != null) {
                    println(prettyPrintJson.encodeToString(message.webAppData))
                } else {
                    println("MESSAGE: $message")
                }
            }
        }
    }

    embeddedServer(Netty, applicationEngineEnvironment {

        connector {
            port = 8080
        }
        sslConnector(
            keyStore = keyStore,
            keyAlias = "sampleAlias",
            keyStorePassword = { "123456".toCharArray() },
            privateKeyPassword = { "foobar".toCharArray() }) {
            port = 8443
            keyStorePath = keyStoreFile
        }

        module {
            install(CallLogging)
            install(CORS) {
                allowMethod(HttpMethod.Get)
                anyHost()
            }

            routing {
                staticResources("/", "", index = "index.html") {
                    default("index.html")
                }
//                staticFiles("webappapp.js", File("C:\\dev\\tg-bot\\webapp-app\\build\\kotlin-webpack\\js\\developmentExecutable\\webappapp.js"))
            }
        }
    })
        .start(wait = true)
}