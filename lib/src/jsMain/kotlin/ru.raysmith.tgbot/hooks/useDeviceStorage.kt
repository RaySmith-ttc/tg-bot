package ru.raysmith.tgbot.hooks

import kotlinx.js.JsPlainObject
import react.useMemo
import react.useState
import ru.raysmith.tgbot.webApp
import seskar.js.JsValue

/**
 *  A state variable that tracks the current state of the hook:
 * - `IDLE`: The hook is ready to use.
 * - `SETTING_ITEM`: Setting an item in device storage.
 * - `GETTING_ITEM`: Fetching an item from device storage.
 * - `REMOVING_ITEM`: Removing an item from device storage.
 * - `CLEARING`: Clearing all items from device storage.
 * */
sealed external interface DeviceStorageHookState {
    companion object {

        /** The hook is ready to use */
        @JsValue("IDLE") val IDLE: DeviceStorageHookState

        /** Setting an item in device storage */
        @JsValue("SETTING_ITEM") val SETTING_ITEM: DeviceStorageHookState

        /** Fetching an item from device storage */
        @JsValue("GETTING_ITEM") val GETTING_ITEM: DeviceStorageHookState

        /** Removing an item from device storage */
        @JsValue("REMOVING_ITEM") val REMOVING_ITEM: DeviceStorageHookState

        /** Clearing all items from device storage */
        @JsValue("CLEARING") val CLEARING: DeviceStorageHookState
    }
}

/**
 * This hook provides controls for the device's persistent local storage ([DeviceStorage][ru.raysmith.tgbot.DeviceStorage]).
 *
 * - `state`: The current state of the hook. See [DeviceStorageHookState]
 * - `lastError`: Error message if the last operation has failed.
 * */
fun useDeviceStorage(): DeviceStorageHookType {
    var state by useState(DeviceStorageHookState.IDLE)
    var lastError by useState<String?>(null)

    val setItem: (key: String, value: String, callback: ((error: String?, isStored: Boolean) -> Unit)?) -> Unit
            = { key, value, callback ->
                state = DeviceStorageHookState.SETTING_ITEM
                webApp.DeviceStorage.setItem(key, value) { error, isStored ->
                    state = DeviceStorageHookState.IDLE
                    lastError = error
                    callback?.invoke(error, isStored)
                }
            }

    val getItem: (key: String, callback: (error: String?, value: String?) -> Unit) -> Unit
            = { key, callback ->
                state = DeviceStorageHookState.GETTING_ITEM
                webApp.DeviceStorage.getItem(key) { error, value ->
                    state = DeviceStorageHookState.IDLE
                    lastError = error
                    callback(error, value)
                }
            }

    val removeItem: (key: String, callback: ((error: String?, isRemoved: Boolean) -> Unit)?) -> Unit
            = { key, callback ->
                state = DeviceStorageHookState.REMOVING_ITEM
                webApp.DeviceStorage.removeItem(key) { error, isRemoved ->
                    state = DeviceStorageHookState.IDLE
                    lastError = error
                    callback?.invoke(error, isRemoved)
                }
            }

    val clear: (callback: ((error: String?, isCleared: Boolean) -> Unit)?) -> Unit
            = { callback ->
                state = DeviceStorageHookState.CLEARING
                webApp.DeviceStorage.clear { error, isCleared ->
                    state = DeviceStorageHookState.IDLE
                    lastError = error
                    callback?.invoke(error, isCleared)
                }
            }

    return useMemo(state, lastError) {
        DeviceStorageHookType(
            state = state,
            lastError = lastError,
            setItem = setItem,
            getItem = getItem,
            removeItem = removeItem,
            clear = clear,
        )
    }
}

@JsPlainObject
external interface DeviceStorageHookType {

    /** A state that tracks the current state of the hook */
    val state: DeviceStorageHookState

    /** An error message if the last operation has failed */
    val lastError: String?

    /**
     * A method that stores a value in the device's local storage using the specified `key`. If an optional
     * `callback` parameter was passed, the `callback` function will be called. In case of an error, `error` will
     * contain the error. In case of success, `error` will be `null` and `isStored` will indicate whether the value
     * was stored.
     */
    val setItem: (key: String, value: String, callback: ((error: String?, isStored: Boolean) -> Unit)?) -> Unit

    /**
     * A method that receives a value from the device's local storage using the specified `key`. In case of an
     * error, `error` will contain the error. In case of success, `error` will be `null` and the value will be
     * passed as the second argument.
     */
    val getItem: (key: String, callback: (error: String?, value: String?) -> Unit) -> Unit

    /**
     * A method that removes a value from the device's local storage using the specified `key`. If an optional
     * `callback` parameter was passed, the `callback` function will be called. In case of an error, `error` will
     * contain the error. In case of success, `error` will be `null` and `isRemoved` will indicate whether the value
     * was removed.
     */
    val removeItem: (key: String, callback: ((error: String?, isRemoved: Boolean) -> Unit)?) -> Unit

    /**
     * A method that clears all keys previously stored by the bot in the device's local storage. If an optional
     * `callback` parameter was passed, the `callback` function will be called. In case of an error, `error` will
     * contain the error. In case of success, `error` will be `null` and `isCleared` will indicate whether all
     * values were removed.
     */
    val clear: (callback: ((error: String?, isCleared: Boolean) -> Unit)?) -> Unit
}
