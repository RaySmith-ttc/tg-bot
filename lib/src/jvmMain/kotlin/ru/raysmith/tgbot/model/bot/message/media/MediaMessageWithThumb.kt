package ru.raysmith.tgbot.model.bot.message.media

import ru.raysmith.tgbot.model.network.media.input.NotReusableInputFile

abstract class MediaMessageWithThumb : CaptionableMediaMessage() {
    var thumbnail: NotReusableInputFile? = null
}