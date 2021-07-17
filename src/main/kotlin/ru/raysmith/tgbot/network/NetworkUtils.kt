package ru.raysmith.tgbot.network

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.serializer
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import kotlin.reflect.KClass

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

    // TODO it's need?
//    object AnySerializer : KSerializer<Any> {
//        override val descriptor: SerialDescriptor = String.serializer().descriptor
//
//        override fun deserialize(decoder: Decoder): Any {
//            require(decoder is JsonDecoder).let {
//                val element = decoder.decodeJsonElement()
//                return element.toString()
//            }
//        }
//
//        override fun serialize(encoder: Encoder, value: Any) {
//            encoder.encodeString(value.toString())
//        }
//    }
}