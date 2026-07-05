package ru.raysmith.tgbot.webappapp.pages.securestorage

import mui.material.Alert
import mui.material.AlertColor
import mui.material.Button
import mui.material.StackDirection
import mui.material.TextField
import mui.material.Typography
import mui.system.responsive
import mui.system.sx
import react.FC
import react.ReactNode
import react.create
import react.dom.onChange
import react.useState
import ru.raysmith.tgbot.hooks.useSecureStorage
import ru.raysmith.tgbot.webappapp.components.ControlsPaperStack
import ru.raysmith.tgbot.webappapp.components.applyControlButtonStyle
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayCheckbox
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTable
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTableRow
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTextField
import ru.raysmith.tgbot.webappapp.pages.BaseSubPageLayout
import web.cssom.pct

val SecureStoragePage = FC {
    val secureStorage = useSecureStorage()

    var key by useState("mySecureKey")
    var value by useState("mySecureValue")
    var lastValue by useState<String?>(null)
    var canBeStored by useState(false)
    var lastOperation by useState<String?>(null)

    BaseSubPageLayout {
        title = "Secure storage"

        DataDisplayTable {
            DataDisplayTableRow {
                title = "state"
                this.value = Typography.create { +secureStorage.state.toString() }
            }
            DataDisplayTableRow {
                title = "lastError"
                this.value = Typography.create { +(secureStorage.lastError ?: "-") }
            }
            DataDisplayTableRow {
                title = "value (getItem)"
                this.value = DataDisplayTextField.create { this.value = lastValue }
            }
            DataDisplayTableRow {
                title = "canBeStored"
                this.value = DataDisplayCheckbox.create { checked = canBeStored }
            }
        }

        if (lastOperation != null) {
            Alert {
                +lastOperation!!
                severity = AlertColor.info.unsafeCast<String>()
                sx { width = 100.pct }
            }
        }

        ControlsPaperStack {
            title = "Item controls"
            direction = responsive(StackDirection.column)

            TextField {
                label = ReactNode("key")
                this.value = key
                onChange = { event -> key = event.target.asDynamic().value.unsafeCast<String>() }
            }

            TextField {
                label = ReactNode("value")
                this.value = value
                onChange = { event -> value = event.target.asDynamic().value.unsafeCast<String>() }
            }

            Button {
                +"Set item"
                applyControlButtonStyle()
                disabled = key.isEmpty()
                onClick = {
                    secureStorage.setItem(key, value) { error, isStored ->
                        lastOperation = if (error != null) "setItem error: $error" else "setItem: isStored = $isStored"
                    }
                }
            }

            Button {
                +"Get item"
                applyControlButtonStyle()
                disabled = key.isEmpty()
                onClick = {
                    secureStorage.getItem(key) { error, storedValue, storedCanBeStored ->
                        lastValue = storedValue
                        canBeStored = storedCanBeStored
                        lastOperation = if (error != null) "getItem error: $error" else "getItem: value = $storedValue, canBeStored = $storedCanBeStored"
                    }
                }
            }

            Button {
                +"Restore item"
                applyControlButtonStyle()
                disabled = key.isEmpty()
                onClick = {
                    secureStorage.restoreItem(key) { error, restoredValue ->
                        lastValue = restoredValue
                        lastOperation = if (error != null) "restoreItem error: $error" else "restoreItem: value = $restoredValue"
                    }
                }
            }

            Button {
                +"Remove item"
                applyControlButtonStyle()
                disabled = key.isEmpty()
                onClick = {
                    secureStorage.removeItem(key) { error, isRemoved ->
                        lastOperation = if (error != null) "removeItem error: $error" else "removeItem: isRemoved = $isRemoved"
                    }
                }
            }

            Button {
                +"Clear all"
                applyControlButtonStyle()
                onClick = {
                    secureStorage.clear { error, isCleared ->
                        lastValue = null
                        lastOperation = if (error != null) "clear error: $error" else "clear: isCleared = $isCleared"
                    }
                }
            }
        }
    }
}
