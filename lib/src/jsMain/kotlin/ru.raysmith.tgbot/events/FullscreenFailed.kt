package ru.raysmith.tgbot.events

import ru.raysmith.tgbot.events.FullscreenFailedType.Companion.ALREADY_FULLSCREEN
import ru.raysmith.tgbot.events.FullscreenFailedType.Companion.UNSUPPORTED
import seskar.js.JsValue

/**
 * @property error Reason for the failure
 * */
external interface FullscreenFailed {

    /** Reason for the failure */
    val error: FullscreenFailedType
}

/**
 * The reason for the failure when fullscreen fails.
 *
 * @property UNSUPPORTED Fullscreen mode is not supported on this device or platform
 * @property ALREADY_FULLSCREEN The Mini App is already in fullscreen mode
 */
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