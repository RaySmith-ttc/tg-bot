package ru.raysmith.tgbot.events

import kotlinx.js.JsPlainObject

/**
 * @property isAuthenticated Whether the user was authenticated successfully
 * @property biometricToken If [isAuthenticated] is true, the field will contain the biometric token stored in secure
 *  storage on the device.
 * */
@JsPlainObject
external interface BiometricAuthRequested {
    val isAuthenticated: Boolean
    val biometricToken: String?
}
