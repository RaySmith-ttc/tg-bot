package ru.raysmith.tgbot

import kotlinx.js.JsPlainObject

@JsPlainObject
external interface GyroscopeFailed {
    val error: GyroscopeFailedType
}
