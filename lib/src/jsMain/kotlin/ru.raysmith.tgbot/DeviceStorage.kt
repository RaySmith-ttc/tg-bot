package ru.raysmith.tgbot

import kotlinx.js.JsPlainObject

/**
 * This object provides access to persistent local storage on the user’s device. It is conceptually similar to the
 * browser's `localStorage` API, but integrated within the Telegram client. All data is stored locally and is available
 * only to the bot that created it. Each bot can store up to **5 MB per user** using this storage.
 */
@JsPlainObject
external interface DeviceStorage {

    /**
     * A method that stores a value in the device's local storage using the specified key. If an optional `callback`
     * parameter was passed, the `callback` function will be called. In case of an error, the first argument will
     * contain the error. In case of success, the first argument will be `null` and the second argument will be a
     * boolean indicating whether the value was stored.
     *
     * @since Bot API 9.0+
     * */
    val setItem: (key: String, value: String, callback: ((error: String?, isStored: Boolean) -> Unit)?) -> DeviceStorage

    /**
     * A method that receives a value from the device's local storage using the specified key. In case of an error, the
     * `callback` function will be called and the first argument will contain the error. In case of success, the first
     * argument will be `null` and the value will be passed as the second argument.
     *
     * @since Bot API 9.0+
     * */
    val getItem: (key: String, callback: (error: String?, value: String?) -> Unit) -> DeviceStorage

    /**
     * A method that removes a value from the device's local storage using the specified key. If an optional `callback`
     * parameter was passed, the `callback` function will be called. In case of an error, the first argument will
     * contain the error. In case of success, the first argument will be `null` and the second argument will be a
     * boolean indicating whether the value was removed.
     *
     * @since Bot API 9.0+
     * */
    val removeItem: (key: String, callback: ((error: String?, isRemoved: Boolean) -> Unit)?) -> DeviceStorage

    /**
     * A method that clears all keys previously stored by the bot in the device's local storage. If an optional
     * `callback` parameter was passed, the `callback` function will be called. In case of an error, the first argument
     * will contain the error. In case of success, the first argument will be null and the second argument will be a
     * boolean indicating whether all values were removed.
     *
     * @since Bot API 9.0+
     * */
    var clear: (callback: ((error: String?, isCleared: Boolean) -> Unit)?) -> DeviceStorage
}
