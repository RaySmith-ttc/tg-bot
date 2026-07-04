package ru.raysmith.tgbot.model.bot.message.keyboard

import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.keyboard.ForceReply
import ru.raysmith.tgbot.model.api.keyboard.KeyboardMarkup

@Serializable
class ForceReplyKeyboard : MessageKeyboard {
    override val classDiscriminator: String = "ForceReplyKeyboard"

    var inputFieldPlaceholder: String? = null
    var selective: Boolean? = null

    override fun toMarkup(): KeyboardMarkup {
        return ForceReply(
            forceReply = true, inputFieldPlaceholder, selective
        )
    }
}
