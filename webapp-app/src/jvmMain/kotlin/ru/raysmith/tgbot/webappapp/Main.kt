package ru.raysmith.tgbot.webappapp

import io.ktor.http.*
import io.ktor.network.tls.certificates.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
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
import ru.raysmith.tgbot.utils.verifyWebAppInitData
import java.io.File

private val localIp = "192.168.0.228"

val bot = Bot()
    .onError { Bot.logger.error(it) }

@OptIn(DelicateCoroutinesApi::class, ExperimentalSerializationApi::class)
fun main() {
    val keyStoreFile = File("build/keystore.jks")
    val keyStore = buildKeyStore {
        certificate("sampleAlias") {
            password = "foobar"
            domains = listOf("127.0.0.1", "0.0.0.0", "localhost", localIp)
        }
    }

    keyStore.saveToFile(keyStoreFile, "123456")

    GlobalScope.launch {
        val prettyPrintJson = Json(TelegramApi.json) {
            prettyPrint = true
            prettyPrintIndent = " "
        }

        bot.start {
            handleCommand {
                isCommand(BotCommand.START) {
                    send {
                        text = "site"
                        inlineKeyboard {
                            row {
                                button {
                                    text = "Open"
                                    webApp = WebAppInfo(System.getenv("HOST") ?: "https://$localIp:8443")
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

    embeddedServer(Netty,
        configure = {
            connector {
                port = 8080
            }
//            sslConnector(
//                keyStore = keyStore,
//                keyAlias = "sampleAlias",
//                keyStorePassword = { "123456".toCharArray() },
//                privateKeyPassword = { "foobar".toCharArray() }
//            ) {
//                port = 8443
//                keyStorePath = keyStoreFile
//            }
        }
    ) {
        install(CallLogging)
        install(CORS) {
            allowMethod(HttpMethod.Get)
            anyHost()
        }
        install(Compression) {
            gzip()
            deflate()
        }

        routing {
            staticResources("/", "", index = "index.html") {
                default("index.html")
            }

            post("/verify") {
                val initData = call.receiveText()
                if (bot.verifyWebAppInitData(initData)) {
                    call.respond(HttpStatusCode.Accepted)
                } else {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }
    }.start(wait = true)
}