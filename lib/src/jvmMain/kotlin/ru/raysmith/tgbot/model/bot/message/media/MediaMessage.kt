package ru.raysmith.tgbot.model.bot.message.media

import ru.raysmith.tgbot.model.bot.message.IMessage
import ru.raysmith.tgbot.model.bot.message.keyboard.KeyboardCreator
import ru.raysmith.tgbot.model.bot.message.keyboard.MessageKeyboard
import ru.raysmith.tgbot.model.network.media.input.InputFile
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.model.network.message.ReplyParameters

// TODO docs
abstract class MediaMessage : IMessage<Message>, KeyboardCreator {
    protected abstract var sendChatAction: Boolean
    protected var media: InputFile? = null
    protected abstract val mediaName: String

    override var disableNotification: Boolean? = null
    override var replyParameters: ReplyParameters? = null
    override var keyboardMarkup: MessageKeyboard? = null
    override var protectContent: Boolean? = null
    override var allowPaidBroadcast: Boolean? = null

    /** Pass *True*, if the caption must be shown above the message media */
    var showCaptionAboveMedia: Boolean? = null // TODO ?
}