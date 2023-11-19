package ru.raysmith.tgbot.model.network.sticker

import kotlinx.serialization.encodeToString
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.group.MediaRequest
import ru.raysmith.tgbot.network.TelegramApi2
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.errorBody

/** Builder of [addStickerSet][BotContext.addStickerToSet] request */
open class AddStickerInStickerSet(

    /** User identifier of created sticker set owner */
    val userId: ChatId.ID,

    /**
     * Short name of sticker set, to be used in `t.me/addstickers/` URLs (e.g., animals).
     * Can contain only English letters, digits and underscores. Must begin with a letter,
     * can't contain consecutive underscores and must end in `"_by_<bot_username>"`.
     * `<bot_username>` is case insensitive. 1-64 characters.
     * */
    val name: String,

    /**
     * Information about the added sticker.
     * If exactly the same sticker had already been added to the set, then the set isn't changed.
     * */
    val sticker: InputSticker
) : MediaRequest() {

    internal fun add(service: TelegramService): Boolean {
        val stickerJson = TelegramApi2.json.encodeToString(sticker.toSerializable { getMedia(it, "sticker") })

        return if (multipartBodyParts.isNotEmpty()) {
            service.addStickerToSet(userId, name, stickerJson, multipartBodyParts.getOrNull(0))
        } else {
            service.addStickerToSet(userId, name, stickerJson)
        }.execute().body()?.result ?: errorBody()
    }
}