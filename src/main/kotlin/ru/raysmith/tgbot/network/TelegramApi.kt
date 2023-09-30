package ru.raysmith.tgbot.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import retrofit2.Retrofit
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.exceptions.BotException
import ru.raysmith.tgbot.model.network.Error
import ru.raysmith.tgbot.model.network.chat.member.*
import ru.raysmith.tgbot.model.network.inline.content.InputMessageContent
import ru.raysmith.tgbot.model.network.inline.content.InputTextMessageContent
import java.util.concurrent.TimeUnit
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
                accept(ContentType.Application.Json)
            }
        }

        builder()
    }

    fun withToken(token: String) = defaultClient(token)
}

@OptIn(ExperimentalSerializationApi::class)
object TelegramApi {

    val logger: Logger = LoggerFactory.getLogger("tg-api")
    private var TOKEN = Bot.config.token
    const val BASE_URL = "https://api.telegram.org"

    fun setToken(newToken: String) {
        TOKEN = newToken
    }

    internal val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
        encodeDefaults = false
        classDiscriminator = "#type"
        namingStrategy = JsonNamingStrategy.SnakeCase

        serializersModule = SerializersModule {
            polymorphic(InputMessageContent::class) {
                subclass(InputTextMessageContent::class)
            }
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

    private fun getClient() = client ?: defaultClient
    private var client: OkHttpClient? = null
    val defaultClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(
                HttpLoggingInterceptor(TelegramLoggingInterceptor).apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                }
            )
            .addInterceptor { chain ->
                val request = chain.request()
                val response = chain.proceed(request)

                if (!response.isSuccessful && response.body != null) {
                    val error = json.decodeFromString<Error>(response.body!!.string())
                    if (error.errorCode == 401) {
                        throw BotException("Bot is unauthorized with token")
                    } else throw TelegramApiException(error, request)
                }

                response
            }
            .build()
    }

    fun setClient(client: OkHttpClient) {
        this.client = client
    }

    fun buildService(token: String?, type: ServiceType): Retrofit {
        if (token == null) throw BotException("Token is not set")

        return Retrofit.Builder()
            .baseUrl(type.getBaseUrl(token))
            .client(getClient())
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .addConverterFactory(NetworkUtils.ConverterFactory())
            .build()
    }


    private val services = mutableMapOf<String, TelegramService>()
    private val filesServices = mutableMapOf<String, TelegramFileService>()

    val service: TelegramService by lazy {
        buildService(
            TOKEN,
            ServiceType.DEFAULT
        ).create(TelegramService::class.java)
    }
    val fileService: TelegramFileService by lazy {
        buildService(
            TOKEN,
            ServiceType.FILE
        ).create(TelegramFileService::class.java)
    }

    fun serviceWithToken(token: String): TelegramService {
        return services[token] ?: run {
            val newService = buildService(token, ServiceType.DEFAULT).create(TelegramService::class.java)
            services[token] = newService
            newService
        }
    }

    fun fileServiceWithToken(token: String): TelegramFileService {
        return filesServices[token] ?: run {
            val newService = buildService(token, ServiceType.FILE).create(TelegramFileService::class.java)
            filesServices[token] = newService
            newService
        }
    }
}

