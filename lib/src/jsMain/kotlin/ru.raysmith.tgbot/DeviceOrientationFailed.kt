package ru.raysmith.tgbot

import kotlinx.js.JsPlainObject

@JsPlainObject
external interface DeviceOrientationFailed {
    val error: DeviceOrientationFailedType
}
