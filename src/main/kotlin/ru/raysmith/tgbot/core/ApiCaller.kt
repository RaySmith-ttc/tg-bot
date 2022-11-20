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
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.network.TelegramFileService
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.utils.errorBody

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
}