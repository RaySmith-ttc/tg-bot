package ru.raysmith.tgbot.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import retrofit2.Retrofit
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.exceptions.BotException
import ru.raysmith.tgbot.model.bot.ChatIdConverterFactory
import ru.raysmith.tgbot.model.bot.MessageInlineKeyboard
import ru.raysmith.tgbot.model.bot.MessageKeyboard
import ru.raysmith.tgbot.model.bot.MessageReplyKeyboard
import ru.raysmith.tgbot.model.network.*
import ru.raysmith.tgbot.model.network.command.*
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardMarkup
import ru.raysmith.tgbot.model.network.keyboard.KeyboardMarkup
import ru.raysmith.tgbot.model.network.keyboard.ReplyKeyboardMarkup
import ru.raysmith.tgbot.model.network.keyboard.ReplyKeyboardRemove
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalSerializationApi::class)
object TelegramApi {

    val logger: Logger = LoggerFactory.getLogger("tg-api")
    private var TOKEN = Bot.properties?.getOrDefault("token", null) as? String
    const val BASE_URL = "https://api.telegram.org"

    fun setToken(newToken: String) {
        TOKEN = newToken
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

    val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
        encodeDefaults = false
        classDiscriminator = "clazz" // resolve `type` field naming conflict
        serializersModule = SerializersModule {
            polymorphic(KeyboardMarkup::class) {
                subclass(ReplyKeyboardMarkup::class, ReplyKeyboardMarkup.serializer())
                subclass(ReplyKeyboardRemove::class, ReplyKeyboardRemove.serializer())
                subclass(InlineKeyboardMarkup::class, InlineKeyboardMarkup.serializer())
            }
            polymorphic(InlineQueryResult::class) {
                subclass(InlineQueryResultArticle::class, InlineQueryResultArticle.serializer())
            }
            polymorphic(InputMessageContent::class) {
                subclass(InputTextMessageContent::class, InputTextMessageContent.serializer())
            }
            polymorphic(MessageKeyboard::class) {
                subclass(MessageInlineKeyboard::class, MessageInlineKeyboard.serializer())
                subclass(MessageReplyKeyboard::class, MessageReplyKeyboard.serializer())
            }
            polymorphic(BotCommandScope::class) {
                subclass(BotCommandScopeAllChatAdministrators::class, BotCommandScopeAllChatAdministrators.serializer())
                subclass(BotCommandScopeAllGroupChats::class, BotCommandScopeAllGroupChats.serializer())
                subclass(BotCommandScopeAllPrivateChats::class, BotCommandScopeAllPrivateChats.serializer())
                subclass(BotCommandScopeChat::class, BotCommandScopeChat.serializer())
                subclass(BotCommandScopeChatAdministrators::class, BotCommandScopeChatAdministrators.serializer())
                subclass(BotCommandScopeChatMember::class, BotCommandScopeChatMember.serializer())
                subclass(BotCommandScopeDefault::class, BotCommandScopeDefault.serializer())
            }
        }
    }

    fun buildService(token: String?, type: ServiceType): Retrofit {
        if (token == null) throw BotException("Token is not set")

        return Retrofit.Builder()
            .baseUrl(type.getBaseUrl(token))
            .client(getClient())
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .addConverterFactory(NetworkUtils.ConverterFactory())
            .addConverterFactory(ChatIdConverterFactory())
            .build()
    }

    private val services = mutableMapOf<String, TelegramService>()
    private val filesServices = mutableMapOf<String, TelegramFileService>()

    val service: TelegramService by lazy { buildService(TOKEN, ServiceType.DEFAULT).create(TelegramService::class.java) }
    val fileService: TelegramFileService by lazy { buildService(TOKEN, ServiceType.FILE).create(TelegramFileService::class.java) }

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

