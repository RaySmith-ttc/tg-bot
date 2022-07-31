package ru.raysmith.tgbot.core

import kotlinx.serialization.encodeToString
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.User
import ru.raysmith.tgbot.model.network.command.BotCommand
import ru.raysmith.tgbot.model.network.command.BotCommandScope
import ru.raysmith.tgbot.model.network.command.BotCommandScopeDefault
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

    fun getChat(id: ChatId) = service.getChat(id).execute().body()?.result ?: errorBody()

    /**
     * Use this method to change the list of the bot's commands.
     *
     * @see <a href="https://core.telegram.org/bots#commands">commands</a> for more details about bot commands. Returns True on success
     *
     * @param commands list of bot commands to be set as the list of the bot's commands. At most 100 commands can be specified.
     * @param scope [scope][BotCommandScope] of users for which the commands are relevant. Defaults to [BotCommandScopeDefault].
     * @param languageCode A two-letter ISO 639-1 language code. If empty, commands will be applied to all users from the given scope, for whose language there are no dedicated commands
     * */
    fun setMyCommands(commands: List<BotCommand>, scope: BotCommandScope? = null, languageCode: String? = null): Boolean {
        return service.setMyCommands(TelegramApi.json.encodeToString(commands), scope, languageCode)
            .execute().body()?.result ?: errorBody()
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
     * Use this method to delete the list of the bot's commands for the given scope and user language.
     * After deletion, [higher level commands][BotCommandScope] will be shown to affected users. Returns True on success.
     *
     * @param scope [scope][BotCommandScope] of users for which the commands are relevant. Defaults to [BotCommandScopeDefault].
     * @param languageCode A two-letter ISO 639-1 language code. If empty, commands will be applied to all users from the given scope, for whose language there are no dedicated commands
     * */
    fun deleteMyCommands(scope: BotCommandScope? = null, languageCode: String? = null): Boolean {
        return service.deleteMyCommands(scope, languageCode).execute().body()?.result ?: errorBody()
    }
}