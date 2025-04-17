package ru.raysmith.tgbot.events

import ru.raysmith.tgbot.events.ShareMessageFailedError.Companion.MESSAGE_EXPIRED
import ru.raysmith.tgbot.events.ShareMessageFailedError.Companion.MESSAGE_SEND_FAILED
import ru.raysmith.tgbot.events.ShareMessageFailedError.Companion.UNKNOWN_ERROR
import ru.raysmith.tgbot.events.ShareMessageFailedError.Companion.UNSUPPORTED
import ru.raysmith.tgbot.events.ShareMessageFailedError.Companion.USER_DECLINED
import seskar.js.JsValue

/**
 * @property error Reason for the failure
 * */
external interface ShareMessageFailed {

    /** Reason for the failure */
    val error: ShareMessageFailedError
}

/**
 * The reason for the failure when sharing a message requested.
 *
 * @property UNSUPPORTED The feature is not supported by the client
 * @property MESSAGE_EXPIRED The message could not be retrieved because it has expired
 * @property MESSAGE_SEND_FAILED An error occurred while attempting to send the message
 * @property USER_DECLINED The user closed the dialog without sharing the message
 * @property UNKNOWN_ERROR An unknown error occurred
 * */
sealed external interface ShareMessageFailedError {
    companion object {

        /** The feature is not supported by the client */
        @JsValue("UNSUPPORTED")
        val UNSUPPORTED: ShareMessageFailedError

        /** The message could not be retrieved because it has expired */
        @JsValue("MESSAGE_EXPIRED")
        val MESSAGE_EXPIRED: ShareMessageFailedError

        /** An error occurred while attempting to send the message */
        @JsValue("MESSAGE_SEND_FAILED")
        val MESSAGE_SEND_FAILED: ShareMessageFailedError

        /** The user closed the dialog without sharing the message */
        @JsValue("USER_DECLINED")
        val USER_DECLINED: ShareMessageFailedError

        /** An unknown error occurred */
        @JsValue("UNKNOWN_ERROR")
        val UNKNOWN_ERROR: ShareMessageFailedError
    }
}