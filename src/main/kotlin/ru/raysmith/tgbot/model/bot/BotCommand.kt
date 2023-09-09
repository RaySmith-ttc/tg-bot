package ru.raysmith.tgbot.model.bot

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.model.network.User

/**
 * Represents a bot command
 *
 * @param commandText original command text
 * */
class BotCommand(val commandText: String) {

    /** Command text without mention. For example, /start */
    val body: String = commandText.substring(1, getArgsStartIndex()).let {
        if (it.contains("@")) it.substring(0, it.indexOf("@")) else it
    }.trim()

    val botMention = commandText.let {
        if (it.contains("@")) it.substring(it.indexOf("@") + 1, getArgsStartIndex()) else null
    }

    /** Returns true if the command mentions a bot or is missing */
    fun mentionIsCurrentBot(bot: Bot) = botMention == null || botMention == bot.me.username

    /** Returns true if mention in command is null or [bot user][botUser] */
    fun mentionIsCurrentBot(botUser: User) = botMention == null || botMention == botUser.username

    /**
     * The rest of the text in message after the command.
     *
     * For example, from command `/start myarg`, [argsString] would be `myarg`
     * */
    val argsString: String? = commandText.substring(getArgsStartIndex()).trim().ifEmpty { null }

    companion object {
        
        /**
         * Begins the interaction with the user, like sending an introductory message.
         * This command can also be used to pass additional parameters to the bot ([argsString]).
         * */
        const val START = "start"
        
        /** Returns a help message, like a short text about what your bot can do and a list of commands. */
        const val HELP = "help"
        
        /** (if applicable) shows the bot's settings for this user and suggests commands to edit them. */
        const val SETTINGS = "settings"
        
        private val bodyArgsSeparateChars = charArrayOf(' ', '\n')
    }

    private fun getArgsStartIndex() = commandText.indexOfAny(bodyArgsSeparateChars).let { if (it == -1) commandText.length else it + 1 }

    override fun toString(): String {
        return "Command(body=$body, argsString=$argsString, botMention=$botMention)"
    }
}