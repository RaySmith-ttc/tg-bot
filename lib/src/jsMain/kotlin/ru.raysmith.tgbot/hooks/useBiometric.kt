package ru.raysmith.tgbot.hooks

import kotlinx.js.JsPlainObject
import react.useCallback
import react.useEffect
import react.useMemo
import react.useState
import ru.raysmith.tgbot.*
import ru.raysmith.tgbot.events.EventType
import web.function.VoidFunction

/**
 * This hook controls biometrics on the device.
 * Before the first use of this object, it needs to be initialized using the init method.
 * */
fun useBiometric(): BiometricHookType {
    @Suppress("LocalVariableName")
    var BiometricManager by useState(webApp.BiometricManager)
    var isInit by useState(webApp.BiometricManager.isInited)

    val init = useCallback(isInit) { callback: VoidFunction ->
        if (!isInit) {
            BiometricManager.init {
                BiometricManager = copyOf(webApp.BiometricManager)
                isInit = true
                callback()
            }
        }
    }

    val requestAccess = { params: BiometricRequestAccessParams, callback: ((granted: Boolean) -> Unit)? ->
        BiometricManager = copyOf(BiometricManager.requestAccess(params, callback))
    }

    val authenticate = { params: BiometricAuthenticateParams, callback: ((authenticated: Boolean, token: String?) -> Unit)? ->
        BiometricManager = copyOf(BiometricManager.authenticate(params, callback))
    }

    val openSettings = {
        BiometricManager = copyOf(BiometricManager.openSettings())
    }

    val updateBiometricToken = { token: String, callback: ((updated: Boolean) -> Unit)? ->
        BiometricManager = copyOf(BiometricManager.updateBiometricToken(token, callback))
    }

    useEffect(isInit) {
        if (isInit) {
            webApp.onEvent(EventType.biometricManagerUpdated) {
                BiometricManager = copyOf(webApp.BiometricManager)
            }
            webApp.onEvent(EventType.biometricTokenUpdated) {
                BiometricManager = copyOf(webApp.BiometricManager)
            }
        }
    }

    return useMemo(BiometricManager, isInit) {
        BiometricHookType(
            isInited = BiometricManager.isInited,
            isBiometricAvailable = BiometricManager.isBiometricAvailable,
            biometricType = BiometricManager.biometricType,
            isAccessRequested = BiometricManager.isAccessRequested,
            isAccessGranted = BiometricManager.isAccessGranted,
            isBiometricTokenSaved = BiometricManager.isBiometricTokenSaved,
            deviceId = BiometricManager.deviceId,
            init = init,
            requestAccess = requestAccess,
            authenticate = authenticate,
            updateBiometricToken = updateBiometricToken,
            openSettings = openSettings,
        )
    }
}

@JsPlainObject
external interface BiometricHookType {

    /** Shows whether biometrics object is initialized. */
    val isInited: Boolean

    /** Shows whether biometrics is available on the current device. */
    val isBiometricAvailable: Boolean

    /** The type of biometrics currently available on the device. */
    val biometricType: BiometricType

    /** Shows whether permission to use biometrics has been requested. */
    val isAccessRequested: Boolean

    /** Shows whether permission to use biometrics has been granted. */
    val isAccessGranted: Boolean

    /** Shows whether the token is saved in secure storage on the device. */
    val isBiometricTokenSaved: Boolean

    /** A unique device identifier that can be used to match the token to the device. */
    val deviceId: String

    /**
     * A method that initializes the BiometricManager object. It should be called before the object's first use.
     * If an optional *callback* parameter was passed, the *callback* function will be called when the object is
     * initialized.
     */
    val init: (callback: VoidFunction) -> Unit

    /**
     * A method that requests permission to use biometrics according to the *params* argument of type
     * [BiometricRequestAccessParams]. If an optional *callback* parameter was passed, the *callback* function will
     * be called and the first argument will be a boolean indicating whether the user granted access.
     */
    val requestAccess: (params: BiometricRequestAccessParams, callback: ((granted: Boolean) -> Unit)?) -> Unit

    /**
     * A method that authenticates the user using biometrics according to the *params* argument of type
     * [BiometricAuthenticateParams].
     * If an optional *callback* parameter was passed, the *callback* function will be called and the first argument
     * will be a boolean indicating whether the user authenticated successfully.
     * If so, the second argument will be a biometric token.
     */
    val authenticate: (params: BiometricAuthenticateParams, callback: ((authenticated: Boolean, token: String?) -> Unit)?) -> Unit

    /**
     * A method that updates the biometric token in secure storage on the device. To remove the token, pass an empty
     * string. If an optional *callback* parameter was passed, the *callback* function will be called and the first
     * argument will be a boolean indicating whether the token was updated.
     */
    val updateBiometricToken: (token: String, callback: ((updated: Boolean) -> Unit)?) -> Unit

    /**
     * A method that opens the biometric access settings for bots. Useful when you need to request biometrics access
     * to users who haven't granted it yet.
     *
     * *Note that this method can be called only in response to user interaction with the Mini App interface
     * (e.g. a click inside the Mini App or on the main button).*
     */
    val openSettings: () -> Unit
}
