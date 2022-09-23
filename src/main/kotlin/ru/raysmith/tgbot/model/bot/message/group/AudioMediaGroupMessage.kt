package ru.raysmith.tgbot.model.bot.message.group

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.model.bot.message.MessageText
import ru.raysmith.tgbot.model.network.media.input.InputFile
import ru.raysmith.tgbot.model.network.media.input.InputMediaAudio
import ru.raysmith.tgbot.model.network.media.input.NotReusableInputFile
import ru.raysmith.tgbot.model.network.message.MessageEntity
import ru.raysmith.tgbot.model.network.message.ParseMode
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService

class AudioMediaGroupMessage(override val service: TelegramService, override val fileService: TelegramFileService) : MediaGroupMessage(service, fileService) {
    fun audio(
        audio: InputFile, thumb: NotReusableInputFile? = null, caption: String? = null, parseMode: ParseMode? = null,
        captionEntities: List<MessageEntity>? = null, duration: Int? = null, performer: String? = null,
        title: String? = null
    ) {
        inputMedia.add(
            InputMediaAudio(
                getMedia(audio), thumb?.let { getMedia(it) }, getCaption(caption, false, parseMode),
                parseMode, captionEntities, duration, performer, title
            )
        )
    }

    fun audio(
        audio: InputFile, thumb: NotReusableInputFile? = null, duration: Int? = null, performer: String? = null,
        title: String? = null, printNulls: Boolean = Bot.Config.printNulls,
        safeTextLength: Boolean = Bot.Config.safeTextLength, caption: MessageText.() -> Unit
    ) {
        inputMedia.add(
            InputMediaAudio(
                getMedia(audio), thumb?.let { getMedia(it) }, getCaption(printNulls, caption), null,
                getCaptionEntities(printNulls, safeTextLength, caption), duration, performer, title
            )
        )
    }
}

