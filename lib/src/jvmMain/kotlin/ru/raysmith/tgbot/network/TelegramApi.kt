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
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.raysmith.tgbot.core.Bot
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
            retryOnServerErrors(maxRetries = Int.MAX_VALUE)
            retryOnException(retryOnTimeout = true)
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