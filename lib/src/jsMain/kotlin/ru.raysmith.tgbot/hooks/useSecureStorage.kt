package ru.raysmith.tgbot.hooks

import kotlinx.js.JsPlainObject
import react.useMemo
import react.useState
import ru.raysmith.tgbot.webApp
import seskar.js.JsValue

/**
 *  A state variable that tracks the current state of the hook:
 * - `IDLE`: The hook is ready to use.
 * - `SETTING_ITEM`: Setting an item in secure storage.
 * - `GETTING_ITEM`: Fetching an item from secure storage.
 * - `RESTORING_ITEM`: Restoring an item from secure storage.
 * - `REMOVING_ITEM`: Removing an item from secure storage.
 * - `CLEARING`: Clearing all items from secure storage.
 * */
sealed external interface SecureStorageHookState {
    companion object {

        /** The hook is ready to use */
        @JsValue("IDLE") val IDLE: SecureStorageHookState

        /** Setting an item in secure storage */
        @JsValue("SETTING_ITEM") val SETTING_ITEM: SecureStorageHookState

        /** Fetching an item from secure storage */
        @JsValue("GETTING_ITEM") val GETTING_ITEM: SecureStorageHookState

        /** Restoring an item from secure storage */
        @JsValue("RESTORING_ITEM") val RESTORING_ITEM: SecureStorageHookState

        /** Removing an item from secure storage */
        @JsValue("REMOVING_ITEM") val REMOVING_ITEM: SecureStorageHookState

        /** Clearing all items from secure storage */
        @JsValue("CLEARING") val CLEARING: SecureStorageHookState
    }
}

/**
 * This hook provides controls for the device's secure storage ([SecureStorage][ru.raysmith.tgbot.SecureStorage]).
 * On **iOS** it uses the system **Keychain**, on **Android** it uses the **Keystore**, so all stored values are
 * encrypted at rest. Each bot can store up to **10 items per user**.
 *
 * - `state`: The current state of the hook. See [SecureStorageHookState]
 * - `lastError`: Error message if the last operation has failed.
 * */
fun useSecureStorage(): SecureStorageHookType {
    var state by useState(SecureStorageHookState.IDLE)
    var lastError by useState<String?>(null)

    val setItem: (key: String, value: String, callback: ((error: String?, isStored: Boolean) -> Unit)?) -> Unit
            = { key, value, callback ->
                state = SecureStorageHookState.SETTING_ITEM
                webApp.SecureStorage.setItem(key, value) { error, isStored ->
                    state = SecureStorageHookState.IDLE
                    lastError = error
                    callback?.invoke(error, isStored)
                }
            }

    val getItem: (key: String, callback: (error: String?, value: String?, canBeStored: Boolean) -> Unit) -> Unit
            = { key, callback ->
                state = SecureStorageHookState.GETTING_ITEM
                webApp.SecureStorage.getItem(key) { error, value, canBeStored ->
                    state = SecureStorageHookState.IDLE
                    lastError = error
                    callback(error, value, canBeStored)
                }
            }

    val restoreItem: (key: String, callback: (error: String?, value: String?) -> Unit) -> Unit
            = { key, callback ->
                state = SecureStorageHookState.RESTORING_ITEM
                webApp.SecureStorage.restoreItem(key) { error, value ->
                    state = SecureStorageHookState.IDLE
                    lastError = error
                    callback(error, value)
                }
            }

    val removeItem: (key: String, callback: ((error: String?, isRemoved: Boolean) -> Unit)?) -> Unit
            = { key, callback ->
                state = SecureStorageHookState.REMOVING_ITEM
                webApp.SecureStorage.removeItem(key) { error, isRemoved ->
                    state = SecureStorageHookState.IDLE
                    lastError = error
                    callback?.invoke(error, isRemoved)
                }
            }

    val clear: (callback: ((error: String?, isCleared: Boolean) -> Unit)?) -> Unit
            = { callback ->
                state = SecureStorageHookState.CLEARING
                webApp.SecureStorage.clear { error, isCleared ->
                    state = SecureStorageHookState.IDLE
                    lastError = error
                    callback?.invoke(error, isCleared)
                }
            }

    return useMemo(state, lastError) {
        SecureStorageHookType(
            state = state,
            lastError = lastError,
            setItem = setItem,
            getItem = getItem,
            restoreItem = restoreItem,
            removeItem = removeItem,
            clear = clear,
        )
    }
}

@JsPlainObject
external interface SecureStorageHookType {

    /** A state that tracks the current state of the hook */
    val state: SecureStorageHookState

    /** An error message if the last operation has failed */
    val lastError: String?

    /**
     * A method that stores a value in the device's secure storage using the specified `key`. If an optional
     * `callback` parameter was passed, the `callback` function will be called. In case of an error, `error` will
     * contain the error. In case of success, `error` will be `null` and `isStored` will indicate whether the value
     * was stored.
     */
    val setItem: (key: String, value: String, callback: ((error: String?, isStored: Boolean) -> Unit)?) -> Unit

    /**
     * A method that receives a value from the device's secure storage using the specified `key`. In case of an
     * error, `error` will contain the error. In case of success, `error` will be `null` and the value will be
     * passed as the second argument. If the key was not found, the value will be `null`, and `canBeStored` will
     * indicate whether the key can be restored from the current device.
     */
    val getItem: (key: String, callback: (error: String?, value: String?, canBeStored: Boolean) -> Unit) -> Unit

    /**
     * Attempts to restore a key that previously existed on the current device. When called, the user will be asked
     * for permission to restore the value. If the user declines or an error occurs, `error` will contain the
     * error. If restored successfully, `error` will be `null` and the value will be passed as the second argument.
     */
    val restoreItem: (key: String, callback: (error: String?, value: String?) -> Unit) -> Unit

    /**
     * A method that removes a value from the device's secure storage using the specified `key`. If an optional
     * `callback` parameter was passed, the `callback` function will be called. In case of an error, `error` will
     * contain the error. In case of success, `error` will be `null` and `isRemoved` will indicate whether the value
     * was removed.
     */
    val removeItem: (key: String, callback: ((error: String?, isRemoved: Boolean) -> Unit)?) -> Unit

    /**
     * A method that clears all keys previously stored by the bot in the device's secure storage. If an optional
     * `callback` parameter was passed, the `callback` function will be called. In case of an error, `error` will
     * contain the error. In case of success, `error` will be `null` and `isCleared` will indicate whether all
     * values were removed.
     */
    val clear: (callback: ((error: String?, isCleared: Boolean) -> Unit)?) -> Unit
}
