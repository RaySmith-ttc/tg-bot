package ru.raysmith.tgbot

import seskar.js.JsValue

@Suppress("NESTED_CLASS_IN_EXTERNAL_INTERFACE")
sealed external interface BottomButtonPosition {
    companion object {

        /** displayed to the left of the main button */
        @JsValue("left")
        val left: BottomButtonPosition

        /** displayed to the right of the main button */
        @JsValue("right")
        val right: BottomButtonPosition

        /** displayed above the main button */
        @JsValue("top")
        val top: BottomButtonPosition

        /** displayed below the main button */
        @JsValue("bottom")
        val bottom: BottomButtonPosition
    }
}