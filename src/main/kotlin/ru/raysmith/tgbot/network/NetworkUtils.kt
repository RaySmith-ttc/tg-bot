package ru.raysmith.tgbot.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.encodeToString
import retrofit2.Converter
import retrofit2.Retrofit
import ru.raysmith.tgbot.model.network.command.BotCommand
import ru.raysmith.tgbot.model.network.command.BotCommandScope
import java.lang.reflect.Type

object NetworkUtils {
    class ConverterFactory : Converter.Factory() {
        override fun stringConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<*, String>? {
            return when {
                type is Class<*> && type.isEnum -> EnumConverter
                type is Class<*> && type == BotCommandScope::class.java -> BotCommandScopeJsonConverter
                type is Class<*> && type == BotCommand::class.java -> BotCommandJsonConverter
                else -> null
            }
        }
    }

    private object EnumConverter : Converter<Enum<*>, String> {
        override fun convert(enum: Enum<*>): String = enum.name.lowercase()
    }

    private object BotCommandScopeJsonConverter : Converter<BotCommandScope, String> {
        override fun convert(scope: BotCommandScope): String = TelegramApi.json.encodeToString(scope)
    }

    private object BotCommandJsonConverter : Converter<BotCommand, String> {
        override fun convert(command: BotCommand): String = TelegramApi.json.encodeToString(command)
    }

    private inline fun <reified A : Annotation> Enum<*>.getEnumFieldAnnotation(): A? =
        javaClass.getDeclaredField(name).getAnnotation(A::class.java)

    internal fun Enum<*>.getSerialName(): String = getEnumFieldAnnotation<SerialName>()?.value ?: name
}