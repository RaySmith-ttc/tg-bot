package ru.raysmith.tgbot.network

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import kotlinx.serialization.json.ClassDiscriminatorMode
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.model.network.chat.member.*
import ru.raysmith.tgbot.model.network.command.*
import ru.raysmith.tgbot.model.network.inline.content.*
import ru.raysmith.tgbot.model.network.inline.result.*
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardMarkup
import ru.raysmith.tgbot.model.network.keyboard.KeyboardMarkup
import ru.raysmith.tgbot.model.network.keyboard.ReplyKeyboardMarkup
import ru.raysmith.tgbot.model.network.keyboard.ReplyKeyboardRemove
import ru.raysmith.tgbot.model.network.media.input.*
import ru.raysmith.tgbot.model.network.menubutton.MenuButton
import ru.raysmith.tgbot.model.network.menubutton.MenuButtonCommands
import ru.raysmith.tgbot.model.network.menubutton.MenuButtonDefault
import ru.raysmith.tgbot.model.network.menubutton.MenuButtonWebApp
import ru.raysmith.tgbot.model.network.response.BooleanResponse
import ru.raysmith.tgbot.model.network.response.LiveLocationResponse
import ru.raysmith.tgbot.model.network.response.MessageResponse
import ru.raysmith.utils.properties.getOrNull
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

private val UnauthenticatedRequestKey = AttributeKey<Unit>("UnauthenticatedRequestKey")

fun HttpRequestBuilder.unauthenticated() {
    attributes.put(UnauthenticatedRequestKey, Unit)
}

class TokenAuthorization(val token: String) {
    class Config {
        var token: String = ""
    }

    companion object : HttpClientPlugin<Config, TokenAuthorization> {
        override val key = AttributeKey<TokenAuthorization>("TokenAuthorization")

        override fun prepare(block: Config.() -> Unit): TokenAuthorization {
            val config = Config().apply(block)
            return TokenAuthorization(config.token)
        }

        override fun install(plugin: TokenAuthorization, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.Before) {
                val isUnauthenticatedRequest = context.attributes.contains(UnauthenticatedRequestKey)

                if (!isUnauthenticatedRequest) {
                    context.url.encodedPath = "/bot${plugin.token}/${context.url.encodedPath}"
                }
            }
        }
    }
}

fun HttpClientConfig<*>.tokenAuth(block: TokenAuthorization.Config.() -> Unit) {
    install(TokenAuthorization, block)
}

object TelegramApi {
    val logger: Logger = LoggerFactory.getLogger("tg-api")
    const val BASE_URL = "https://api.telegram.org"

    val defaultClientInstance by lazy { defaultClient() }

    val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
        encodeDefaults = false
        classDiscriminator = "#type"
        classDiscriminatorMode = ClassDiscriminatorMode.NONE
        namingStrategy = JsonNamingStrategy.SnakeCase

        serializersModule = SerializersModule {
            polymorphic(InputMessageContent::class) {
                subclass(InputTextMessageContent::class)
            }

            polymorphic(BotCommandScope::class) {
                subclass(BotCommandScopeDefault::class)
                subclass(BotCommandScopeAllPrivateChats::class)
                subclass(BotCommandScopeAllGroupChats::class)
                subclass(BotCommandScopeAllChatAdministrators::class)
                subclass(BotCommandScopeChat::class)
                subclass(BotCommandScopeChatAdministrators::class)
                subclass(BotCommandScopeChatMember::class)
            }

            polymorphic(InputMessageContent::class) {
                subclass(InputTextMessageContent::class)
                subclass(InputLocationMessageContent::class)
                subclass(InputVenueMessageContent::class)
                subclass(InputContactMessageContent::class)
                subclass(InputInvoiceMessageContent::class)
            }

            polymorphic(InlineQueryResult::class) {
                subclass(InlineQueryResultCachedAudio::class)
                subclass(InlineQueryResultCachedDocument::class)
                subclass(InlineQueryResultCachedGif::class)
                subclass(InlineQueryResultCachedMpeg4Gif::class)
                subclass(InlineQueryResultCachedPhoto::class)
                subclass(InlineQueryResultCachedSticker::class)
                subclass(InlineQueryResultCachedVideo::class)
                subclass(InlineQueryResultCachedVoice::class)
                subclass(InlineQueryResultArticle::class)
                subclass(InlineQueryResultAudio::class)
                subclass(InlineQueryResultContact::class)
                subclass(InlineQueryResultGame::class)
                subclass(InlineQueryResultDocument::class)
                subclass(InlineQueryResultGif::class)
                subclass(InlineQueryResultLocation::class)
                subclass(InlineQueryResultMpeg4Gif::class)
                subclass(InlineQueryResultPhoto::class)
                subclass(InlineQueryResultVenue::class)
                subclass(InlineQueryResultVideo::class)
                subclass(InlineQueryResultVoice::class)
            }

            polymorphic(ChatMember::class) {
                subclass(ChatMemberOwner::class)
                subclass(ChatMemberLeft::class)
                subclass(ChatMemberMember::class)
                subclass(ChatMemberAdministrator::class)
                subclass(ChatMemberBanned::class)
                subclass(ChatMemberRestricted::class)
            }

            polymorphic(InputMedia::class) {
                subclass(InputMediaAnimation::class)
                subclass(InputMediaDocument::class)
                subclass(InputMediaAudio::class)
                subclass(InputMediaPhoto::class)
                subclass(InputMediaVideo::class)
            }

            polymorphic(InputMediaGroup::class) {
                subclass(InputMediaDocument::class)
                subclass(InputMediaAudio::class)
                subclass(InputMediaPhoto::class)
                subclass(InputMediaVideo::class)
            }

            polymorphic(InputMediaGroupWithThumbnail::class) {
                subclass(InputMediaDocument::class)
                subclass(InputMediaAudio::class)
                subclass(InputMediaVideo::class)
            }

            polymorphic(MenuButton::class) {
                subclass(MenuButtonCommands::class)
                subclass(MenuButtonWebApp::class)
                subclass(MenuButtonDefault::class)
            }

            polymorphic(LiveLocationResponse::class) {
                subclass(BooleanResponse::class)
                subclass(MessageResponse::class)
            }

            polymorphic(KeyboardMarkup::class) {
                subclass(InlineKeyboardMarkup::class)
                subclass(ReplyKeyboardMarkup::class)
                subclass(ReplyKeyboardRemove::class)
            }
        }
    }


    fun defaultClient(
        token: String = Bot.properties?.getOrNull("token") ?: System.getenv("TG_BOT_TOKEN")
            ?: error("Can't create default http client: token not found. Provide it with TG_BOT_TOKEN environment variable or token property"),
        builder: HttpClientConfig<OkHttpConfig>.() -> Unit = {}
    ) = HttpClient(OkHttp) {
        engine {
            config {
                callTimeout(60.seconds.toJavaDuration())
                connectTimeout(60.seconds.toJavaDuration())
                readTimeout(60.seconds.toJavaDuration())
            }
        }

        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 5)
            exponentialDelay()
        }

        install(Logging) {
            this.logger = object : io.ktor.client.plugins.logging.Logger {
                override fun log(message: String) {
                    this@TelegramApi.logger.debug(message)
                }
            }
            level = LogLevel.INFO
        }

        install(ContentNegotiation) {
            json(json)
        }

        tokenAuth {
            this.token = token
        }

        defaultRequest {
            url("$BASE_URL/bot${token}/")

            headers {
                contentType(ContentType.Application.Json)
            }
        }

        builder()
    }

    fun withToken(token: String) = defaultClient(token)
}