package ru.raysmith.tgbot.model.bot.message.keyboard

import kotlinx.serialization.encodeToString
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardMarkup
import ru.raysmith.tgbot.model.network.keyboard.KeyboardMarkup
import ru.raysmith.tgbot.model.network.keyboard.ReplyKeyboardMarkup
import ru.raysmith.tgbot.model.network.keyboard.ReplyKeyboardRemove
import ru.raysmith.tgbot.network.TelegramApi2

@KeyboardDsl
interface MessageKeyboard {
    fun toMarkup(): KeyboardMarkup?
    fun toJson() = TelegramApi2.json.encodeToString(toMarkup())

    companion object {
        fun of(keyboardMarkup: KeyboardMarkup) = when(keyboardMarkup) {
            is InlineKeyboardMarkup -> {
                buildInlineKeyboard {
                    keyboardMarkup.keyboard.forEach { row ->
                        row {
                            row.forEach { button ->
                                button {
                                    this.callbackData = button.callbackData
                                    this.pay = button.pay
                                    this.loginUrl = button.loginUrl
                                    this.text = button.text
                                    this.url = button.url
                                    this.switchInlineQuery = button.switchInlineQuery
                                    this.webApp = button.webApp
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
                RemoveKeyboard().apply {
                    this.selective = keyboardMarkup.selective
                }
            }
        }
    }
}