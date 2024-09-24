package ru.raysmith.tgbot

import seskar.js.JsValue

@Suppress("NESTED_CLASS_IN_EXTERNAL_INTERFACE")
sealed external interface BottomButtonType {
    companion object {
        @JsValue("main")
        val main: BottomButtonType

        @JsValue("secondary")
        val secondary: BottomButtonType
    }
}