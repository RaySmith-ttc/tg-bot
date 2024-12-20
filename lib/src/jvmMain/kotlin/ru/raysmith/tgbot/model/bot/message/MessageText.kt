package ru.raysmith.tgbot.model.bot.message

import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.model.network.User
import ru.raysmith.tgbot.model.network.message.MessageEntity
import ru.raysmith.tgbot.model.network.message.MessageEntityType
import ru.raysmith.tgbot.model.network.message.ParseMode
import ru.raysmith.tgbot.utils.datepicker.DatePicker
import ru.raysmith.tgbot.utils.withSafeLength
import ru.raysmith.utils.letIf

// TODO docs
//  make class abstract
//  leave only general entities functions
//  move all other functions to subclasses for each MessageTextType

/**
 * Represents message text or caption as string with entities
 * */
@TextMessageDsl
class MessageText(val type: MessageTextType, val config: BotConfig) {

    /** Whether text should be truncated if text length is greater than 4096 */
    var safeTextLength: Boolean = config.safeTextLength

    /**
     * Whether null values should display in the message text. *[BotConfig.printNulls] by default.*
     *
     * `printNulls = true`:
     * ```
     * textWithEntities {
     *     text("Some text: ").text(null) // Output: 'Some text: null'
     * }
     * ```
     *
     * `printNulls = false`:
     * ```
     * textWithEntities {
     *     text("Some text: ").text(null) // Output: 'Some text: '
     * }
     * ```
     * */
    var printNulls: Boolean = config.printNulls

    private val text: StringBuilder = StringBuilder()

    /** Current the message text length */
    val currentTextLength: Int get() = text.length

    private var entities: MutableList<MessageEntity> = mutableListOf()

    /** Returns result of [getNotSafeEntities] or [getSafeEntities] depends on [safeTextLength] */
    fun getEntities(): List<MessageEntity> = if (safeTextLength) getSafeEntities() else entities

    /** Returns all entities applied to the message text */
    fun getNotSafeEntities() = entities

    /** Returns an entity that does not exceed the [safeTextLength] boundary */
    fun getSafeEntities(): List<MessageEntity> {
        return entities
            .filter { it.offset < type.maxLength }
            .toMutableList().also { filtered ->
                filtered.lastOrNull()?.let { last ->
                    if (last.length + last.offset > type.maxLength) {
                        filtered[filtered.lastIndex] = last.copy(length = type.maxLength - last.offset)
                    }
                }
            }.toList()
    }

    /** Returns text of the message depends on [safeTextLength] */
    fun getTextString() = if (safeTextLength) text.toString().withSafeLength(type) else text.toString()

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
    fun emoji(emoji: String?, id: String) = appendEntity(MessageEntityType.CUSTOM_EMOJI, emoji, customEmojiId = id)
    fun blockquote(text: Any?) = appendEntity(MessageEntityType.BLOCKQUOTE, text)
    fun expandableBlockquote(text: Any?) = appendEntity(MessageEntityType.EXPANDABLE_BLOCKQUOTE, text)

    fun mix(text: Any?, vararg types: MessageEntityType) = mix(text, null, null, types = types)
    suspend fun datePickerMessageText(datePicker: DatePicker, data: String? = null) {
        datePicker.messageText(this, config, data, datePicker.startWithState(config, data))
    }

