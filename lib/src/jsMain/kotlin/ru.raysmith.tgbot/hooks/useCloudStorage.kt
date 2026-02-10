package ru.raysmith.tgbot.hooks

import js.array.Tuple2
import js.array.component1
import js.array.component2
import js.collections.JsMap
import js.objects.Object
import kotlinx.js.JsPlainObject
import react.*
import ru.raysmith.tgbot.BuiltinImplementation
import ru.raysmith.tgbot.webApp
import seskar.js.JsValue
import kotlin.js.collections.JsReadonlyMap
import kotlin.js.collections.toMap

/**
 *  A state variable that tracks the current state of the hook:
 * - `GETTING_ITEMS`: Fetching items from cloud storage.
 * - `GETTING_ITEMS_ERROR`: Error while fetching items.
 * - `READY`: The hook is ready to use.
 * - `SETTING_ITEM`: Setting an item in cloud storage.
 * - `GETTING_ITEM`: Fetching an item from cloud storage.
 * - `REMOVING_ITEM`: Removing an item from cloud storage.
 * */
sealed external interface CloudStorageHookState {
    companion object {

        /** Fetching items from cloud storage */
        @JsValue("GETTING_ITEMS") val GETTING_ITEMS: CloudStorageHookState

        /** Error while fetching items */
        @JsValue("GETTING_ITEMS_ERROR") val GETTING_ITEMS_ERROR: CloudStorageHookState

        /** The hook is ready to use */
        @JsValue("READY") val READY: CloudStorageHookState

        /** Setting an item in cloud storage */
        @JsValue("SETTING_ITEM") val SETTING_ITEM: CloudStorageHookState

        /** Fetching an item from cloud storage */
        @JsValue("GETTING_ITEM") val GETTING_ITEM: CloudStorageHookState

        /** Removing an item from cloud storage */
        @JsValue("REMOVING_ITEM") val REMOVING_ITEM: CloudStorageHookState
    }
}

fun CloudStorageHookState.isUpdatingItems() = this == CloudStorageHookState.READY

/**
 * This hook provides controls for cloud storage.
 * The hook holds items in cache use [reducer][useReducer], synchronizes them with the cloud storage and provide state.
 *
 * - `state`: The current state of the hook. See [CloudStorageHookState]
 * - `loadingError`: Error message if an error occurs while loading items from cloud storage.
 * - `items`: Items fetched from cloud storage as a map.
 * */
@OptIn(ExperimentalWasmJsInterop::class)
@BuiltinImplementation
fun useCloudStorage(): CloudStorageHookType {
    var state by useState(CloudStorageHookState.GETTING_ITEMS)
    var fetchingError by useState<String?>(null)

    val (items, dispatch) = useReducer<JsMap<String, String>, Action>({ reducerState, action ->
        when (action.action) {
            ActionType.ADD -> JsMap(reducerState.unsafeCast<JsArray<Tuple2<String, String>>>()).apply { set(action.key!!, action.value!!) } // Создаём новый объект
            ActionType.REMOVE -> JsMap(reducerState.unsafeCast<JsArray<Tuple2<String, String>>>()).apply { delete(action.key!!) }
            ActionType.ADD_ALL -> JsMap(reducerState.unsafeCast<JsArray<Tuple2<String, String>>>()).apply { action.valuesToAdd!!.forEach { value, key -> set(key, value) } }
            ActionType.REMOVE_ALL -> JsMap(reducerState.unsafeCast<JsArray<Tuple2<String, String>>>()).apply { action.keysToRemove!!.forEach { delete(it) } }
            else -> reducerState
        }
    }, JsMap())

    useEffectOnce {
        webApp.CloudStorage.getKeys { errorGetKeys, keys ->
            if (errorGetKeys != null) {
                fetchingError = errorGetKeys
                state = CloudStorageHookState.GETTING_ITEMS_ERROR
            } else {
                webApp.CloudStorage.getItems(keys) { errorGetItems, values ->
                    if (errorGetItems != null) {
                        fetchingError = errorGetItems
                        state = CloudStorageHookState.GETTING_ITEMS_ERROR
                    }
                    if (values != null) {
                        state = CloudStorageHookState.READY
                        dispatch(ru.raysmith.tgbot.hooks.Action(
                            action = ActionType.ADD_ALL,
                            valuesToAdd = JsMap(Object.entries(values)),
                        ))
                    }
                }
            }
        }
    }

    val setItem: (key: String, value: String, callback: (error: String?, isStored: Boolean) -> Unit) -> Unit
            = { key, value, callback ->
                state = CloudStorageHookState.SETTING_ITEM

                webApp.CloudStorage.setItem(key, value) { error, isStored ->
                    state = CloudStorageHookState.READY
                    if (isStored) {
                        dispatch(ru.raysmith.tgbot.hooks.Action(
                            action = ActionType.ADD,
                            key = key,
                            value = value,
                        ))
                    }
                    callback(error, isStored)
                }
            }

    val getItem: (key: String) -> String? = useCallback(items) { key ->
        items.get(key)
    }

    val removeItem: (key: String, callback: ((error: String?, isRemoved: Boolean) -> Unit)?) -> Unit = useCallback(items) { key, callback ->
        state = CloudStorageHookState.REMOVING_ITEM
        webApp.CloudStorage.removeItem(key) { error, isRemoved ->
            state = CloudStorageHookState.READY
            if (isRemoved) {
                dispatch(ru.raysmith.tgbot.hooks.Action(
                    action = ActionType.REMOVE,
                    key = key,
                ))
            }
            callback?.invoke(error, isRemoved)
        }
    }

    val removeItems: (keys: Iterable<String>, callback: ((error: String?, isRemoved: Boolean) -> Unit)?) -> Unit = useCallback(items) { keys, callback ->
        state = CloudStorageHookState.REMOVING_ITEM

        val arrayKeys = if (js("Array.isArray(keys)") as Boolean) {
            keys.unsafeCast<Array<String>>()
        } else {
            keys.toList().toTypedArray()
        }

        webApp.CloudStorage.removeItems(arrayKeys) { error, isRemoved ->
            state = CloudStorageHookState.READY
            if (isRemoved) {
                dispatch(ru.raysmith.tgbot.hooks.Action(
                    action = ActionType.REMOVE_ALL,
                    keysToRemove = arrayKeys,
                ))
            }
            callback?.invoke(error, isRemoved)
        }
    }

    return useMemo(state, items, fetchingError) {
        CloudStorageHookType(
            state = state,
            items = (items.unsafeCast<JsReadonlyMap<String, String>>()).toMap(),
            fetchingError = fetchingError,
            setItem = setItem,
            getItem = getItem,
            removeItem = removeItem,
            removeItems = removeItems,
        )
    }
}

