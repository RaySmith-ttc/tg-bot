package ru.raysmith.tgbot.model.bot.message.group

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.model.bot.message.MessageText
import ru.raysmith.tgbot.model.network.media.input.InputFile
import ru.raysmith.tgbot.model.network.media.input.InputMediaDocument
import ru.raysmith.tgbot.model.network.media.input.NotReusableInputFile
import ru.raysmith.tgbot.model.network.message.MessageEntity
import ru.raysmith.tgbot.model.network.message.ParseMode
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService

class DocumentMediaGroupMessage(override val service: TelegramService, override val fileService: TelegramFileService) : ru.raysmith.tgbot.model.bot.message.group.MediaGroupMessage(service, fileService) {
    fun document(
        document: InputFile, thumb: NotReusableInputFile? = null, disableContentTypeDetection: Boolean? = null,
        printNulls: Boolean = Bot.config.printNulls, safeTextLength: Boolean = Bot.config.safeTextLength,
        caption: MessageText.() -> Unit
    ) {
        inputMedia.add(
            InputMediaDocument(
                getMedia(document), thumb?.let { getMedia(it) }, getCaption(printNulls, caption), null,
                getCaptionEntities(printNulls, safeTextLength, caption), disableContentTypeDetection
            )
        )
    }

    fun document(
        document: InputFile, thumb: NotReusableInputFile? = null, caption: String? = null, parseMode: ParseMode? = null,
        captionEntities: List<MessageEntity>? = null, disableContentTypeDetection: Boolean? = null
    ) {
        inputMedia.add(
            InputMediaDocument(
                getMedia(document), thumb?.let { getMedia(it) }, getCaption(caption, false, parseMode), parseMode,
                captionEntities, disableContentTypeDetection
            )
        )
    }
}