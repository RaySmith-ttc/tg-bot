package ru.raysmith.tgbot

/** This object describes the native popup for requesting permission to use biometrics. */
external interface BiometricRequestAccessParams {

    /**
     * The text to be displayed to a user in the popup describing why the bot needs access to biometrics,
     * 0-128 characters.
     * */
    var reason: String?
}