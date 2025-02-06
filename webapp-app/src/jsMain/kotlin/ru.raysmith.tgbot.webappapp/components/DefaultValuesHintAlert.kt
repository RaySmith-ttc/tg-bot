package ru.raysmith.tgbot.webappapp.components

import mui.material.Alert
import mui.material.AlertProps
import react.FC

val DefaultValuesHintAlert = FC<AlertProps> { props ->
    Alert {
        +"To restore the default values, reload the page."
        severity = mui.material.AlertColor.info.unsafeCast<String>()
        +props
    }
}