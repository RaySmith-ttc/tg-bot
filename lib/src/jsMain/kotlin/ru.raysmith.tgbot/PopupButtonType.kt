package ru.raysmith.tgbot

import seskar.js.JsValue

@Suppress("NESTED_CLASS_IN_EXTERNAL_INTERFACE")
sealed external interface PopupButtonType {
    companion object {

        /** A button with the default style */
        @JsValue("default")
        val default: PopupButtonType

        /** A button with the localized text “OK” */
        @JsValue("ok")
        val ok: PopupButtonType

        /** A button with the localized text “Close” */
        @JsValue("close")
        val close: PopupButtonType

        /** A button with the localized text “Cancel” */
        @JsValue("cancel")
        val cancel: PopupButtonType

        /** A button with a style that indicates a destructive action (e.g. “Remove”, “Delete”, etc.). */
        @JsValue("destructive")
        val destructive: PopupButtonType
    }
}