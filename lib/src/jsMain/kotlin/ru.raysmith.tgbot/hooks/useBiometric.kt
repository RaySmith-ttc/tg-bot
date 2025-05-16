package ru.raysmith.tgbot.hooks

import js.objects.jso
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
        jso {
            this.isInited = BiometricManager.isInited
            this.isBiometricAvailable = BiometricManager.isBiometricAvailable
            this.biometricType = BiometricManager.biometricType
            this.isAccessRequested = BiometricManager.isAccessRequested
            this.isAccessGranted = BiometricManager.isAccessGranted
            this.isBiometricTokenSaved = BiometricManager.isBiometricTokenSaved
            this.deviceId = BiometricManager.deviceId
            this.init = init
            this.requestAccess = requestAccess
            this.authenticate = authenticate
            this.updateBiometricToken = updateBiometricToken
            this.openSettings = openSettings
        }
    }
}

external interface BiometricHookType {

    /** Shows whether biometrics object is initialized. */
    var isInited: Boolean

    /** Shows whether biometrics is available on the current device. */
    var isBiometricAvailable: Boolean

    /** The type of biometrics currently available on the device. */
    var biometricType: BiometricType

    /** Shows whether permission to use biometrics has been requested. */
    var isAccessRequested: Boolean

    /** Shows whether permission to use biometrics has been granted. */
    var isAccessGranted: Boolean

    /** Shows whether the token is saved in secure storage on the device. */
    var isBiometricTokenSaved: Boolean

    /** A unique device identifier that can be used to match the token to the device. */
    var deviceId: String

    /**
     * A method that initializes the BiometricManager object. It should be called before the object's first use.
     * If an optional *callback* parameter was passed, the *callback* function will be called when the object is
     * initialized.
     */
    var init: (callback: VoidFunction) -> Unit

    /**
     * A method that requests permission to use biometrics according to the *params* argument of type
     * [BiometricRequestAccessParams]. If an optional *callback* parameter was passed, the *callback* function will
     * be called and the first argument will be a boolean indicating whether the user granted access.
     */
    var requestAccess: (params: BiometricRequestAccessParams, callback: ((granted: Boolean) -> Unit)?) -> Unit

    /**
     * A method that authenticates the user using biometrics according to the *params* argument of type
     * [BiometricAuthenticateParams].
     * If an optional *callback* parameter was passed, the *callback* function will be called and the first argument
     * will be a boolean indicating whether the user authenticated successfully.
     * If so, the second argument will be a biometric token.
     */
    var authenticate: (params: BiometricAuthenticateParams, callback: ((authenticated: Boolean, token: String?) -> Unit)?) -> Unit

    /**
     * A method that updates the biometric token in secure storage on the device. To remove the token, pass an empty
     * string. If an optional *callback* parameter was passed, the *callback* function will be called and the first
     * argument will be a boolean indicating whether the token was updated.
     */
    var updateBiometricToken: (token: String, callback: ((updated: Boolean) -> Unit)?) -> Unit

    /**
     * A method that opens the biometric access settings for bots. Useful when you need to request biometrics access
     * to users who haven't granted it yet.
     *
     * *Note that this method can be called only in response to user interaction with the Mini App interface
     * (e.g. a click inside the Mini App or on the main button).*
     */
    var openSettings: () -> Unit
}