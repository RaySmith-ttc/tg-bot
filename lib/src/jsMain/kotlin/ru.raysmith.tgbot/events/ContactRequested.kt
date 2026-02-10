package ru.raysmith.tgbot.events

import kotlinx.js.JsPlainObject
import ru.raysmith.tgbot.events.ContactRequestedStatus.Companion.cancelled
import ru.raysmith.tgbot.events.ContactRequestedStatus.Companion.sent
import seskar.js.JsValue

/**
 * @property status The status of the contact request
 * */
@JsPlainObject
external interface ContactRequested {
    val status: ContactRequestedStatus
}

/**
 * The status of the contact request
 *
 * @property sent User shared their phone number with the bot
 * @property cancelled User declined this request
 * */
sealed external interface ContactRequestedStatus {
    companion object {

        /** User shared their phone number with the bot */
        @JsValue("allowed")
        val sent: WriteAccessRequestedStatus

        /** User declined this request */
        @JsValue("cancelled")
        val cancelled: WriteAccessRequestedStatus
    }
}
