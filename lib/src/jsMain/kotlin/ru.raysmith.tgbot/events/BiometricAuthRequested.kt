package ru.raysmith.tgbot.events

/**
 * @property isAuthenticated Whether the user was authenticated successfully
 * @property biometricToken If [isAuthenticated] is true, the field will contain the biometric token stored in secure
 *                          storage on the device.
 * */
external interface BiometricAuthRequested {

    /** Whether the user was authenticated successfully */
    val isAuthenticated: Boolean

    /**
     * If [isAuthenticated] is true, the field will contain the biometric token stored in secure storage on the device.
     **/
    val biometricToken: String?
}