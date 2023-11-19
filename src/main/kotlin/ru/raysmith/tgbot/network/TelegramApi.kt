package ru.raysmith.tgbot.network

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.model.network.chat.member.*
import ru.raysmith.tgbot.model.network.inline.content.InputMessageContent
import ru.raysmith.tgbot.model.network.inline.content.InputTextMessageContent
import kotlin.time.Duration.Companion.seconds

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

object TelegramApi2 {
    val logger: Logger = LoggerFactory.getLogger("tg-api")
    const val BASE_URL = "https://api.telegram.org"

    val defaultClientInstance by lazy { defaultClient() }

    val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
        encodeDefaults = false
        classDiscriminator = "#type"
        namingStrategy = JsonNamingStrategy.SnakeCase

        serializersModule = SerializersModule {
            polymorphic(InputMessageContent::class) {
                subclass(InputTextMessageContent::class)
            }

            // TODO (?) test: check with consist
            polymorphic(ChatMember::class) {
                subclass(ChatMemberOwner::class)
                subclass(ChatMemberLeft::class)
                subclass(ChatMemberMember::class)
                subclass(ChatMemberAdministrator::class)
                subclass(ChatMemberBanned::class)
                subclass(ChatMemberRestricted::class)
            }
        }
    }

    fun defaultClient(
        token: String = Bot.config.token,
        builder: HttpClientConfig<CIOEngineConfig>.() -> Unit = {}
    ) = HttpClient(CIO) {
        engine {
            requestTimeout = 60.seconds.inWholeMilliseconds
        }

        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 5)
            exponentialDelay()
        }

        install(Logging) {
            this.logger = object : io.ktor.client.plugins.logging.Logger {
                override fun log(message: String) {
                    this@TelegramApi2.logger.debug(message)
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