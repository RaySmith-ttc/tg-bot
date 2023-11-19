package ru.raysmith.tgbot.model.bot

/**
 * Represents a bot command.
 *
 * @param commandText original command text
 * */
class BotCommand(val commandText: String) {

    /** Command text without mention. For example, /start */
    val body: String = commandText.substring(1, getArgsStartIndex()).let {
        if (it.contains("@")) it.substring(0, it.indexOf("@")) else it
    }.trim()

    /** Bot mention in the command without `@` symbol. For example, for command `@BotFather /start` [mention] would be `BotFather` */
    val mention = commandText.let {
        if (it.startsWith("@")) it.substring(1, getArgsStartIndex()) else null
    }

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
        return "Command(body=$body, argsString=$argsString, botMention=$mention)"
    }
}