package ru.raysmith.tgbot.model.bot

import kotlinx.serialization.json.encodeToJsonElement
import ru.raysmith.tgbot.model.network.User
import ru.raysmith.tgbot.model.network.message.MessageEntity
import ru.raysmith.tgbot.model.network.message.MessageEntityType
import ru.raysmith.tgbot.model.network.message.ParseMode
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.utils.letIf
import java.lang.StringBuilder

/** Represents message text or caption as string with entities */
@TextMessageDsl
class MessageText(private val printNulls: Boolean) {
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
//    // TODO tests
//    var printNulls: Boolean = false

    fun getEntitiesString() = TelegramApi.json.encodeToJsonElement(entities.toList()).toString()

    // TODO tests
    fun getSafeEntitiesString() = TelegramApi.json.encodeToJsonElement(getSafeEntities()).toString()

    fun getSafeEntities() = entities.filter { it.offset < IMessage.MAX_TEXT_LENGTH }.toMutableList().also { filtered ->
        filtered.lastOrNull()?.let { last ->
            if (last.length + last.offset > IMessage.MAX_TEXT_LENGTH) {
                filtered[filtered.lastIndex] = last.copy(length = IMessage.MAX_TEXT_LENGTH - last.offset)
            }
        }
    }.toList()

    fun getTextString() = text.toString()

    fun getSafeTextString() = getTextString().let {
        if (it.length > IMessage.MAX_TEXT_LENGTH) it.take(IMessage.MAX_TEXT_LENGTH) else it
    }

    fun text(text: Any?): MessageText {
        if (!printNulls && text == null) return this
        this.text.append(text)
        return this
    }
    fun mention(username: Any?) = appendEntity(MessageEntityType.MENTION, username)
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
    fun spoiler(text: Any?) = appendEntity(MessageEntityType.SPOILER, text)
    fun pre(text: Any?, language: String? = null) = appendEntity(MessageEntityType.PRE, text, language = language)
    fun textLink(text: Any?, url: String) = appendEntity(MessageEntityType.TEXT_LINK, text, url = url)
    fun textMention(text: String?, user: User) = appendEntity(MessageEntityType.TEXT_MENTION, text, user = user)

    fun mix(text: Any?, vararg types: MessageEntityType) = mix(text, null, null, types = types)

    // TODO test
    fun mix(text: Any?, url: String? = null, language: String? = null, vararg types: MessageEntityType): MessageText {
        text.toString().also { t ->
            types.forEach {
                entities.add(MessageEntity(it, offset = this.text.length, t.length, url = url, language = language))
            }
            this.text.append(t)
        }
        return this
    }

    @Suppress("DEPRECATION") // MARKDOWN backward compatibility
    private fun String.escape(parseMode: ParseMode, entityType: MessageEntityType? = null): String {
        if (isEmpty()) return this
        return when(parseMode) {
            ParseMode.HTML -> replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
            ParseMode.MARKDOWN -> {
                when(entityType) {
                    MessageEntityType.BOLD -> replace("*", "*\\**")
                    MessageEntityType.ITALIC -> replace("_", "_\\__")
                    MessageEntityType.CODE -> replace("`", "`\\``")
                    MessageEntityType.URL, MessageEntityType.MENTION, MessageEntityType.TEXT_MENTION -> {
                        replace("[", "\\[")
                    }
                    else -> this.replace("_", "\\_").replace("*", "\\*").replace("`", "\\`").replace("[", "\\[")
                }
            }
            ParseMode.MARKDOWNV2 -> {
                when(entityType) {
                    MessageEntityType.PRE, MessageEntityType.CODE -> replace("\\", "\\\\").replace("`", "\\`")
                    else -> {
                        var res = this
                        listOf("_", "*", "[", "]", "(", ")", "~", "`", ">", "#", "+", "-", "=", "|", "{", "}", ".", "!").forEach {
                            res = res.replace(it, "\\$it")
                        }

                        res
                    }
                }
            }
        }
    }

    fun format(parseMode: ParseMode, safeLength: Boolean = true): String {
        val textString = if (safeLength) getSafeTextString() else getTextString()
        if (entities.isEmpty()) return textString.escape(parseMode)

        val entities = if (safeLength) getSafeEntities() else entities
        fun MessageEntity.isSameLengthAndOffset(other: MessageEntity): Boolean {
            return this.length == other.length && this.offset == other.offset
        }
        if (parseMode == ParseMode.MARKDOWNV2) {
            entities.forEachIndexed { i, entity ->
                if (i != 0 && i != entities.size - 1 &&
                    (entity.type == MessageEntityType.ITALIC || entity.type == MessageEntityType.UNDERLINE) && (
                            ((entities[i - 1].type == MessageEntityType.ITALIC || entities[i - 1].type == MessageEntityType.UNDERLINE) && !entities[i - 1].isSameLengthAndOffset(entity)) ||
                            ((entities[i + 1].type == MessageEntityType.ITALIC || entities[i + 1].type == MessageEntityType.UNDERLINE) && !entities[i + 1].isSameLengthAndOffset(entity))
                    )
                ) {
                    throw IllegalStateException("MarkdownV2 not allowed to append italic and underline entities. Use the mix method or create an HTML string instead")
                }
            }
        }

        val pathsOfEntities = mutableListOf<Int>()
        return buildString {
            // text before first entity
            append(textString.substring(0, entities.first().offset).escape(parseMode))

            // entities
            entities.forEachIndexed { i, entity ->
                val mix = entities.filterIndexed { j, e -> j != i && e.length == entity.length && e.offset == entity.offset && pathsOfEntities.add(j) }.let {
                    if (it.isNotEmpty()) it.toMutableList().apply { add(entity) } else it
                }
                val usedPartOfMix = pathsOfEntities.contains(i)
                if (mix.isEmpty()) {
                    append(
                        entity.formatString(
                            textString
                                .substring(entity.offset, entity.offset + entity.length)
                                .escape(parseMode, entity.type), parseMode
                        )
                    )
                } else if (!usedPartOfMix) {
                    pathsOfEntities.add(i)
                    append(
                        mix.fold("") { acc, messageEntity ->
                            messageEntity.formatString(
                                acc.ifEmpty {
                                    textString
                                        .substring(messageEntity.offset, messageEntity.offset + messageEntity.length)
                                        .escape(parseMode, messageEntity.type)
                                }, parseMode
                            )
                        }.letIf(parseMode == ParseMode.MARKDOWNV2) { it.replace("___$".toRegex(), "_${"\n"}__") }
                    )
                }

                // text without entity between two entities
                if (entities.size != i + 1/* && entity.offset + entity.length != entities[i + 1].offset*/) {
                    var end: Int? = null
                    for (j in entities.indices) {
                        if (j > i) {
                            if (entities[j].offset >= entity.offset + entity.length) {
                                end = entities[j].offset
                                break
                            }
                        }
                    }

                    append(
                        textString
                            .substring(entity.offset + entity.length, end ?: entities.last().let { it.offset + it.length })
                            .escape(parseMode)
                    )
                }
            }

            //text after last entity
            append(textString.substring(entities.last().let { it.offset + it.length }).escape(parseMode))
        }
    }

    fun appendEntity(type: MessageEntityType, text: Any?, url: String? = null, language: String? = null, user: User? = null): MessageText {
        text.toString().also { t ->
            entities.add(MessageEntity(type, offset = this.text.length, t.length, url = url, language = language, user = user))
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