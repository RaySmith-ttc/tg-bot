package ru.raysmith.tgbot.model.network.sticker

import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.group.MediaRequest
import ru.raysmith.tgbot.model.network.file.File
import ru.raysmith.tgbot.model.network.media.input.InputFile
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.errorBody

/** Builder of [uploadStickerFile][BotContext.uploadStickerFile] request */
internal class UploadStickerFile(

    /** User identifier of created sticker set owner */
    val userId: ChatId.ID,

    /**
     * A file with the sticker in .WEBP, .PNG, .TGS, or .WEBM format.
     * See [https://core.telegram.org/stickers](https://core.telegram.org/stickers) for technical requirements.
     * */
    val sticker: InputFile,

    /** Format of the sticker */
    val stickerFormat: StickerFormat

) : MediaRequest() {
    fun upload(service: TelegramService): File {
        val stickerParam = getMedia(sticker, "sticker")

        return if (multipartBodyParts.isNotEmpty()) {
            service.uploadStickerFile(userId, multipartBodyParts.first(), stickerFormat)
        } else {
            service.uploadStickerFile(userId, stickerParam, stickerFormat)
        }.execute().body()?.result ?: errorBody()
    }
}