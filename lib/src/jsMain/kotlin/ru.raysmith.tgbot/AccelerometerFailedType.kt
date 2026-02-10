package ru.raysmith.tgbot

import ru.raysmith.tgbot.AccelerometerFailedType.Companion.UNSUPPORTED
import seskar.js.JsValue

/**
 * @property UNSUPPORTED Accelerometer tracking is not supported on this device or platform
 * */
@Suppress("NESTED_CLASS_IN_EXTERNAL_INTERFACE")
sealed external interface AccelerometerFailedType {
    companion object {

        /** Accelerometer tracking is not supported on this device or platform */
        @JsValue("UNSUPPORTED")
        val UNSUPPORTED: AccelerometerFailedType
    }
}
