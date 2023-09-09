package ru.raysmith.tgbot.model.network.sticker

import kotlinx.serialization.encodeToString
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.group.MediaRequest
import ru.raysmith.tgbot.model.network.media.input.InputFile
import ru.raysmith.tgbot.network.TelegramApi.json
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.errorBody

/** Builder of [createNewStickerSet][BotContext.createNewStickerSet] request */
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

    /** Format of stickers in the set */
    val stickerFormat: StickerFormat
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

    private val stickers = mutableListOf<InputSticker>()
    fun sticker(sticker: InputFile, emojiList: List<String>, maskPosition: MaskPosition? = null, keywords: List<String>? = null) {
        stickers.add(InputSticker(sticker, emojiList, maskPosition, keywords))
    }

    internal fun create(service: TelegramService): Boolean {
        return service.createNewStickerSet(
            userId, name, title, json.encodeToString(stickers.map { sticker -> sticker.toSerializable { getMedia(it) } }),
            stickerFormat, stickerType, needsRepainting,
            multipartBodyParts.getOrNull(0),
            multipartBodyParts.getOrNull(1),
            multipartBodyParts.getOrNull(2),
            multipartBodyParts.getOrNull(3),
            multipartBodyParts.getOrNull(4),
            multipartBodyParts.getOrNull(5),
            multipartBodyParts.getOrNull(6),
            multipartBodyParts.getOrNull(7),
            multipartBodyParts.getOrNull(8),
            multipartBodyParts.getOrNull(9),
            multipartBodyParts.getOrNull(10),
            multipartBodyParts.getOrNull(11),
            multipartBodyParts.getOrNull(12),
            multipartBodyParts.getOrNull(13),
            multipartBodyParts.getOrNull(14),
            multipartBodyParts.getOrNull(15),
            multipartBodyParts.getOrNull(16),
            multipartBodyParts.getOrNull(17),
            multipartBodyParts.getOrNull(18),
            multipartBodyParts.getOrNull(19),
            multipartBodyParts.getOrNull(20),
            multipartBodyParts.getOrNull(21),
            multipartBodyParts.getOrNull(22),
            multipartBodyParts.getOrNull(23),
            multipartBodyParts.getOrNull(24),
            multipartBodyParts.getOrNull(25),
            multipartBodyParts.getOrNull(26),
            multipartBodyParts.getOrNull(27),
            multipartBodyParts.getOrNull(28),
            multipartBodyParts.getOrNull(29),
            multipartBodyParts.getOrNull(30),
            multipartBodyParts.getOrNull(31),
            multipartBodyParts.getOrNull(32),
            multipartBodyParts.getOrNull(33),
            multipartBodyParts.getOrNull(34),
            multipartBodyParts.getOrNull(35),
            multipartBodyParts.getOrNull(36),
            multipartBodyParts.getOrNull(37),
            multipartBodyParts.getOrNull(38),
            multipartBodyParts.getOrNull(39),
            multipartBodyParts.getOrNull(40),
            multipartBodyParts.getOrNull(41),
            multipartBodyParts.getOrNull(42),
            multipartBodyParts.getOrNull(43),
            multipartBodyParts.getOrNull(44),
            multipartBodyParts.getOrNull(45),
            multipartBodyParts.getOrNull(46),
            multipartBodyParts.getOrNull(47),
            multipartBodyParts.getOrNull(48),
            multipartBodyParts.getOrNull(49)
        ).execute().body()?.result ?: errorBody()
    }
}