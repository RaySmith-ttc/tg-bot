package ru.raysmith.tgbot

import kotlinx.js.JsPlainObject

@JsPlainObject
external interface AccelerometerFailed {
    val error: AccelerometerFailedType
}
