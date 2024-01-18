package ru.raysmith.tgbot.model.bot.message.media

import ru.raysmith.tgbot.model.bot.message.IMessage
import ru.raysmith.tgbot.model.bot.message.keyboard.KeyboardCreator
import ru.raysmith.tgbot.model.bot.message.keyboard.MessageKeyboard
import ru.raysmith.tgbot.model.network.media.input.InputFile
import ru.raysmith.tgbot.model.network.message.Message

// TODO add auto ChatAction + global config
abstract class MediaMessage : IMessage<Message>, KeyboardCreator {

    protected var media: InputFile? = null
    protected abstract val mediaName: String

    override var messageThreadId: Int? = null
    override var disableNotification: Boolean? = null
    override var replyToMessageId: Int? = null
    override var allowSendingWithoutReply: Boolean? = null
    override var keyboardMarkup: MessageKeyboard? = null
    override var protectContent: Boolean? = null
}