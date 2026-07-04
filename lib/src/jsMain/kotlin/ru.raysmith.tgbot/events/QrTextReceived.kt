package ru.raysmith.tgbot.events

import kotlinx.js.JsPlainObject

/**
 * @property data Text from the QR code
 * */
@JsPlainObject
external interface QrTextReceived {
    val data: String
}
