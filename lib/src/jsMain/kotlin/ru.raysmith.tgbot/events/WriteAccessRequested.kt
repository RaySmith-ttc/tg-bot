package ru.raysmith.tgbot.events

import ru.raysmith.tgbot.events.WriteAccessRequestedStatus.Companion.allowed
import ru.raysmith.tgbot.events.WriteAccessRequestedStatus.Companion.cancelled
import seskar.js.JsValue

/**
 * @property status The status of the write access request
 * */
external interface WriteAccessRequested {

    /** Status of the write access request */
    val status: WriteAccessRequestedStatus
}

/**
 * Status of the write access request
 *
 * @property allowed User granted write permission to the bot
 * @property cancelled User declined this request
 * */
external interface WriteAccessRequestedStatus {
    companion object {

        /** User granted write permission to the bot */
        @JsValue("allowed")
        val allowed: WriteAccessRequestedStatus

        /** User declined this request */
        @JsValue("cancelled")
        val cancelled: WriteAccessRequestedStatus
    }
}

