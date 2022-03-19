package ru.raysmith.tgbot.utils

import kotlinx.serialization.encodeToString
import ru.raysmith.tgbot.model.network.command.BotCommand
import ru.raysmith.tgbot.model.network.command.BotCommandScope
import ru.raysmith.tgbot.model.network.command.BotCommandScopeDefault
import ru.raysmith.tgbot.network.TelegramApi

@Suppress("BlockingMethodInNonBlockingContext")
fun getMe() = TelegramApi.service.getMe().execute().body()!!

@Suppress("BlockingMethodInNonBlockingContext")
fun getChat(id: String) = TelegramApi.service.getChat(id).execute().body()!!

@Suppress("BlockingMethodInNonBlockingContext")
fun deleteMessage(chatId: String, messageId: Long) = TelegramApi.service.deleteMessage(chatId, messageId).execute().body()!!

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
    return TelegramApi.service.setMyCommands(TelegramApi.json.encodeToString(commands), scope, languageCode)
        .execute().body()?.result ?: false
}

/**
 * Use this method to get the current list of the bot's commands for the given scope and user language.
 * Returns Array of [BotCommand] on success. If commands aren't set, an empty list is returned.
 *
 * @param scope [scope][BotCommandScope] of users for which the commands are relevant. Defaults to [BotCommandScopeDefault].
 * @param languageCode A two-letter ISO 639-1 language code. If empty, commands will be applied to all users from the given scope, for whose language there are no dedicated commands
 * */
fun getMyCommands(scope: BotCommandScope? = null, languageCode: String? = null): List<BotCommand> {
    return TelegramApi.service.getMyCommands(scope, languageCode).execute().body()!!.result
}

/**
 * Use this method to delete the list of the bot's commands for the given scope and user language.
 * After deletion, [higher level commands][BotCommandScope] will be shown to affected users. Returns True on success.
 *
 * @param scope [scope][BotCommandScope] of users for which the commands are relevant. Defaults to [BotCommandScopeDefault].
 * @param languageCode A two-letter ISO 639-1 language code. If empty, commands will be applied to all users from the given scope, for whose language there are no dedicated commands
 * */
fun deleteMyCommands(scope: BotCommandScope? = null, languageCode: String? = null): Boolean {
    return TelegramApi.service.deleteMyCommands(scope, languageCode).execute().body()!!.result
}