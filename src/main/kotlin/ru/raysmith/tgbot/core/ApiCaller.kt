package ru.raysmith.tgbot.core

import kotlinx.serialization.encodeToString
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.User
import ru.raysmith.tgbot.model.network.UserProfilePhotos
import ru.raysmith.tgbot.model.network.chat.ChatAdministratorRights
import ru.raysmith.tgbot.model.network.command.BotCommand
import ru.raysmith.tgbot.model.network.command.BotCommandScope
import ru.raysmith.tgbot.model.network.command.BotCommandScopeDefault
import ru.raysmith.tgbot.model.network.file.File
import ru.raysmith.tgbot.model.network.media.input.InputFile
import ru.raysmith.tgbot.model.network.media.input.toRequestBody
import ru.raysmith.tgbot.model.network.sticker.AddStickerInStickerSet
import ru.raysmith.tgbot.model.network.sticker.NewStickerSet
import ru.raysmith.tgbot.model.network.sticker.Sticker
import ru.raysmith.tgbot.model.network.sticker.StickerSet
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.errorBody
import java.io.InputStream

interface ApiCaller {
    val service: TelegramService
    val fileService: TelegramFileService

    /**
     * A simple method for testing your bot's auth token. Requires no parameters.
     *
     * Returns basic information about the bot in form of a [User] object.
     * */
    fun getMe() = service.getMe().execute().body()?.result ?: errorBody()

    /**
     * Use this method to log out from the cloud Bot API server before launching the bot locally.
     * You **must** log out the bot before running it locally, otherwise there is no guarantee that the bot will
     * receive updates. After a successful call, you can immediately log in on a local server, but will not be able
     * to log in back to the cloud Bot API server for 10 minutes. Returns *True* on success. Requires no parameters.
     * */
    fun logOut() = service.logOut().execute().body()?.result ?: errorBody()

    /**
     * Use this method to close the bot instance before moving it from one local server to another.
     * You need to delete the webhook before calling this method to ensure that the bot isn't launched again after
     * server restart. The method will return error 429 in the first 10 minutes after the bot is launched.
     * Returns *True* on success. Requires no parameters.
     * */
    fun close() = service.close().execute().body()?.result ?: errorBody()

    /**
     * Use this method to get a list of profile pictures for a user. Returns a [UserProfilePhotos] object.
     * @param userId Unique identifier of the target user
     * @param offset Sequential number of the first photo to be returned. By default, all photos are returned.
     * @param limit Limits the number of photos to be retrieved. Values between 1-100 are accepted. Defaults to 100.
     * */
    fun getUserProfilePhotos(userId: ChatId.ID, offset: Int? = null, limit: Int? = null): UserProfilePhotos {
        return service.getUserProfilePhotos(userId, offset, limit).execute().body()?.result ?: errorBody()
    }

    /**
     * Use this method to get basic information about a file and prepare it for downloading.
     * For the moment, bots can download files of up to 20MB in size. On success, a [File] object is returned.
     * The file can then be downloaded via the link `https://api.telegram.org/file/bot<token>/<file_path>`,
     * where `<file_path>` is taken from the response. It is guaranteed that the link will be valid for at least 1 hour.
     * When the link expires, a new one can be requested by calling [getFile] again.
     *
     * **Note:** This function may not preserve the original file name and MIME type.
     * You should save the file's MIME type and name (if available) when the File object is received.
     *
     * @param id File identifier to get information about
     * */
    fun getFile(id: String) = service.getFile(id).execute().body()?.file ?: errorBody()

    /**
     * Use this method to change the list of the bot's commands.
     *
     * @see <a href="https://core.telegram.org/bots#commands">commands</a> for more details about bot commands.
     * Returns True on success
     *
     * @param commands list of bot commands to be set as the list of the bot's commands. At most 100 commands can be specified.
     * @param scope [scope][BotCommandScope] of users for which the commands are relevant. Defaults to [BotCommandScopeDefault].
     * @param languageCode A two-letter ISO 639-1 language code. If empty, commands will be applied to all
     * users fromthe given scope, for whose language there are no dedicated commands
     * */
    fun setMyCommands(commands: List<BotCommand>, scope: BotCommandScope? = null, languageCode: String? = null): Boolean {
        return service.setMyCommands(TelegramApi.json.encodeToString(commands), scope, languageCode)
            .execute().body()?.result ?: errorBody()
    }

