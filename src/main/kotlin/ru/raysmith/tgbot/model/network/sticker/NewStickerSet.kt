package ru.raysmith.tgbot.model.network.sticker

import okhttp3.RequestBody.Companion.toRequestBody
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.media.input.InputFile
import ru.raysmith.tgbot.model.network.media.input.toRequestBody
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.errorBody

/** Builder of adding a sticker to a sticker set */
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

    /** One or more emoji corresponding to the sticker */
    var emojis: String
) {

    /**
     * PNG image with the sticker, must be up to 512 kilobytes in size, dimensions must not exceed 512px,
     * and either width or height must be exactly 512px.
     * */
    var pngSticker: InputFile? = null
        private set

    /** **TGS** animation with the sticker, uploaded using multipart/form-data. */
    var tgsSticker: InputFile? = null
        private set

    /** **WEBM** video with the sticker, uploaded using multipart/form-data. */
    var webmSticker: InputFile? = null
        private set

    /**
     * PNG image with the sticker, must be up to 512 kilobytes in size, dimensions must not exceed 512px,
     * and either width or height must be exactly 512px.
     * */
    fun pngSticker(pngSticker: InputFile) {
        this.pngSticker = pngSticker
        this.tgsSticker = null
        this.webmSticker = null
    }

    /** **TGS** animation with the sticker, uploaded using multipart/form-data. */
    fun tgsSticker(tgsSticker: InputFile) {
        this.pngSticker = null
        this.tgsSticker = tgsSticker
        this.webmSticker = null
    }

    /** **WEBM** video with the sticker, uploaded using multipart/form-data. */
    fun webmSticker(webmSticker: InputFile) {
        this.pngSticker = null
        this.tgsSticker = null
        this.webmSticker = webmSticker
    }

    /**
     * Type of stickers in the set, set [StickerType.REGULAR] or [StickerType.MASK].
     * Custom emoji sticker sets can't be created via the Bot API at the moment.
     * By default, a regular sticker set is created.
     * */
    var stickerType: StickerType? = null

    /** Position where the mask should be placed on faces */
    var maskPosition: MaskPosition? = null

    internal fun add(service: TelegramService): Boolean {
        return if (pngSticker != null && pngSticker is InputFile.FileIdOrUrl) {
            service.addStickerToSet(
                userId, name, (pngSticker as InputFile.FileIdOrUrl).value, stickerType, emojis, maskPosition
            ).execute().body()?.result ?: errorBody()
        } else {
            service.addStickerToSet(
                userId, name.toRequestBody(), pngSticker?.toRequestBody("png_sticker"),
                tgsSticker?.toRequestBody("tgs_sticker"), webmSticker?.toRequestBody("webm_sticker"), stickerType,
                emojis.toRequestBody()
            ).execute().body()?.result ?: errorBody()
        }
    }
}

/** Builder of new sticker set */
class NewStickerSet(
    userId: ChatId.ID,
    name: String,
    emojis: String,

    /** Sticker set title, 1-64 characters */
    val title: String
) : AddStickerInStickerSet(userId, name, emojis) {

    internal fun create(service: TelegramService): Boolean {
        return if (pngSticker != null && pngSticker is InputFile.FileIdOrUrl) {
            service.createNewStickerSet(
                userId, name, title, (pngSticker as InputFile.FileIdOrUrl).value, stickerType, emojis, maskPosition
            ).execute().body()?.result ?: errorBody()
        } else {
            service.createNewStickerSet(
                userId, name.toRequestBody(), title.toRequestBody(), pngSticker?.toRequestBody("png_sticker"),
                tgsSticker?.toRequestBody("tgs_sticker"), webmSticker?.toRequestBody("webm_sticker"), stickerType,
                emojis.toRequestBody()
            ).execute().body()?.result ?: errorBody()
        }
    }
}