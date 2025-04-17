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
import ru.raysmith.tgbot.core.send
import ru.raysmith.tgbot.model.Currency
import ru.raysmith.tgbot.model.bot.BotCommand
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.inline.content.InputTextMessageContent
import ru.raysmith.tgbot.model.network.inline.result.InlineQueryResultArticle
import ru.raysmith.tgbot.model.network.menubutton.WebAppInfo
import ru.raysmith.tgbot.model.network.payment.LabeledPrice
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.utils.*
import java.net.InetAddress

private val localIp by lazy {
    InetAddress.getLocalHost().hostAddress
}

private val port = System.getenv("PORT")?.toInt() ?: 8080
private val host = System.getenv("HOST") ?: "http://$localIp:$port"
private val useTestServer = System.getenv("TEST_SERVER") == "true"

val bot = Bot(useTestServer = useTestServer)
    .onError { Bot.logger.error(it) }

val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

var preparedMessageId: String? = null

fun main() {
    scope.launch {
        val prettyPrintJson = Json(TelegramApi.json) {
            prettyPrint = true
            prettyPrintIndent = " "
        }

        bot.start {
            handleCommand {
                isCommand(BotCommand.START) {
                    send {
                        text = "Use the button to open WebApp"
                        textWithEntities {
                            textLink("Open via link", "https://t.me/${ru.raysmith.tgbot.webappapp.bot.me.username}?startapp").n()
                            textLink("Open specific page via link", "https://t.me/${ru.raysmith.tgbot.webappapp.bot.me.username}?startapp=info").n()
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
                port = ru.raysmith.tgbot.webappapp.port
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
                if (bot.verifyWebAppInitData(initData)) {
                    call.respond(HttpStatusCode.Accepted)
                } else {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            get("/createInvoiceLink") {
                val link = botContext(bot) {
                    createInvoiceLink {
                        title = "Test invoice"
                        description = "Test invoice description"
                        payload = "payload"
                        currency = Currency.RUB
                        prices = listOf(LabeledPrice("Test", Currency.RUB.ofNative(299)))
                        providerToken = "381764678:TEST:29287"
                    }
                }

                call.respond(link)
            }

            get("/preparedMessageId") {
                val userId = call.parameters.getOrFail<Long>("userId")
                call.respond(preparedMessageId ?: run {
                    bot.savePreparedInlineMessage(
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