package ru.raysmith.tgbot

import seskar.js.JsValue

@Suppress("NESTED_CLASS_IN_EXTERNAL_INTERFACE")
sealed external interface ChatType {
    companion object {
        @JsValue("users")
        val users: ChatType

        @JsValue("bots")
        val bots: ChatType

        @JsValue("groups")
        val groups: ChatType

        @JsValue("channels")
        val channels: ChatType
    }
}

