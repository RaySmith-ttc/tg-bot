package ru.raysmith.tgbot

import seskar.js.JsValue

/**
 * @property group
 * @property supergroup
 * @property channel
 * */
@Suppress("NESTED_CLASS_IN_EXTERNAL_INTERFACE")
sealed external interface WebAppChatType {
    companion object {

        @JsValue("group")
        val group: WebAppChatType

        @JsValue("supergroup")
        val supergroup: WebAppChatType

        @JsValue("channel")
        val channel: WebAppChatType
    }
}
