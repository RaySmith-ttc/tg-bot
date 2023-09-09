package ru.raysmith.tgbot.core

import kotlinx.serialization.encodeToString
import okhttp3.RequestBody.Companion.toRequestBody
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.User
import ru.raysmith.tgbot.model.network.UserProfilePhotos
import ru.raysmith.tgbot.model.network.WebhookInfo
import ru.raysmith.tgbot.model.network.chat.ChatAdministratorRights
import ru.raysmith.tgbot.model.network.command.BotCommand
import ru.raysmith.tgbot.model.network.command.BotCommandScope
import ru.raysmith.tgbot.model.network.command.BotCommandScopeDefault
import ru.raysmith.tgbot.model.network.file.File
import ru.raysmith.tgbot.model.network.inline.SentWebAppMessage
import ru.raysmith.tgbot.model.network.inline.result.InlineQueryResult
import ru.raysmith.tgbot.model.network.inline.result.InlineQueryResultsButton
import ru.raysmith.tgbot.model.network.media.input.InputFile
import ru.raysmith.tgbot.model.network.media.input.toRequestBody
import ru.raysmith.tgbot.model.network.sticker.*
import ru.raysmith.tgbot.model.network.updates.Update
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.network.TelegramApi.json
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.errorBody
import java.io.InputStream
import kotlin.time.Duration

interface ApiCaller {
    val service: TelegramService
    val fileService: TelegramFileService

    /**
     * Use this method to specify a URL and receive incoming updates via an outgoing webhook.
     * Whenever there is an update for the bot, we will send an HTTPS POST request to the specified URL,
     * containing a JSON-serialized [Update]. In case of an unsuccessful request, we will give up after a
     * reasonable amount of attempts. Returns *True* on Success.
     *
     * If you'd like to make sure that the webhook was set by you, you can specify secret data in the parameter
     * secret_token. If specified, the request will contain a header “X-Telegram-Bot-Api-Secret-Token”
     * with the secret token as content.
     *
     * **Notes:**
     * 1. You will not be able to receive updates using getUpdates for as long as an outgoing webhook is set up.
     * 2. To use a self-signed certificate, you need to upload your public key certificate using certificate parameter. Please upload as InputFile, sending a String will not work.
     * 3. Ports currently supported for webhooks: 443, 80, 88, 8443.
     *
     * If you're having any trouble setting up webhooks, please check out this amazing guide to webhooks.
     *
     * @param url HTTPS URL to send updates to. Use an empty string to remove webhook integration
     * @param certificate Upload your public key certificate so that the root certificate in use can be checked.
     * See our [self-signed](https://core.telegram.org/bots/self-signed) guide for details.
     * @param ipAddress The fixed IP address which will be used to send webhook requests
     * instead of the IP address resolved through DNS
     * @param maxConnections The maximum allowed number of simultaneous HTTPS connections to the webhook for update
     * delivery, 1-100. Defaults to 40. Use lower values to limit the load on your bot's server, and higher
     * values to increase your bot's throughput.
     * @param allowedUpdates A JSON-serialized list of the update types you want your bot to receive.
     * See [Update] for a complete list of available update types. Specify an empty list to receive all update
     * types except chat_member (default). If not specified, the previous setting will be used.
     *
     * Please note that this parameter doesn't affect updates created before the call to the setWebhook,
     * so unwanted updates may be received for a short period of time.
     * @param dropPendingUpdates Pass *True* to drop all pending updates
     * @param secretToken A secret token to be sent in a header “X-Telegram-Bot-Api-Secret-Token” in every webhook
     * request, 1-256 characters. Only characters `A-Z`, `a-z`, `0-9`, `_` and `-` are allowed.
     * The header is useful to ensure that the request comes from a webhook set by you.
     * */
    fun setWebhook(
        url: String,
        certificate: InputFile? = null,
        ipAddress: String? = null,
        maxConnections: Int? = null,
        allowedUpdates: String? = null,
        dropPendingUpdates: Boolean? = null,
        secretToken: String? = null,
    ) = service.setWebhook(
        url.toRequestBody(), certificate?.toRequestBody("certificate"), ipAddress?.toRequestBody(),
        maxConnections?.toString()?.toRequestBody(), allowedUpdates?.toRequestBody(),
        dropPendingUpdates?.toString()?.toRequestBody(), secretToken?.toRequestBody()
    ).execute().body()?.result ?: errorBody()

    // TODO link to getUpdates
    /**
     * Use this method to remove webhook integration if you decide to switch back to getUpdates. Returns *True* on success.
     *
     * @param dropPendingUpdates Pass *True* to drop all pending updates
     * */
    fun deleteWebhook(dropPendingUpdates: Boolean? = null) =
        service.deleteWebhook(dropPendingUpdates).execute().body()?.result ?: errorBody()

