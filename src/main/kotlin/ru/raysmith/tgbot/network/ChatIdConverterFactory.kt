package ru.raysmith.tgbot.network

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import ru.raysmith.tgbot.model.bot.ChatId
import java.lang.reflect.Type

internal class ChatIdConverterFactory : Converter.Factory() {
    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        if (type != ChatId::class.java) return null

        return Converter<ChatId, RequestBody> { chatId -> chatId.toRequestBody() }
    }

    override fun stringConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, String>? {
        if (type != ChatId::class.java) return null

        return Converter<ChatId, String> { chatId -> chatId.toStringValue() }
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        if (type != ChatId::class.java) return null

        return Converter<ResponseBody, ChatId?> { body -> body.string().toLongOrNull()?.let { ChatId.of(it) } }
    }
}