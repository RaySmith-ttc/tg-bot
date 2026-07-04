package ru.raysmith.tgbot.model.api.keyboard

import kotlinx.serialization.Contextual
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.raysmith.tgbot.model.api.message.Message

/**
 * Upon receiving a message with this object, Telegram clients will remove the current custom keyboard and display the
 * default letter-keyboard. By default, custom keyboards are displayed until a new keyboard is sent by a bot.
 * An exception is made for one-time keyboards that are hidden immediately after the user presses a button
 * (see [ReplyKeyboardMarkup]). Not supported in channels and for messages sent on behalf of a business account.
 * */
@Serializable
data class ReplyKeyboardRemove(

    /**
     * Use this parameter if you want to show the keyboard to specific users only. Targets:
     * - users that are @mentioned in the *text* of the [Message] object;
     * - if the bot's message is a reply (has *reply_to_message_id*), sender of the original message.
     *
     * *Example:* A user requests to change the bot's language, bot replies to the request with a keyboard
     * to select the new language. Other users in the group don't see the keyboard.
     * */
    @SerialName("selective") val selective: Boolean? = null
) : KeyboardMarkup() {

    /**
     * Requests clients to remove the custom keyboard (user will not be able to summon this keyboard; if you want to
     * hide the keyboard from sight but keep it accessible, use
     * *[oneTimeKeyboard][ReplyKeyboardMarkup.oneTimeKeyboard]* in [ReplyKeyboardMarkup])
     * */
    @EncodeDefault
    @SerialName("remove_keyboard")
    val removeKeyboard: Boolean = true
}
