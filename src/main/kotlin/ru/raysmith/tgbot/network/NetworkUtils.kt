package ru.raysmith.tgbot.network

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.serializer
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

internal object NetworkUtils {

    class ConverterFactory : Converter.Factory() {
        override fun stringConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<*, String>? {
            if (type !is Class<*>) return null
            if (type.isEnum) return EnumConverter

            return when (type) {
                String::class.java -> null
                else -> jsonConverter { serializer(type) }
            }
        }
    }

    val EnumConverter = Converter<Enum<*>, String> { it.name.lowercase() }

    fun <T> jsonConverter(block: () -> KSerializer<T>) =
        Converter<T, String> { TelegramApi2.json.encodeToString(block(), it) }

    private inline fun <reified A : Annotation> Enum<*>.getEnumFieldAnnotation(): A? =
        javaClass.getDeclaredField(name).getAnnotation(A::class.java)

    fun Enum<*>.getSerialName(): String = getEnumFieldAnnotation<SerialName>()?.value ?: name
}