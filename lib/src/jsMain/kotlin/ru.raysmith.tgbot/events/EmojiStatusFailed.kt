package ru.raysmith.tgbot.events

import ru.raysmith.tgbot.events.EmojiStatusFailedError.Companion.DURATION_INVALID
import ru.raysmith.tgbot.events.EmojiStatusFailedError.Companion.SERVER_ERROR
import ru.raysmith.tgbot.events.EmojiStatusFailedError.Companion.SUGGESTED_EMOJI_INVALID
import ru.raysmith.tgbot.events.EmojiStatusFailedError.Companion.UNKNOWN_ERROR
import ru.raysmith.tgbot.events.EmojiStatusFailedError.Companion.UNSUPPORTED
import ru.raysmith.tgbot.events.EmojiStatusFailedError.Companion.USER_DECLINED
import seskar.js.JsValue

/**
 * @property error Reason for the failure
 * */
external interface EmojiStatusFailed {

    /** Reason for the failure */
    val error: EmojiStatusFailedError
}

/**
 * The reason for the failure when setting an emoji status fails.
 *
 * @property UNSUPPORTED The feature is not supported by the client
 * @property SUGGESTED_EMOJI_INVALID One or more emoji identifiers are invalid
 * @property DURATION_INVALID The specified duration is invalid
 * @property USER_DECLINED The user closed the dialog without setting a status
 * @property SERVER_ERROR A server error occurred when attempting to set the status
 * @property UNKNOWN_ERROR An unknown error occurred
 * */
sealed external interface EmojiStatusFailedError {
    companion object {

        /** The feature is not supported by the client */
        @JsValue("UNSUPPORTED")
        val UNSUPPORTED: EmojiStatusFailedError

        /** One or more emoji identifiers are invalid */
        @JsValue("SUGGESTED_EMOJI_INVALID")
        val SUGGESTED_EMOJI_INVALID: EmojiStatusFailedError

        /** The specified duration is invalid */
        @JsValue("DURATION_INVALID")
        val DURATION_INVALID: EmojiStatusFailedError

        /** The user closed the dialog without setting a status */
        @JsValue("USER_DECLINED")
        val USER_DECLINED: EmojiStatusFailedError

        /** A server error occurred when attempting to set the status */
        @JsValue("SERVER_ERROR")
        val SERVER_ERROR: EmojiStatusFailedError

        /** An unknown error occurred */
        @JsValue("UNKNOWN_ERROR")
        val UNKNOWN_ERROR: EmojiStatusFailedError
    }
}