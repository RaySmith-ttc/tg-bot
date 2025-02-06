package ru.raysmith.tgbot.hooks

import js.objects.jso
import react.*
import ru.raysmith.tgbot.*
import web.scheduling.VoidFunction

/**
 * This hook controls biometrics on the device.
 * Before the first use of this object, it needs to be initialized using the init method.
 * */
fun useBiometric(): BiometricHookType {
    @Suppress("LocalVariableName")
    var BiometricManager by useState(webApp.BiometricManager)
    var isInit by useState(false)

    val init = useCallback(isInit) { callback: VoidFunction ->
        if (!isInit) {
            BiometricManager.init {
                BiometricManager = webApp.BiometricManager
                isInit = true
                callback()
            }
        }
    }

    val requestAccess = useCallback(BiometricManager) { params: BiometricRequestAccessParams, callback: ((granted: Boolean) -> Unit)? ->
        BiometricManager = BiometricManager.requestAccess(params, callback)
    }

    val authenticate = useCallback(BiometricManager) { params: BiometricAuthenticateParams, callback: ((authenticated: Boolean, token: String?) -> Unit)? ->
        BiometricManager = BiometricManager.authenticate(params, callback)
    }

    val openSettings = useCallback(BiometricManager,) {
        BiometricManager = BiometricManager.openSettings()
    }

    val updateBiometricToken = useCallback(BiometricManager) { token: String, callback: ((updated: Boolean) -> Unit)? ->
        BiometricManager = BiometricManager.updateBiometricToken(token, callback)
    }

    useEffect(isInit) {
        if (isInit) {
            webApp.onEvent(EventType.biometricManagerUpdated) {
                BiometricManager = webApp.BiometricManager
            }
            webApp.onEvent(EventType.biometricTokenUpdated) {
                BiometricManager = webApp.BiometricManager
            }
        }
    }

    return useMemo(
        BiometricManager, isInit // redundant // init, requestAccess, authenticate, updateBiometricToken, openSettings
    ) {
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

    /**
     * Shows whether biometrics object is initialized.
     *
     * @since Bot API 7.2
     */
    var isInited: Boolean

    /**
     * Shows whether biometrics is available on the current device.
     *
     * @since Bot API 7.2
     */
    var isBiometricAvailable: Boolean

    /**
     * The type of biometrics currently available on the device.
     *
     * @since Bot API 7.2
     */
    var biometricType: BiometricType

    /**
     * Shows whether permission to use biometrics has been requested.
     *
     * @since Bot API 7.2
     */
    var isAccessRequested: Boolean

    /**
     * Shows whether permission to use biometrics has been granted.
     *
     * @since Bot API 7.2
     */
    var isAccessGranted: Boolean

    /**
     * Shows whether the token is saved in secure storage on the device.
     *
     * @since Bot API 7.2
     */
    var isBiometricTokenSaved: Boolean

    /**
     * A unique device identifier that can be used to match the token to the device.
     *
     * @since Bot API 7.2
     */
    var deviceId: String

    /**
     * A method that initializes the BiometricManager object. It should be called before the object's first use.
     * If an optional *callback* parameter was passed, the *callback* function will be called when the object is
     * initialized.
     *
     * @since Bot API 7.2
     */
    var init: (callback: VoidFunction) -> Unit

    /**
     * A method that requests permission to use biometrics according to the *params* argument of type
     * [BiometricRequestAccessParams]. If an optional *callback* parameter was passed, the *callback* function will
     * be called and the first argument will be a boolean indicating whether the user granted access.
     *
     * @since Bot API 7.2
     */
    var requestAccess: (params: BiometricRequestAccessParams, callback: ((granted: Boolean) -> Unit)?) -> Unit

    /**
     * A method that authenticates the user using biometrics according to the *params* argument of type
     * [BiometricAuthenticateParams].
     * If an optional *callback* parameter was passed, the *callback* function will be called and the first argument
     * will be a boolean indicating whether the user authenticated successfully.
     * If so, the second argument will be a biometric token.
     *
     * @since Bot API 7.2
     */
    var authenticate: (params: BiometricAuthenticateParams, callback: ((authenticated: Boolean, token: String?) -> Unit)?) -> Unit

    /**
     * A method that updates the biometric token in secure storage on the device. To remove the token, pass an empty
     * string. If an optional *callback* parameter was passed, the *callback* function will be called and the first
     * argument will be a boolean indicating whether the token was updated.
     *
     * @since Bot API 7.2
     */
    var updateBiometricToken: (token: String, callback: ((updated: Boolean) -> Unit)?) -> Unit

    /**
     * A method that opens the biometric access settings for bots. Useful when you need to request biometrics access
     * to users who haven't granted it yet.
     *
     * *Note that this method can be called only in response to user interaction with the Mini App interface
     * (e.g. a click inside the Mini App or on the main button).*
     *
     * @since Bot API 7.2
     */
    var openSettings: () -> Unit
}