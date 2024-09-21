package ru.raysmith.tgbot.model.network.sticker

import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.group.MediaRequest
import ru.raysmith.tgbot.model.network.media.input.InputFile

/** Builder of [uploadStickerFile][BotContext.uploadStickerFile] request */
data class UploadStickerFile(

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

    context(BotContext<*>)
    suspend fun upload() = uploadStickerFile(userId, sticker, stickerFormat)
}