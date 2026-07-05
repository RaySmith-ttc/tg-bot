package ru.raysmith.tgbot.webappapp.pages.devicestorage

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
import ru.raysmith.tgbot.hooks.useDeviceStorage
import ru.raysmith.tgbot.webappapp.components.ControlsPaperStack
import ru.raysmith.tgbot.webappapp.components.applyControlButtonStyle
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTable
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTableRow
import ru.raysmith.tgbot.webappapp.components.datadisplay.DataDisplayTextField
import ru.raysmith.tgbot.webappapp.pages.BaseSubPageLayout
import web.cssom.pct

val DeviceStoragePage = FC {
    val deviceStorage = useDeviceStorage()

    var key by useState("myKey")
    var value by useState("myValue")
    var lastValue by useState<String?>(null)
    var lastOperation by useState<String?>(null)

    BaseSubPageLayout {
        title = "Device storage"

        DataDisplayTable {
            DataDisplayTableRow {
                title = "state"
                this.value = Typography.create { +deviceStorage.state.toString() }
            }
            DataDisplayTableRow {
                title = "lastError"
                this.value = Typography.create { +(deviceStorage.lastError ?: "-") }
            }
            DataDisplayTableRow {
                title = "value (getItem)"
                this.value = DataDisplayTextField.create { this.value = lastValue }
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
                    deviceStorage.setItem(key, value) { error, isStored ->
                        lastOperation = if (error != null) "setItem error: $error" else "setItem: isStored = $isStored"
                    }
                }
            }

            Button {
                +"Get item"
                applyControlButtonStyle()
                disabled = key.isEmpty()
                onClick = {
                    deviceStorage.getItem(key) { error, storedValue ->
                        lastValue = storedValue
                        lastOperation = if (error != null) "getItem error: $error" else "getItem: value = $storedValue"
                    }
                }
            }

            Button {
                +"Remove item"
                applyControlButtonStyle()
                disabled = key.isEmpty()
                onClick = {
                    deviceStorage.removeItem(key) { error, isRemoved ->
                        lastOperation = if (error != null) "removeItem error: $error" else "removeItem: isRemoved = $isRemoved"
                    }
                }
            }

            Button {
                +"Clear all"
                applyControlButtonStyle()
                onClick = {
                    deviceStorage.clear { error, isCleared ->
                        lastValue = null
                        lastOperation = if (error != null) "clear error: $error" else "clear: isCleared = $isCleared"
                    }
                }
            }
        }
    }
}
