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
import org.slf4j.LoggerFactory
import retrofit2.Retrofit
import ru.raysmith.tgbot.core.BotException
import ru.raysmith.tgbot.model.network.Error
import ru.raysmith.tgbot.model.network.keyboard.KeyboardMarkup
import ru.raysmith.tgbot.model.network.keyboard.ReplyKeyboardMarkup
import ru.raysmith.tgbot.model.network.media.input.InputMedia
import ru.raysmith.tgbot.model.network.media.input.InputMediaPhoto
import ru.raysmith.utils.PropertiesFactory
import ru.raysmith.utils.properties.getOrDefault
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalSerializationApi::class)
object TelegramApi {

    val logger = LoggerFactory.getLogger("tg-api")
    private var TOKEN = PropertiesFactory.from("bot.properties").getOrDefault<String>("token", "")!!

    fun setToken(newToken: String) {
        TOKEN = newToken
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
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

    val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
        encodeDefaults = false
        classDiscriminator = "clazz" // resolve `type` field naming conflict
        serializersModule = SerializersModule {
            polymorphic(KeyboardMarkup::class) {
                subclass(ReplyKeyboardMarkup::class, ReplyKeyboardMarkup.serializer())
            }
            polymorphic(InputMedia::class) {
                subclass(InputMediaPhoto::class, InputMediaPhoto.serializer())
            }
        }
    }

    private fun getRetrofit(token: String): Retrofit {
        if (token.isEmpty()) throw BotException("Token is empty")

        return Retrofit.Builder()
            .baseUrl("https://api.telegram.org/bot$token/")
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .addConverterFactory(NetworkUtils.EnumConverterFactory())
            .build()
    }

    private val fileRetrofit by lazy {
        if (TOKEN.isEmpty()) throw BotException("Token is empty")

        Retrofit.Builder()
            .baseUrl("https://api.telegram.org/file/bot$TOKEN/")
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .addConverterFactory(NetworkUtils.EnumConverterFactory())
            .build()
    }

    private val services = mutableMapOf<String, TelegramService>()
    val service by lazy { getRetrofit(TOKEN).create(TelegramService::class.java) }
    val fileService by lazy { fileRetrofit.create(TelegramFileService::class.java) }

    fun serviceWithToken(token: String): TelegramService {
        return services[token] ?: run {
            val newService = getRetrofit(token).create(TelegramService::class.java)
            services[token] = newService
            newService
        }
    }
}

