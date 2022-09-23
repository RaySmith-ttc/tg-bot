package ru.raysmith.tgbot.model.bot.message.group

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.model.bot.message.MessageText
import ru.raysmith.tgbot.model.network.media.input.InputFile
import ru.raysmith.tgbot.model.network.media.input.InputMediaVideo
import ru.raysmith.tgbot.model.network.media.input.NotReusableInputFile
import ru.raysmith.tgbot.model.network.message.MessageEntity
import ru.raysmith.tgbot.model.network.message.ParseMode
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService

class VideoMediaGroupMessage(override val service: TelegramService, override val fileService: TelegramFileService) : MediaGroupMessage(service, fileService) {
    fun video(
        video: InputFile, thumb: NotReusableInputFile? = null, caption: String? = null, parseMode: ParseMode? = null,
        captionEntities: List<MessageEntity>? = null, width: Int? = null, height: Int? = null, duration: Int? = null,
        supportsStreaming: Boolean? = null
    ) {
        inputMedia.add(
            InputMediaVideo(
                getMedia(video), thumb?.let { getMedia(it) }, getCaption(caption, false, parseMode),
                parseMode, captionEntities, width, height, duration, supportsStreaming
            )
        )
    }

    fun video(
        video: InputFile, thumb: NotReusableInputFile? = null, width: Int? = null, height: Int? = null, duration: Int? = null,
        supportsStreaming: Boolean? = null, printNulls: Boolean = Bot.Config.printNulls,
        safeTextLength: Boolean = Bot.Config.safeTextLength, caption: MessageText.() -> Unit
    ) {
        inputMedia.add(
            InputMediaVideo(
                getMedia(video), thumb?.let { getMedia(it) }, getCaption(printNulls, caption), null,
                getCaptionEntities(printNulls, safeTextLength, caption), width, height, duration, supportsStreaming
            )
        )
    }
}