    // TODO link to getUpdates
    /**
     * Use this method to get current webhook status. Requires no parameters.
     * On success, returns a [WebhookInfo] object. If the bot is using getUpdates,
     * will return an object with the url field empty.
     * */
    fun getWebhookInfo() = service.getWebhookInfo().execute().body()?.result ?: errorBody()

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
     * Returns *True* on success
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
     * After deletion, [higher level commands][BotCommandScope] will be shown to affected users. Returns *True* on success.
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
     * modify the list before adding the bot. Returns *True* on success.
     *
     * @param rights [ChatAdministratorRights] object describing new default administrator rights.
     * If not specified, the default administrator rights will be cleared.
     * @param forChannels Pass *True* to change the default administrator rights of the bot in channels.
     * Otherwise, the default administrator rights of the bot for groups and supergroups will be changed.
     * */
    fun setMyDefaultAdministratorRights(rights: ChatAdministratorRights? = null, forChannels: Boolean? = null): Boolean {
        return service.setMyDefaultAdministratorRights(rights, forChannels).execute().body()?.result ?: errorBody()
    }

    /**
     * Use this method to get the current default administrator rights of the bot.
     * Returns ChatAdministratorRights on success.
     *
     * @param forChannels Pass *True* to change the default administrator rights of the bot in channels.
     * Otherwise, the default administrator rights of the bot for groups and supergroups will be returned.
     * */
    fun getMyDefaultAdministratorRights(forChannels: Boolean? = null): ChatAdministratorRights {
        return service.getMyDefaultAdministratorRights(forChannels).execute().body()?.result ?: errorBody()
    }

    /**
     * Use this method to send answers to callback queries sent from [inline keyboards](https://core.telegram.org/bots/features#inline-keyboards). The answer will be displayed
     * to the user as a notification at the top of the chat screen or as an alert. On success, *True* is returned.
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
        return service.getCustomEmojiStickers(json.encodeToString(customEmojiIds)).execute().body()?.result ?: errorBody()
    }

    /**
     * Use this method to upload a .PNG file with a sticker for later use in *[createNewStickerSet]* and
     * *[addStickerToSet]* methods (can be used multiple times). Returns the uploaded File on success.
     *
     * @param userId User identifier of sticker file owner
     * @param sticker A file with the sticker in .WEBP, .PNG, .TGS, or .WEBM format.
     * See [https://core.telegram.org/stickers](https://core.telegram.org/stickers) for technical requirements.
     * @param stickerFormat Format of the sticker
     * */
    fun uploadStickerFile(userId: ChatId.ID, sticker: InputFile, stickerFormat: StickerFormat): File {
        return UploadStickerFile(userId, sticker, stickerFormat).upload(service)
    }

    /**
     * Use this method to create a new sticker set owned by a user.
     * The bot will be able to edit the sticker set thus created. Returns *True* on success.
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
    fun createNewStickerSet(userId: ChatId.ID, name: String, title: String, stickerFormat: StickerFormat, block: CreateNewStickerInStickerSet.() -> Unit): Boolean {
        return CreateNewStickerInStickerSet(userId, name, title, stickerFormat).apply(block).create(service)
    }

    /**
     * Use this method to add a new sticker to a set created by the bot.
     * You must use exactly one of the fields png_sticker, tgs_sticker, or webm_sticker.
     * Animated stickers can be added to animated sticker sets and only to them.
     * Animated sticker sets can have up to 50 stickers. Static sticker sets can have up to 120 stickers.
     * Returns *True* on success.
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
    fun addStickerToSet(userId: ChatId.ID, name: String, sticker: InputSticker): Boolean {
        return AddStickerInStickerSet(userId, name, sticker).add(service)
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

    /**
     * Use this method to change the list of emoji assigned to a regular or custom emoji sticker.
     * The sticker must belong to a sticker set created by the bot. Returns *True* on success.
     *
     * @param sticker File identifier of the sticker
     * @param emojiList 1-20 emoji associated with the sticker
     * */
    fun setStickerEmojiList(sticker: String, emojiList: List<String>): Boolean {
        return service.setStickerEmojiList(sticker, emojiList).execute().body()?.result ?: errorBody()
    }

    /**
     * Use this method to change search keywords assigned to a regular or custom emoji sticker.
     * The sticker must belong to a sticker set created by the bot. Returns *True* on success.
     *
     * @param sticker File identifier of the sticker
     * @param keywords 0-20 search keywords for the sticker with total length of up to 64 characters
     * */
    fun setStickerKeywords(sticker: String, keywords: List<String>): Boolean {
        return service.setStickerKeywords(sticker, keywords).execute().body()?.result ?: errorBody()
    }

    /**
     * Use this method to change the [mask position][MaskPosition] of a mask sticker.
     * The sticker must belong to a sticker set that was created by the bot. Returns *True* on success.
     *
     * @param sticker File identifier of the sticker
     * @param maskPosition position where the mask should be placed on faces. Omit the parameter to remove the mask position.
     * */
    fun setStickerMaskPosition(sticker: String, maskPosition: MaskPosition?): Boolean {
        return service.setStickerMaskPosition(sticker, maskPosition).execute().body()?.result ?: errorBody()
    }

