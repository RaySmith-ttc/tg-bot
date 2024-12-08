package ru.raysmith.tgbot

import seskar.js.JsValue

@Suppress("NESTED_CLASS_IN_EXTERNAL_INTERFACE")
sealed external interface FullscreenFailedType {
    companion object {

        /** Fullscreen mode is not supported on this device or platform. */
        @JsValue("UNSUPPORTED")
        val UNSUPPORTED: FullscreenFailedType

        /** The Mini App is already in fullscreen mode. */
        @JsValue("ALREADY_FULLSCREEN")
        val ALREADY_FULLSCREEN: FullscreenFailedType
    }
}