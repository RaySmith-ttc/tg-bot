package ru.raysmith.tgbot.model.bot

import kotlinx.serialization.json.encodeToJsonElement
import ru.raysmith.tgbot.model.network.message.MessageEntity
import ru.raysmith.tgbot.model.network.message.MessageEntityType
import ru.raysmith.tgbot.network.TelegramApi
import java.lang.StringBuilder

/**
 * Represents message text or caption as string with entities
 * */
class MessageText {
    private val text: StringBuilder = StringBuilder()
    val currentTextLength: Int
        get() = text.length

    private var entities: MutableList<MessageEntity> = mutableListOf()

    internal fun getEntitiesString() = TelegramApi.json.encodeToJsonElement(entities).toString()
    internal fun getTextString() = text.toString()

    fun append(text: String) = this.text.append(text)

    fun italic(text: String) = appendEntity(MessageEntityType.ITALIC, text)
    fun bold(text: String) = appendEntity(MessageEntityType.BOLD, text)
    fun underline(text: String) = appendEntity(MessageEntityType.UNDERLINE, text)
    fun phoneNumber(text: String) = appendEntity(MessageEntityType.PHONE_NUMBER, text)
    fun url(text: String) = appendEntity(MessageEntityType.URL, text)

    private fun appendEntity(type: MessageEntityType, text: String) {
        entities.add(MessageEntity(type, offset = this.text.length, text.length))
        this.text.append(text)
    }

    fun entity(type: MessageEntityType, init: MessageEntityBuilder.() -> Unit): MessageEntity {
       return MessageEntityBuilder(type).apply(init).toEntity().also {
           entities.add(it)
       }
    }
}