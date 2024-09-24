package ru.raysmith.tgbot

import web.scheduling.VoidFunction

/**
 * This object controls biometrics on the device.
 * Before the first use of this object, it needs to be initialized using the [init] method.
 * */
external interface BiometricManager {

    /**
     * Shows whether biometrics object is initialized.
     *
     * @since Bot API 7.2
     */
    val isInited: Boolean

    /**
     * Shows whether biometrics is available on the current device.
     *
     * @since Bot API 7.2
     */
    val isBiometricAvailable: Boolean

    /**
     * The type of biometrics currently available on the device.
     *
     * @since Bot API 7.2
     */
    val biometricType: BiometricType

    /**
     * Shows whether permission to use biometrics has been requested.
     *
     * @since Bot API 7.2
     */
    val isAccessRequested: Boolean

    /**
     * Shows whether permission to use biometrics has been granted.
     *
     * @since Bot API 7.2
     */
    val isAccessGranted: Boolean

    /**
     * Shows whether the token is saved in secure storage on the device.
     *
     * @since Bot API 7.2
     */
    val isBiometricTokenSaved: Boolean

    /**
     * A unique device identifier that can be used to match the token to the device.
     *
     * @since Bot API 7.2
     */
    val deviceId: String

    /**
     * A method that initializes the BiometricManager object. It should be called before the object's first use.
     * If an optional *callback* parameter was passed, the *callback* function will be called when the object is initialized.
     *
     * @since Bot API 7.2
     */
    val init: (callback: VoidFunction?) -> BiometricManager

    /**
     * A method that requests permission to use biometrics according to the *params* argument of type
     * [BiometricRequestAccessParams]. If an optional *callback* parameter was passed, the *callback* function will
     * be called and the first argument will be a boolean indicating whether the user granted access.
     *
     * @since Bot API 7.2
     */
    val requestAccess: (params: BiometricRequestAccessParams, callback: ((granted: Boolean) -> Unit)?) -> BiometricManager

    /**
     * A method that authenticates the user using biometrics according to the *params* argument of type
     * [BiometricAuthenticateParams].
     * If an optional *callback* parameter was passed, the *callback* function will be called and the first argument
     * will be a boolean indicating whether the user authenticated successfully.
     * If so, the second argument will be a biometric token.
     *
     * @since Bot API 7.2
     */
    val authenticate: (params: BiometricAuthenticateParams, callback: ((authenticated: Boolean, token: String?) -> Unit)?) -> BiometricManager

    /**
     * A method that updates the biometric token in secure storage on the device. To remove the token, pass an empty string.
     * If an optional *callback* parameter was passed, the *callback* function will be called and the first argument
     * will be a boolean indicating whether the token was updated.
     *
     * @since Bot API 7.2
     */
    val updateBiometricToken: (token: String, callback: ((updated: Boolean) -> Unit)?) -> BiometricManager

    /**
     * A method that opens the biometric access settings for bots. Useful when you need to request biometrics access
     * to users who haven't granted it yet.
     *
     * *Note that this method can be called only in response to user interaction with the Mini App interface
     * (e.g. a click inside the Mini App or on the main button).*
     *
     * @since Bot API 7.2
     */
    val openSettings: () -> BiometricManager
}

/**
 * A method that initializes the BiometricManager object. It should be called before the object's first use.
 *
 * @since Bot API 7.2
 */
fun BiometricManager.init() = init(null)