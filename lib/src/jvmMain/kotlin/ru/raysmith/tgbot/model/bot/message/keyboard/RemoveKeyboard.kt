package ru.raysmith.tgbot.model.bot.message.keyboard

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.network.keyboard.KeyboardMarkup
import ru.raysmith.tgbot.model.network.keyboard.ReplyKeyboardRemove

@Serializable
class RemoveKeyboard : MessageKeyboard {
    @EncodeDefault
    @SerialName("class")
    override val classDiscriminator = "RemoveKeyboard"
    var selective: Boolean? = null

    override fun toMarkup(): KeyboardMarkup {
        return ReplyKeyboardRemove(selective)
    }

}