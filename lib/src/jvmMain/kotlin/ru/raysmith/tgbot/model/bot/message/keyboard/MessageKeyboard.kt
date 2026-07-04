package ru.raysmith.tgbot.model.bot.message.keyboard

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ru.raysmith.tgbot.model.api.keyboard.*

internal object MessageKeyboardPolymorphicSerializer : JsonContentPolymorphicSerializer<MessageKeyboard>(MessageKeyboard::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<MessageKeyboard> {
        return when(element.jsonObject["class"]?.jsonPrimitive?.content) {
            "MessageInlineKeyboard" -> MessageInlineKeyboard.serializer()
            "MessageReplyKeyboard" -> MessageReplyKeyboard.serializer()
            "RemoveKeyboard" -> RemoveKeyboard.serializer()
            else -> error("Class discriminator not found for MessageKeyboard implementation class. Check the 'classDiscriminator' field has @EncodeDefault and @SerialName(\"class\") annotations")
        }
    }
}

@KeyboardDsl
@Serializable(MessageKeyboardPolymorphicSerializer::class)
sealed interface MessageKeyboard {
    val classDiscriminator: String // TODO ???
    fun toMarkup(): KeyboardMarkup?

    companion object {
        suspend fun of(keyboardMarkup: KeyboardMarkup) = when(keyboardMarkup) {
            is InlineKeyboardMarkup -> {
                buildInlineKeyboard {
                    keyboardMarkup.keyboard.forEach { row ->
                        row {
                            row.forEach { button ->
                                button {
                                    this.text = button.text
                                    this.iconCustomEmojiId = button.iconCustomEmojiId
                                    this.style = button.style
                                    this.url = button.url
                                    this.callbackData = button.callbackData
                                    this.webApp = button.webApp
                                    this.loginUrl = button.loginUrl
                                    this.switchInlineQuery = button.switchInlineQuery
                                    this.switchInlineQueryCurrentChat = button.switchInlineQueryCurrentChat
                                    this.switchInlineQueryChosenChat = button.switchInlineQueryChosenChat
                                    this.copyText = button.copyText
                                    this.pay = button.pay
                                }
                            }
                        }
                    }
                }
            }
            is ReplyKeyboardMarkup -> {
                buildReplyKeyboard {
                    this.isPersistent = keyboardMarkup.isPersistent
                    this.oneTimeKeyboard = keyboardMarkup.oneTimeKeyboard
                    this.resizeKeyboard = keyboardMarkup.resizeKeyboard
                    this.inputFieldPlaceholder = keyboardMarkup.inputFieldPlaceholder
                    this.selective = keyboardMarkup.selective
                    keyboardMarkup.keyboard.forEach { row ->
                        row {
                            row.forEach { button ->
                                button {
                                    this.text = button.text
                                    this.requestContact = button.requestContact
                                    this.requestLocation = button.requestLocation
                                    this.webApp = button.webApp
                                }
                            }
                        }
                    }
                }
            }
            is ReplyKeyboardRemove -> {
                buildReplyKeyboardRemove {
                    this.selective = keyboardMarkup.selective
                }
            }
            is ForceReply -> {
                buildForceReplyKeyboard {
                    this.inputFieldPlaceholder = keyboardMarkup.inputFieldPlaceholder
                    this.selective = keyboardMarkup.selective
                }
            }
        }
    }
}
