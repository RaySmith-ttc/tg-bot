package ru.raysmith.tgbot.model.bot.message.media

import ru.raysmith.tgbot.model.bot.message.IMessage
import ru.raysmith.tgbot.model.bot.message.keyboard.KeyboardCreator
import ru.raysmith.tgbot.model.bot.message.keyboard.MessageKeyboard
import ru.raysmith.tgbot.model.network.media.input.InputFile
import ru.raysmith.tgbot.model.network.message.Message
import ru.raysmith.tgbot.model.network.message.ReplyParameters

abstract class MediaMessage : IMessage<Message>, KeyboardCreator {
    protected abstract var sendChatAction: Boolean
    protected var media: InputFile? = null
    protected abstract val mediaName: String

    override var messageThreadId: Int? = null
    override var disableNotification: Boolean? = null
    override var replyParameters: ReplyParameters? = null
    override var keyboardMarkup: MessageKeyboard? = null
    override var protectContent: Boolean? = null
    override var businessConnectionId: String? = null
}