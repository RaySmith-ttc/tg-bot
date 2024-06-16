package ru.raysmith.tgbot.model.network.sticker

import io.ktor.client.*
import io.ktor.client.request.*
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.group.MediaRequest
import ru.raysmith.tgbot.model.network.media.input.InputFile
import ru.raysmith.tgbot.network.TelegramApi

/** Builder of [createNewStickerSet][TelegramApi.createNewStickerSet] request */
open class CreateNewStickerInStickerSet(

    /** User identifier of created sticker set owner */
    val userId: ChatId.ID,

    /**
     * Short name of sticker set, to be used in `t.me/addstickers/` URLs (e.g., animals).
     * Can contain only English letters, digits and underscores. Must begin with a letter,
     * can't contain consecutive underscores and must end in `"_by_<bot_username>"`.
     * `<bot_username>` is case insensitive. 1-64 characters.
     * */
    val name: String,

    /** Sticker set title, 1-64 characters */
    val title: String,

) : MediaRequest() {

    /** Type of stickers in the set. By default, a regular sticker set is created. */
    var stickerType: StickerType? = null

    /** Position where the mask should be placed on faces */
    var maskPosition: MaskPosition? = null

    /**
     * *True*, if the sticker must be repainted to a text color in messages, the color of the Telegram Premium badge
     * in emoji status, white color on chat photos, or another appropriate color in other places
     * */
    var needsRepainting: Boolean? = null

    internal val stickers = mutableListOf<InputSticker>()

    fun sticker(sticker: InputFile, format: StickerFormat, emojiList: List<String>, maskPosition: MaskPosition? = null, keywords: List<String>? = null) {
        stickers.add(InputSticker(sticker, format, emojiList, maskPosition, keywords))
    }

    context(BotContext<*>)
    internal suspend fun create(): Boolean {
        return createNewStickerSet(
            userId, name, title, stickers, stickerType, needsRepainting
        )
    }
}