@JsPlainObject
external interface CloudStorageHookType {

    /** A state that tracks the current state of the hook */
    val state: CloudStorageHookState

    /** A map that holds the items fetched from cloud storage */
    val items: Map<String, String>

    /** An error message if an error occurs while fetching items from cloud storage */
    val fetchingError: String?

    /**
     * A method that stores a value in the cloud storage using the specified `key`.
     * The key should contain 1-128 characters, only `A-Z`, `a-z`, `0-9`, `_` and `-` are allowed.
     * The value should contain 0-4096 characters. You can store up to 1024 keys in the cloud storage.
     * If an optional `callback` parameter was passed, the `callback` function will be called.
     * In case of an error, `error` will contain the error.
     * In case of success, `error` will be `null` and `isStored`  will be indicated whether the value was stored.
     */
    val setItem: (key: String, value: String, callback: (error: String?, isStored: Boolean) -> Unit) -> Unit

    /**
     * A method that returns a value from the cloud storage using the specified `key`.
     * The `key` should contain 1-128 characters, only `A-Z`, `a-z`, `0-9`, `_` and `-` are allowed.
     */
    val getItem: (key: String) -> String?

    /**
     * A method that removes a value from the cloud storage using the specified `key`.
     * The `key` should contain 1-128 characters, only `A-Z`, `a-z`, `0-9`, `_` and `-` are allowed.
     * If an optional `callback` parameter was passed, the `callback` function will be called.
     * In case of an error, `error` will contain the error.
     * In case of success, `error` will be `null` and `isRemoved` will be indicated whether the value was removed.
     */
    val removeItem: (key: String, callback: (error: String?, isRemoved: Boolean) -> Unit) -> Unit

    /**
     * A method that removes values from the cloud storage using the specified `keys`.
     * The `keys` should contain 1-128 characters, only `A-Z`, `a-z`, `0-9`, `_` and `-` are allowed.
     * If an optional `callback` parameter was passed, the `callback` function will be called.
     * In case of an error, `error` will contain the error.
     * In case of success, `error` will be `null` and  `isRemoved` will be indicated whether the values were removed.
     */
    val removeItems: (keys: Iterable<String>, callback: ((error: String?, isRemoved: Boolean) -> Unit)?) -> Unit
}

// ---------------------------------------------------- Internal -------------------------------------------------------

@Suppress("NESTED_CLASS_IN_EXTERNAL_INTERFACE")
private sealed external interface ActionType {
    companion object {
        @JsValue("ADD") val ADD: ActionType
        @JsValue("REMOVE") val REMOVE: ActionType
        @JsValue("ADD_ALL") val ADD_ALL: ActionType
        @JsValue("REMOVE_ALL") val REMOVE_ALL: ActionType
    }
}

@JsPlainObject
private external interface Action {
    val action: ActionType
    val key: String?
    val value: String?
    val keysToRemove: Array<String>?
    val valuesToAdd: JsMap<String, String>?
}
