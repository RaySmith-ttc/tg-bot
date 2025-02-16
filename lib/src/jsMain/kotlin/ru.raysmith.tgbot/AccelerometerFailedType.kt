package ru.raysmith.tgbot

import seskar.js.JsValue

@Suppress("NESTED_CLASS_IN_EXTERNAL_INTERFACE")
sealed external interface AccelerometerFailedType {
    companion object {

        /** Accelerometer tracking is not supported on this device or platform */
        @JsValue("UNSUPPORTED")
        val UNSUPPORTED: AccelerometerFailedType
    }
}