package ru.raysmith.tgbot.events

/**
 * @property data Text from the QR code
 * */
external interface QrTextReceived {

    /** Text from the QR code */
    val data: String
}