    /**
     * Use this method to delete the list of the bot's commands for the given scope and user language.
     * After deletion, [higher level commands][BotCommandScope] will be shown to affected users. Returns True on success.
     *
     * @param scope [scope][BotCommandScope] of users for which the commands are relevant. Defaults to [BotCommandScopeDefault].
     * @param languageCode A two-letter ISO 639-1 language code. If empty, commands will be applied to all users from the given scope, for whose language there are no dedicated commands
     * */
    fun deleteMyCommands(scope: BotCommandScope? = null, languageCode: String? = null): Boolean {
        return service.deleteMyCommands(scope, languageCode).execute().body()?.result ?: errorBody()
    }

    /**
     * Use this method to get the current list of the bot's commands for the given scope and user language.
     * Returns Array of [BotCommand] on success. If commands aren't set, an empty list is returned.
     *
     * @param scope [scope][BotCommandScope] of users for which the commands are relevant. Defaults to [BotCommandScopeDefault].
     * @param languageCode A two-letter ISO 639-1 language code. If empty, commands will be applied to all users from the given scope, for whose language there are no dedicated commands
     * */
    fun getMyCommands(scope: BotCommandScope? = null, languageCode: String? = null): List<BotCommand> {
        return service.getMyCommands(scope, languageCode).execute().body()?.result ?: errorBody()
    }

    /**
     * Use this method to change the default administrator rights requested by the bot when it's added as an
     * administrator to groups or channels. These rights will be suggested to users, but they are are free to
     * modify the list before adding the bot. Returns True on success.
     *
     * @param rights [ChatAdministratorRights] object describing new default administrator rights.
     * If not specified, the default administrator rights will be cleared.
     * @param forChannels Pass True to change the default administrator rights of the bot in channels.
     * Otherwise, the default administrator rights of the bot for groups and supergroups will be changed.
     * */
    fun setMyDefaultAdministratorRights(rights: ChatAdministratorRights? = null, forChannels: Boolean? = null): Boolean {
        return service.setMyDefaultAdministratorRights(rights, forChannels).execute().body()?.result ?: errorBody()
    }

    /**
     * Use this method to get the current default administrator rights of the bot.
     * Returns ChatAdministratorRights on success.
     *
     * @param forChannels Pass True to change the default administrator rights of the bot in channels.
     * Otherwise, the default administrator rights of the bot for groups and supergroups will be returned.
     * */
    fun getMyDefaultAdministratorRights(forChannels: Boolean? = null): ChatAdministratorRights {
        return service.getMyDefaultAdministratorRights(forChannels).execute().body()?.result ?: errorBody()
    }



    /**
     * Use this method to send answers to callback queries sent from (inline keyboards)(https://core.telegram.org/bots/features#inline-keyboards). The answer will be displayed
     * to the user as a notification at the top of the chat screen or as an alert. On success, _True_ is returned.
     * */
    fun answerCallbackQuery(
        callbackQueryId: String,
        text: String? = null,
        showAlert: Boolean? = null,
        url: String? = null,
        cacheTime: Int? = null
    ): Boolean {
        return service.answerCallbackQuery(callbackQueryId, text, showAlert, url, cacheTime).execute().body()?.result ?: errorBody()
    }

    // TODO
//    fun setMyCommands(
//        commands: Iterable<BotCommand>, scope: BotCommandScope?, languageCode:
//    )

    /**
     * Use this method to get a sticker set. On success, a [StickerSet] object is returned.
     *
     * @param name Name of the sticker set
     * */
    fun getStickerSet(name: String): StickerSet {
        return service.getStickerSet(name).execute().body()?.result ?: errorBody()
    }

    /**
     * Use this method to get information about custom emoji stickers by their identifiers.
     * Returns an Array of [Sticker] objects.
     *
     * @param customEmojiIds List of custom emoji identifiers. At most 200 custom emoji identifiers can be specified.
     * */
    fun getCustomEmojiStickers(customEmojiIds: List<String>): List<Sticker> {
        return service.getCustomEmojiStickers(customEmojiIds).execute().body()?.result ?: errorBody()
    }

