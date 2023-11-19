package ru.raysmith.tgbot.model.network.sticker

import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.bot.message.group.MediaRequest

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

    context(BotContext<*>)
    internal suspend fun add() = addStickerToSet(userId, name, sticker)
}