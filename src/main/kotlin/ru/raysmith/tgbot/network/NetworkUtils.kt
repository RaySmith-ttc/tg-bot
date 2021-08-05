package ru.raysmith.tgbot.network

import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

object NetworkUtils {
    class EnumConverterFactory : Converter.Factory() {
        override fun stringConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<*, String>? {
            return if (type is Class<*> && type.isEnum) EnumConverter
            else null
        }
    }

    private object EnumConverter : Converter<Enum<*>, String> {
        override fun convert(enum: Enum<*>): String = enum.name.lowercase()
    }
}