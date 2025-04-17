package ru.raysmith.tgbot.network

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.api.*
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
import ru.raysmith.tgbot.utils.obtainToken
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

private val UnauthenticatedRequestKey = AttributeKey<Unit>("UnauthenticatedRequestKey")

fun HttpRequestBuilder.unauthenticated() {
    attributes.put(UnauthenticatedRequestKey, Unit)
}

class TelegramTestServerConfig {
    var token: String? = null
}
val TelegramTestServer = createClientPlugin("TelegramTestServer", ::TelegramTestServerConfig) {
    val token = pluginConfig.token
        ?: client.pluginOrNull(Token)?.token
        ?: error("Can't install TelegramTestServer plugin to http client: token not found. Provide it with TG_BOT_TOKEN environment variable or token property")

    onRequest { request, _ ->
        if (!request.attributes.contains(UnauthenticatedRequestKey)) {
            request.url.encodedPath = "/bot${token}/test/${request.url.pathSegments.drop(2).joinToString("/")}"
        }
    }
}

class Token(val token: String) {
    class Config {
        var token: String = ""
    }

    companion object : HttpClientPlugin<Config, Token> {
        override val key = AttributeKey<Token>("Token")

        override fun prepare(block: Config.() -> Unit): Token {
            val config = Config().apply(block)
            return Token(config.token)
        }

        override fun install(plugin: Token, scope: HttpClient) {
//            scope.requestPipeline.intercept(HttpRequestPipeline.Before) {
//                val isUnauthenticatedRequest = context.attributes.contains(UnauthenticatedRequestKey)
//
//                if (!isUnauthenticatedRequest) {
//                    context.url.encodedPath = "/bot${plugin.token}/${context.url.encodedPath}"
//                }
//            }
        }
    }
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
        token: String = obtainToken() ?: error("Can't create default http client: token not found. Provide it with TG_BOT_TOKEN environment variable or token property"),
        useTestServer: Boolean = false,
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
            retryOnException(retryOnTimeout = true)
            retryIf(Int.MAX_VALUE) { _, response ->
                response.status.value.let { it in 500..599 } ||
                response.status == HttpStatusCode.TooManyRequests
            }
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

        install(Token) {
            this.token = token
        }

        defaultRequest {
            url("$BASE_URL/bot${token}/${if (useTestServer) "test/" else ""}")

            headers {
                contentType(ContentType.Application.Json)
            }
        }

        builder()
    }

    fun withToken(token: String) = defaultClient(token)
}