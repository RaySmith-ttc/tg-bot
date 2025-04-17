package ru.raysmith.tgbot.webappapp.hooks

import js.objects.Object
import js.objects.jso
import kotlinx.browser.window
import react.useCallback
import react.useState

external interface LocalStorageData<T> {
    var state: T
    var update: (name: String, value: dynamic) -> Unit
    var set: (value: T) -> Unit
    var reset: () -> Unit
}

fun <T : Any?> useLocalStorage(key: String, defaultValue: T): LocalStorageData<T> {
    val (state, setState) = useState(getStorage<T>(key) ?: run {
        setStorage(key, defaultValue)
        defaultValue
    })

    val updateState = useCallback(key) { updateValue: dynamic ->
        setState { prevValue: T ->
            val newObj = Object.assign(jso(), prevValue.asDynamic(), updateValue).asDynamic()

            setStorage(key, newObj)
            newObj
        }
    }

    val update = useCallback(updateState) { name: String, value: dynamic ->
        updateState(jso {
            this[name] = value
        })
    }

    val set = useCallback(updateState) { value: T ->
        updateState(value)
    }

    val reset = useCallback(defaultValue, key) {
        removeStorage(key)
        setState(defaultValue)
    }

    return jso {
        this.state = state
        this.update = update
        this.reset = reset
        this.set = set
    }
}

private fun <T> getStorage(key: String): T? = try {
    window.localStorage.getItem(key)?.let {
        JSON.parse(it)
    }
} catch (e: dynamic) {
    console.error(e)
    null
}

private fun setStorage(key: String, value: dynamic) = try {
    window.localStorage.setItem(key, JSON.stringify(value))
} catch (e: dynamic) {
    console.error(e)
}

private fun removeStorage(key: String) = try {
    window.localStorage.removeItem(key)
} catch (e: dynamic) {
    console.error(e)
}