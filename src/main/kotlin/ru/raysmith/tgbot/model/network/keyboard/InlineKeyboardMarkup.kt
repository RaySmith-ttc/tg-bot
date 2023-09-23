package ru.raysmith.tgbot.model.network.keyboard

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** This object represents an inline keyboard that appears right next to the message it belongs to. */
@Serializable
data class InlineKeyboardMarkup(

    /** Array of button rows, each represented by an Array of [InlineKeyboardButton] objects */
    @SerialName("inline_keyboard") @Required val keyboard: List<List<InlineKeyboardButton>>
) : KeyboardMarkup()