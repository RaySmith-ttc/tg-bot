package ru.raysmith.tgbot.webappapp

import io.ktor.http.*
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
import io.ktor.server.util.*
import io.ktor.util.logging.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.handler.base.isCommand
import ru.raysmith.tgbot.model.Currency
import ru.raysmith.tgbot.model.bot.BotCommand
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.api.inline.content.InputTextMessageContent
import ru.raysmith.tgbot.model.api.inline.result.InlineQueryResultArticle
import ru.raysmith.tgbot.model.api.menubutton.WebAppInfo
import ru.raysmith.tgbot.model.api.payment.LabeledPrice
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.utils.*
import java.io.File
import java.net.InetAddress

private val localIp by lazy {
    InetAddress.getLocalHost().hostAddress
}

private val portHttp = System.getenv("PORT")?.ifEmpty { null }?.toInt() ?: 8080
private val portHttps = System.getenv("PORT_HTTPS")?.ifEmpty { null }?.toInt() ?: 8081
private val host = System.getenv("HOST")?.ifEmpty { null } ?: "http://$localIp:$portHttp"
private val useTestServer = System.getenv("TEST_SERVER") != "false"
private val paymentProviderToken = System.getenv("PAYMENT_PROVIDER_TOKEN")

val webappBot = Bot(useTestServer = useTestServer)
    .onError { Bot.logger.error(it) }

val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

var preparedMessageId: String? = null

fun main() {
    startApp()
}

fun startApp() {
    scope.launch {
        val prettyPrintJson = Json(TelegramApi.json) {
            prettyPrint = true
            prettyPrintIndent = " "
        }

        webappBot.start {
            handleCommand {
                isCommand(BotCommand.START) {
                    send {
                        textWithEntities {
                            text("Use the button to open WebApp").n()
                            n()
                            textLink("Open via link", "https://t.me/${webappBot.me.username}?startapp").n()
                            textLink("Open specific page via link", "https://t.me/${webappBot.me.username}?startapp=info").n()
                        }
                        inlineKeyboard {
                            row {
                                button {
                                    text = "Open via button"
                                    webApp = WebAppInfo(host)
                                }
                            }
                        }
                    }
                }
            }

            handleMessage {
                messageContact {
                    send("Contact was received: ${it.phoneNumber}")
                } ?:

                messageWebAppData {
                    send {
                        textWithEntities {
                            text("WebAppData was received:").n()
                            text("• data: ").code(it.data).n()
                            text("• button_text: ").code(it.buttonText).n()
                        }
                    }
                } ?:

                messageStory {
                    println(prettyPrintJson.encodeToString(it))
                } ?:

                println(message)
            }

            handlePreCheckoutQuery {
                println("answerPreCheckoutQuery: ${answerPreCheckoutQuery(true)}")
            }
        }
    }

    embeddedServer(Netty,
        configure = {
            connector {
                port = portHttp
            }

            if (System.getenv("cert")?.toBoolean() ?: false) {
                val cert = File("cert.p12")
                sslConnector(
                    keyStore = cert.inputStream().use {
                        java.security.KeyStore.getInstance("PKCS12").apply {
                            load(it, "demondrop".toCharArray())
                        }
                    },
                    keyAlias = "back",
                    keyStorePassword = { "demondrop".toCharArray() },
                    privateKeyPassword = { "demondrop".toCharArray() }
                ) {
                    port = portHttps
                    keyStorePath = cert
                }
            }
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
                if (webappBot.verifyWebAppInitData(initData)) {
                    call.respond(HttpStatusCode.Accepted)
                } else {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            get("/createInvoiceLink") {
                if (paymentProviderToken != null) {
                    val link = botContext(webappBot) {
                        createInvoiceLink {
                            title = "Test invoice"
                            description = "Test invoice description"
                            payload = "payload"
                            currency = Currency.RUB
                            prices = listOf(LabeledPrice("Test", Currency.RUB.ofNative(299)))
                            providerToken = paymentProviderToken
                        }
                    }

                    call.respond(link)
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Payment provider token is not set. Add PAYMENT_PROVIDER_TOKEN environment variable to enable this feature.")
                }
            }

            get("/preparedMessageId") {
                val userId = call.parameters.getOrFail<Long>("userId")
                call.respond(preparedMessageId ?: run {
                    webappBot.savePreparedInlineMessage(
                        userId = ChatId.ID(userId),
                        result = InlineQueryResultArticle(
                            id = "id",
                            title = "Title",
                            inputMessageContent = InputTextMessageContent(
                                messageText = "Message text"
                            ),
                        ),
                        allowBotChats = true,
                    )
                        .also { preparedMessageId = it.id }
                        .id
                })
            }

        }
    }.start(wait = true)
}
