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
import ru.raysmith.tgbot.model.network.Error
import ru.raysmith.tgbot.model.network.inputmedia.InputMedia
import ru.raysmith.tgbot.model.network.inputmedia.InputMediaPhoto
import ru.raysmith.tgbot.model.network.keyboard.KeyboardMarkup
import ru.raysmith.tgbot.model.network.keyboard.ReplyKeyboardMarkup
import ru.raysmith.utils.PropertiesFactory
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalSerializationApi::class)
object TelegramApi {

    val logger = LoggerFactory.getLogger("tg-api")
    private var TOKEN = PropertiesFactory.from("bot.properties").get("token")

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(
            HttpLoggingInterceptor(TelegramLoggingInterceptor).apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }
        )
        .addInterceptor { chain ->
            val response = chain.proceed(chain.request())

            if (!response.isSuccessful && response.body != null) {
                val error = json.decodeFromString<Error>(response.body!!.string())
                throw TelegramApiException(error)
            }

            response
        }
        .build()

    val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
        prettyPrint = true
        classDiscriminator = "clazz" // resolve `type` field naming conflict
        serializersModule = SerializersModule {
//            contextual(NetworkUtils.AnySerializer)
            polymorphic(KeyboardMarkup::class) {
                subclass(ReplyKeyboardMarkup::class, ReplyKeyboardMarkup.serializer())
            }
            polymorphic(InputMedia::class) {
                subclass(InputMediaPhoto::class, InputMediaPhoto.serializer())
            }
        }
    }

    private fun getRetrofit(token: String) = Retrofit.Builder()
            .baseUrl("https://api.telegram.org/bot$token/")
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .addConverterFactory(NetworkUtils.EnumConverterFactory())
            .build()

    private val fileRetrofit = Retrofit.Builder()
        .baseUrl("https://api.telegram.org/file/bot$TOKEN/")
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .addConverterFactory(NetworkUtils.EnumConverterFactory())
        .build()

    private val services = mutableMapOf<String, TelegramService>()
    val service = getRetrofit(TOKEN).create(TelegramService::class.java)
    val fileService = fileRetrofit.create(TelegramFileService::class.java)

    fun serviceWithToken(token: String): TelegramService {
        return services[token] ?: run {
            val newService = getRetrofit(token).create(TelegramService::class.java)
            services[token] = newService
            newService
        }
    }
}