    fun mix(
        text: Any?, url: String? = null, language: String? = null, user: User? = null, customEmojiId: String? = null,
        vararg types: MessageEntityType
    ): MessageText {
        if (!printNulls && text == null) return this

        text.toString().also { t ->
            types.forEach {
                entities.add(MessageEntity(
                    it, offset = this.text.length, t.length, url = url, user = user, language = language,
                    customEmojiId = customEmojiId
                ))
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
                        listOf(
                            "_", "*", "[", "]", "(", ")", "~", "`", ">", "#", "+", "-", "=", "|", "{", "}", ".", "!"
                        ).forEach {
                            res = res.replace(it, "\\$it")
                        }

                        res
                    }
                }
            }
        }
    }

    /** @author ChatGPT-3 */
    private fun fixed(entities: List<MessageEntity>): List<MessageEntity> {
        val sortedEntities = entities.sortedBy { it.offset }

        fun processEntities(entities: List<MessageEntity>): List<MessageEntity> {
            val newEntities = mutableListOf<MessageEntity>()
            val blacklist = mutableListOf<MessageEntity>()

            for (entity in entities) {
                val overlappingEntities = entities.filter {
                    it != entity && it.offset < entity.offset + entity.length && it.offset + it.length > entity.offset
                }

                if (overlappingEntities.isNotEmpty()) {
                    overlappingEntities.forEach { overlappingEntity ->
                        if (overlappingEntity.offset > entity.offset) {
                            newEntities.add(entity.copy(length = overlappingEntity.offset - entity.offset))
                        }

                        val start = maxOf(entity.offset, overlappingEntity.offset)
                        val end = minOf(entity.offset + entity.length, overlappingEntity.offset + overlappingEntity.length)
                        newEntities.add(entity.copy(offset = start, length = end - start))
                        newEntities.add(overlappingEntity.copy(offset = start, length = end - start))

                        if (entity.offset + entity.length > overlappingEntity.offset + overlappingEntity.length) {
                            newEntities.add(
                                entity.copy(
                                    offset = overlappingEntity.offset + overlappingEntity.length,
                                    length = entity.offset + entity.length - (overlappingEntity.offset + overlappingEntity.length)
                                )
                            )
                        }

                        blacklist.add(overlappingEntity)
                    }
                } else if (!blacklist.contains(entity)) {
                    newEntities.add(entity)
                }
            }

            return newEntities.distinct()
        }

        var currentEntities = sortedEntities
        var previousSize: Int

        do {
            previousSize = currentEntities.size
            currentEntities = processEntities(currentEntities)
        } while (currentEntities.size != previousSize)

        return currentEntities
    }

    /** Returns the message string that can be used with [parseMode] applied to send the message */
    fun format(parseMode: ParseMode): String {
        val textString = getTextString()
        if (entities.isEmpty()) return textString.escape(parseMode)

        val entities: List<MessageEntity> = fixed(getEntities()).sortedBy { it.offset }
        fun MessageEntity.isSameLengthAndOffset(other: MessageEntity): Boolean {
            return this.length == other.length && this.offset == other.offset
        }
//        if (parseMode == ParseMode.MARKDOWNV2 && config.verifyMarkdown2Format) {
//            entities.forEachIndexed { i, entity ->
//                if (i != 0 && i != entities.lastIndex && entity.offset + entity.length == entities[i + 1].offset &&
//                    (entity.type == MessageEntityType.ITALIC || entity.type == MessageEntityType.UNDERLINE) && (
//                        ((entities[i - 1].type == MessageEntityType.ITALIC || entities[i - 1].type == MessageEntityType.UNDERLINE) && !entities[i - 1].isSameLengthAndOffset(entity)) ||
//                        ((entities[i + 1].type == MessageEntityType.ITALIC || entities[i + 1].type == MessageEntityType.UNDERLINE) && !entities[i + 1].isSameLengthAndOffset(entity))
//                    )
//                ) {
//
//                    val prev = entities[(i - 2).coerceAtLeast(0)]
//                    val next = entities[(i + 2).coerceAtMost(entities.lastIndex)]
//                    val problematicText = textString.substring(prev.offset, prev.offset + entity.length + next.length)
//
//                    throw IllegalStateException(
//                        "MarkdownV2 not allowed to append italic and underline entities in text '$problematicText'. " +
//                        "Use the mix method for append both types, append another type " +
//                        "if you try to append two italic/underline in a row, or create an HTML string instead." +
//                        "To disable this verification set verifyMarkdown2Format = false in bot config"
//                    )
//                }
//            }
//        }

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
                        }.letIf(parseMode == ParseMode.MARKDOWNV2) {
                            val pattern = Regex("___(.*?)___")

                            pattern.replace(it) { matchResult ->
                                "___${matchResult.groups[1]?.value}_**__"
                            }

//                            it.replace("___(?!_)".toRegex(), "_**__")
                        }
                    )
                }

                // text without entity between two entities
                if (entities.size != i + 1 && !usedPartOfMix) {
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

    fun appendEntity(
        type: MessageEntityType, text: Any?, url: String? = null, language: String? = null, user: User? = null,
        customEmojiId: String? = null
    ): MessageText {
        if (!printNulls && text == null) return this
        text.toString().also { t ->
            entities.add(
                MessageEntity(
                    type, offset = this.text.length, t.length, url = url, language = language, user = user,
                    customEmojiId = customEmojiId
                )
            )
            this.text.append(t)
        }
        return this
    }

    fun entity(type: MessageEntityType, init: MessageEntityBuilder.() -> Unit): MessageEntity {
       return MessageEntityBuilder(type).apply { init() }.toEntity().also {
           entities.add(it)
       }
    }
}