package ru.raysmith.tgbot.model.bot

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.model.network.User
import ru.raysmith.tgbot.utils.botContext

/**
 * Represent a bot command
 *
 * @param commandText raw test of message
 * */
class BotCommand(val commandText: String) {

    /** Command. For example, /start */
    val body: String = commandText.substring(1, getArgsStartIndex()).let {
        if (it.contains("@")) it.substring(0, it.indexOf("@")) else it
    }.trim()

    val botMention = commandText.let {
        if (it.contains("@")) it.substring(it.indexOf("@") + 1, getArgsStartIndex()) else null
    }

    /** Returns true if the command mentions a bot or is missing */
    fun mentionIsCurrentBot(bot: Bot) = botMention == null || botMention == botContext(bot) { getMe() }.username

    /** Returns true if mention in command is null or [bot user][botUser] */
    fun mentionIsCurrentBot(botUser: User) = botMention == null || botMention == botUser.username

    /**
     * The rest of the text in message after the command.
     *
     * For example, from command `/start myarg`, [argsString] would be `myarg`
     * */
    val argsString: String? = if (hasArgs()) {
        commandText.substring(getArgsStartIndex()).trim()
    } else {
        null
    }

    companion object {
        const val START = "start"
        private val bodyArgsSeparateChars = charArrayOf(' ', '\n')
    }

    /** Return true if the command has arguments */
    private fun hasArgs() = commandText.length != body.length + (botMention?.length?.let { it + 1 } ?: 0)

    private fun getArgsStartIndex() = commandText.indexOfAny(bodyArgsSeparateChars).let { if (it == -1) commandText.length else it + 1 }

    override fun toString(): String {
        return "Command(body=$body, argsString=$argsString, botMention=$botMention)"
    }
}