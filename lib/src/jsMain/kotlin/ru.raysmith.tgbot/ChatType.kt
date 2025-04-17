package ru.raysmith.tgbot

import seskar.js.JsValue

@Suppress("NESTED_CLASS_IN_EXTERNAL_INTERFACE")
sealed external interface ChatType {
    companion object {

        @JsValue("sender")
        val sender: ChatType

        @JsValue("private")
        val private: ChatType

        @JsValue("group")
        val group: ChatType

        @JsValue("supergroup")
        val supergroup: ChatType

        @JsValue("channel")
        val channel: ChatType
    }
}