package ru.raysmith.tgbot.model.bot.message.media

import ru.raysmith.tgbot.model.bot.message.IMessage
import ru.raysmith.tgbot.model.bot.message.keyboard.KeyboardCreator
import ru.raysmith.tgbot.model.bot.message.keyboard.MessageKeyboard
import ru.raysmith.tgbot.model.api.media.input.InputFile
import ru.raysmith.tgbot.model.api.message.Message
import ru.raysmith.tgbot.model.api.message.ReplyParameters
import ru.raysmith.tgbot.model.api.suggestedpost.SuggestedPostParameters

// TODO should be SuggestablePostMessage?
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
    override var directMessagesTopicId: Int? = null

    /** Pass *True*, if the caption must be shown above the message media */
    var showCaptionAboveMedia: Boolean? = null // TODO ?
}
