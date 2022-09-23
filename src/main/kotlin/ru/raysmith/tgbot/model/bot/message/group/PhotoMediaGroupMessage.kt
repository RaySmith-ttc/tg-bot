package ru.raysmith.tgbot.model.bot.message.group

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.model.bot.message.MessageText
import ru.raysmith.tgbot.model.network.media.input.InputFile
import ru.raysmith.tgbot.model.network.media.input.InputMediaPhoto
import ru.raysmith.tgbot.model.network.message.MessageEntity
import ru.raysmith.tgbot.model.network.message.ParseMode
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService

class PhotoMediaGroupMessage(override val service: TelegramService, override val fileService: TelegramFileService) : ru.raysmith.tgbot.model.bot.message.group.MediaGroupMessage(service, fileService) {
    // TODO docs: not correctly work with the safeLength property when parseMode is not null. Provide hand-made safe caption
    fun photo(
        photo: InputFile, caption: String? = null, parseMode: ParseMode? = null,
        safeTextLength: Boolean = Bot.Config.safeTextLength, captionEntities: List<MessageEntity>? = null
    ) {
        inputMedia.add(
            InputMediaPhoto(
                getMedia(photo), getCaption(caption, safeTextLength, parseMode), parseMode, captionEntities
            )
        )
    }
    fun photo(
        photo: InputFile, printNulls: Boolean = Bot.Config.printNulls,
        safeTextLength: Boolean = Bot.Config.safeTextLength, caption: MessageText.() -> Unit
    ) {
        inputMedia.add(
            InputMediaPhoto(
                getMedia(photo),
                getCaption(printNulls, caption),
                null,
                getCaptionEntities(printNulls, safeTextLength, caption)
            )
        )
    }
}