    /**
     * Use this method to upload a .PNG file with a sticker for later use in *[createNewStickerSet]* and
     * *[addStickerToSet]* methods (can be used multiple times). Returns the uploaded File on success.
     *
     * @param userId User identifier of sticker file owner
     * @param pngSticker **PNG** image with the sticker, must be up to 512 kilobytes in size,
     * dimensions must not exceed 512px, and either width or height must be exactly 512px.
     * */
    fun uploadStickerFile(userId: ChatId.ID, pngSticker: InputFile): File {
        return service.uploadStickerFile(userId, pngSticker.toRequestBody("png_sticker")).execute().body()?.result ?: errorBody()
    }

    /**
     * Use this method to create a new sticker set owned by a user.
     * The bot will be able to edit the sticker set thus created. You **must** use exactly one of the
     * fields *[pngSticker][NewStickerSet.pngSticker]*, *[tgsSticker][NewStickerSet.tgsSticker]*,
     * or *[webmSticker][NewStickerSet.webmSticker]*. Returns *True* on success.
     *
     * @param userId User identifier of created sticker set owner
     * @param name Short name of sticker set, to be used in `t.me/addstickers/` URLs (e.g., animals).
     * Can contain only English letters, digits and underscores. Must begin with a letter,
     * can't contain consecutive underscores and must end in `"_by_<bot_username>"`.
     * `<bot_username>` is case insensitive. 1-64 characters.
     * You can use [stickerSetName][ru.raysmith.tgbot.utils.stickerSetName] method to automatically create name for
     * bot in context
     * @param title Sticker set title, 1-64 characters
     * @param block Sticker set builder
     * */
    fun createNewStickerSet(userId: ChatId.ID, name: String, title: String, emojis: String, block: NewStickerSet.() -> Unit): Boolean {
        return NewStickerSet(userId, name, title, emojis).apply(block).create(service)
    }

    /**
     * Use this method to add a new sticker to a set created by the bot.
     * You must use exactly one of the fields png_sticker, tgs_sticker, or webm_sticker.
     * Animated stickers can be added to animated sticker sets and only to them.
     * Animated sticker sets can have up to 50 stickers. Static sticker sets can have up to 120 stickers.
     * Returns True on success.
     *
     * @param userId User identifier of created sticker set owner
     * @param name Short name of sticker set, to be used in `t.me/addstickers/` URLs (e.g., animals).
     * Can contain only English letters, digits and underscores. Must begin with a letter,
     * can't contain consecutive underscores and must end in `"_by_<bot_username>"`.
     * `<bot_username>` is case insensitive. 1-64 characters.
     * You can use [stickerSetName][ru.raysmith.tgbot.utils.stickerSetName] method to automatically create name for
     * bot in context
     * @param block Add sticker set builder
     * */
    fun addStickerToSet(userId: ChatId.ID, name: String, emojis: String, block: AddStickerInStickerSet.() -> Unit): Boolean {
        return AddStickerInStickerSet(userId, name, emojis).apply(block).add(service)
    }

    /**
     * Use this method to move a sticker in a set created by the bot to a specific position. Returns *True* on success.
     *
     * @param sticker File identifier of the sticker
     * @param position New sticker position in the set, zero-based
     * */
    fun setStickerPositionInSet(sticker: String, position: Int): Boolean {
        return service.setStickerPositionInSet(sticker, position).execute().body()?.result ?: errorBody()
    }

    /**
     * Use this method to delete a sticker from a set created by the bot. Returns *True* on success.
     *
     * @param sticker File identifier of the sticker
     * */
    fun deleteStickerFromSet(sticker: String): Boolean {
        return service.deleteStickerFromSet(sticker).execute().body()?.result ?: errorBody()
    }

    fun downloadFile(fileId: String): InputStream {
        val fileResponse = service.getFile(fileId).execute().body() ?: errorBody()
        return downloadFile(fileResponse.file)
    }

    fun downloadFile(file: File): InputStream {
        return fileService.downLoad(file.path!!).execute().let {
            (it.body() ?: errorBody()).byteStream()
        }
    }
}