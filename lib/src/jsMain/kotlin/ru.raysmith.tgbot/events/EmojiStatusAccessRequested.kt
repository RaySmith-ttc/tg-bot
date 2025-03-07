package ru.raysmith.tgbot.events

import ru.raysmith.tgbot.events.EmojiStatusAccessRequestedStatus.Companion.allowed
import ru.raysmith.tgbot.events.EmojiStatusAccessRequestedStatus.Companion.cancelled
import seskar.js.JsValue

/**
 * @property status Status of the emoji status access request
 * */
external interface EmojiStatusAccessRequested {

    /** Status of the emoji status access request */
    val status: EmojiStatusAccessRequestedStatus
}

/**
 * The status of the emoji status access request
 *
 * @property allowed User granted emoji status permission to the bot
 * @property cancelled User declined this request
 * */
sealed external interface EmojiStatusAccessRequestedStatus {
    companion object {

        /** User granted emoji status permission to the bot */
        @JsValue("allowed")
        val allowed: EmojiStatusAccessRequestedStatus

        /** User declined this request */
        @JsValue("cancelled")
        val cancelled: EmojiStatusAccessRequestedStatus
    }
}