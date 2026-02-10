package ru.raysmith.tgbot

import kotlinx.js.JsPlainObject

/**
 * This object describes the native popup for scanning QR codes.
 *
 * @property text The text to be displayed under the 'Scan QR' heading, 0-64 characters.
 * */
@JsPlainObject
external interface ScanQrPopupParams {
    var text: String?
}
