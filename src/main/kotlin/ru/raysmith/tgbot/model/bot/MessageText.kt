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

    /**
     * Set true for apply null strings to message text
     *
     * ```
     * textWithEntities {
     *     printNulls = true
     *     text("Some text: ").text(null) // Output: 'Some text: null'
     * }
     *
     * textWithEntities {
     *     printNulls = false
     *     text("Some text: ").text(null) // Output: 'Some text: '
     * }
     * ```
     * */
    // TODO tests
    var printNulls: Boolean = false

    internal fun getEntitiesString() = TelegramApi.json.encodeToJsonElement(entities).toString()
    internal fun getTextString() = text.toString()

    fun text(text: Any?): MessageText {
        if (!printNulls && text == null) return this
        this.text.append(text)
        return this
    }
    fun mention(text: Any?) = appendEntity(MessageEntityType.MENTION, text)
    fun hashtag(text: Any?) = appendEntity(MessageEntityType.HASHTAG, text)
    fun cashtag(text: Any?) = appendEntity(MessageEntityType.CASHTAG, text)
    fun botCommand(text: Any?) = appendEntity(MessageEntityType.BOT_COMMAND, text)
    fun url(text: Any?) = appendEntity(MessageEntityType.URL, text)
    fun email(text: Any?) = appendEntity(MessageEntityType.EMAIL, text)
    fun phoneNumber(text: Any?) = appendEntity(MessageEntityType.PHONE_NUMBER, text)
    fun bold(text: Any?) = appendEntity(MessageEntityType.BOLD, text)
    fun italic(text: Any?) = appendEntity(MessageEntityType.ITALIC, text)
    fun underline(text: Any?) = appendEntity(MessageEntityType.UNDERLINE, text)
    fun strikethrough(text: Any?) = appendEntity(MessageEntityType.STRIKETHROUGH, text)
    fun code(text: Any?) = appendEntity(MessageEntityType.CODE, text)
    fun pre(text: Any?, language: String) = appendEntity(MessageEntityType.PRE, text, language)
    fun textLink(text: Any?, url: String) = appendEntity(MessageEntityType.TEXT_LINK, text, url)

    // TODO add user. https://core.telegram.org/bots/api#messageentity
    fun textMention(text: String?) = appendEntity(MessageEntityType.TEXT_MENTION, text)

    private fun appendEntity(type: MessageEntityType, text: Any?, url: String? = null, language: String? = null): MessageText {
        text.toString().also { t ->
            entities.add(MessageEntity(type, offset = this.text.length, t.length, url = url, language = language))
            this.text.append(t)
        }
        return this
    }

    fun entity(type: MessageEntityType, init: MessageEntityBuilder.() -> Unit): MessageEntity {
       return MessageEntityBuilder(type).apply(init).toEntity().also {
           entities.add(it)
       }
    }
}