    /**
     * Use this method to set the title of a created sticker set. Returns *True* on success.
     *
     * @param name Sticker set name
     * @param title Sticker set title, 1-64 characters
     * */
    fun setStickerSetTitle(name: String, title: String): Boolean {
        return service.setStickerSetTitle(name, title).execute().body()?.result ?: errorBody()
    }

    /**
     * Use this method to set the thumbnail of a regular or mask sticker set.
     * The format of the thumbnail file must match the format of the stickers in the set. Returns *True* on success.
     *
     * @param name Sticker set name
     * @param userId User identifier of the sticker set owner
     * @param thumbnail A **PNG** image with the thumbnail, must be up to 128 kilobytes in size and have width and height
     * exactly 100px, or a **TGS** animation with the thumbnail up to 32 kilobytes in size;
     * see [Animation Requirements](https://core.telegram.org/stickers#animated-sticker-requirements)
     * for animated sticker technical requirements, or a **WEBM** video with the thumbnail up to 32 kilobytes in size;
     * see [Video Requirements](https://core.telegram.org/stickers#video-sticker-requirements) for video sticker
     * technical requirements. Pass a *file_id* as a String to send a file that already exists on the Telegram servers,
     * pass an HTTP URL as a String for Telegram to get a file from the Internet,
     * or upload a new one. [More information on Sending Files »](https://core.telegram.org/bots/api#sending-files).
     * Animated sticker set thumbnails can't be uploaded via HTTP URL.
     * */
    fun setStickerSetThumbnail(name: String, userId: ChatId.ID, thumbnail: InputFile): Boolean {
        return service.setStickerSetThumbnail(name, userId, thumbnail.toRequestBody("thumbnail")).execute().body()?.result ?: errorBody()
    }

    /**
     * Use this method to set the thumbnail of a custom emoji sticker set. Returns *True* on success.
     *
     * @param name Sticker set name
     * @param customEmojiId Custom emoji identifier of a sticker from the sticker set; pass an empty string to drop the thumbnail and use the first sticker as the thumbnail.
     * */
    fun setCustomEmojiStickerSetThumbnail(name: String, customEmojiId: String?): Boolean {
        return service.setCustomEmojiStickerSetThumbnail(name, customEmojiId).execute().body()?.result ?: errorBody()
    }

    /**
     * Use this method to delete a sticker set that was created by the bot. Returns *True* on success.
     *
     * @param name Sticker set name
     * */
    fun deleteStickerSet(name: String): Boolean {
        return service.deleteStickerSet(name).execute().body()?.result ?: errorBody()
    }

    /**
     * Use this method to send answers to an inline query. On success, *True* is returned.
     * No more than 50 results per query are allowed.
     *
     * @param id Unique identifier for the answered query
     * @param results List of results for the inline query
     * @param cacheTime The maximum amount of time that the result of the inline query may be cached on the server.
     * Defaults to 300 seconds.
     * @param isPersonal Pass *True* if results may be cached on the server side only for the user that sent the query.
     * By default, results may be returned to any user who sends the same query.
     * @param nextOffset Pass the offset that a client should send in the next query with the same text to receive
     * more results. Pass an empty string if there are no more results or if you don't support pagination.
     * Offset length can't exceed 64 bytes.
     * @param button Button to be shown above inline query results
     * */
    fun answerInlineQuery(
        id: String,
        results: List<InlineQueryResult>,
        cacheTime: Duration?,
        isPersonal: Boolean? = null,
        nextOffset: String? = null,
        button: InlineQueryResultsButton? = null
    ): Boolean {
        return service.answerInlineQuery(
            id, TelegramApi.json.encodeToString(results), cacheTime?.inWholeSeconds?.toInt(), isPersonal, nextOffset,
            TelegramApi.json.encodeToString(button)
        ).execute().body()?.result ?: errorBody()
    }

    /**
     * Use this method to set the result of an interaction with a [Web App](https://core.telegram.org/bots/webapps)
     * and send a corresponding message on behalf of the user to the chat from which the query originated.
     * On success, a [SentWebAppMessage] object is returned.
     *
     * @param webAppQueryId Unique identifier for the query to be answered
     * @param result Object describing the message to be sent
     * */
    fun answerWebAppQuery(webAppQueryId: String, result: InlineQueryResult): SentWebAppMessage {
        return service.answerWebAppQuery(webAppQueryId, result).execute().body()?.result ?: errorBody()
    }

    // TODO docs
    fun downloadFile(fileId: String): InputStream {
        val fileResponse = service.getFile(fileId).execute().body() ?: errorBody()
        return downloadFile(fileResponse.file)
    }

    // TODO docs
    fun downloadFile(file: File): InputStream {
        return fileService.downLoad(file.path!!).execute().let {
            (it.body() ?: errorBody()).byteStream()
        }
    }
}