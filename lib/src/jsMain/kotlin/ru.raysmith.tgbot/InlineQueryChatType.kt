package ru.raysmith.tgbot

import seskar.js.JsValue

@Suppress("NESTED_CLASS_IN_EXTERNAL_INTERFACE")
sealed external interface InlineQueryChatType {
    companion object {
        @JsValue("users")
        val users: InlineQueryChatType

        @JsValue("bots")
        val bots: InlineQueryChatType

        @JsValue("groups")
        val groups: InlineQueryChatType

        @JsValue("channels")
        val channels: InlineQueryChatType
    }
}