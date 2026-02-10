package ru.raysmith.tgbot

import kotlinx.js.JsPlainObject

/** This object describes the native popup for requesting permission to use biometrics. */
@JsPlainObject
external interface BiometricRequestAccessParams {

    /**
     * The text to be displayed to a user in the popup describing why the bot needs access to biometrics,
     * 0-128 characters.
     * */
    val reason: String?
}
