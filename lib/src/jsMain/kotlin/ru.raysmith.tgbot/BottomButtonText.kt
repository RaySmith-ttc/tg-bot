package ru.raysmith.tgbot

import seskar.js.JsValue

@Suppress("NESTED_CLASS_IN_EXTERNAL_INTERFACE")
sealed external interface BottomButtonText {
    companion object {
        @JsValue("Continue")
        val Continue: BottomButtonText

        @JsValue("Cancel")
        val Cancel: BottomButtonText
    }
}

fun String.asBottomButtonText() = unsafeCast<BottomButtonText>()