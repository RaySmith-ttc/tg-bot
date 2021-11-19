package ru.raysmith.tgbot.model.network.keyboard

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.encodeToJsonElement
import ru.raysmith.tgbot.network.TelegramApi

@Serializable
/** This object represents an inline keyboard that appears right next to the message it belongs to. */
data class InlineKeyboardMarkup(

    /** Array of button rows, each represented by an Array of [InlineKeyboardButton] objects */
    @SerialName("inline_keyboard") @Required val keyboard: List<List<InlineKeyboardButton>>
) : KeyboardMarkup() {
    override fun toString(): String {
        return TelegramApi.json.encodeToString(this)
    }
}