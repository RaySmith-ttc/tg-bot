package ru.raysmith.tgbot

/** This object describes the native popup for scanning QR codes. */
external interface ScanQrPopupParams {

    /** The text to be displayed under the 'Scan QR' heading, 0-64 characters. */
    var text: String?
}