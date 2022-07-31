package ru.raysmith.tgbot.model.bot

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
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

object ChatIdSerializer : JsonContentPolymorphicSerializer<ChatId>(ChatId::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out ChatId> {
        println("RES: ${element.jsonPrimitive.isString}")
        return if (element.jsonPrimitive.isString) ChatId.UsernameSerializer else ChatId.IDSerializer
    }
}

@Polymorphic
@Serializable(with = ChatIdSerializer::class)
sealed class ChatId {

    override fun equals(other: Any?): Boolean {
        return if (other == null || other !is ChatId) false
        else toStringValue() == other.toStringValue()
    }

    override fun hashCode(): Int {
        return toStringValue().hashCode()
    }

    fun toRequestBody(): RequestBody = when (this) {
        is ID -> value.toString().toRequestBody()
        is Username -> username.toRequestBody()
    }

    fun toStringValue() = when (this) {
        is ID -> value.toString()
        is Username -> username
    }

    companion object {
        fun of(value: Any): ChatId {
            return when (value) {
                is Number -> ID(value.toLong())
                is String -> Username(value)
                else -> throw IllegalArgumentException("ChatId can only be created from Long or String")
            }
        }
    }

    @Serializable(with = IDSerializer::class)
    data class ID(val value: Long) : ChatId()

    @Serializable(with = UsernameSerializer::class)
    data class Username(val username: String) : ChatId()

    object IDSerializer : KSerializer<ID> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ChatId.ID", PrimitiveKind.LONG)
        override fun deserialize(decoder: Decoder): ID = ID(decoder.decodeLong())
        override fun serialize(encoder: Encoder, value: ID) {
            encoder.encodeLong(value.value)
        }
    }

    object UsernameSerializer : KSerializer<Username> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ChatId.Username", PrimitiveKind.STRING)
        override fun deserialize(decoder: Decoder): Username = Username(decoder.decodeString())
        override fun serialize(encoder: Encoder, value: Username) {
            encoder.encodeString(value.username)
        }
    }
}