package ru.raysmith.tgbot.webappapp.components

import mui.material.Alert
import mui.material.AlertProps
import mui.system.sx
import react.FC
import web.cssom.pct

val DefaultValuesHintAlert = FC<AlertProps> { props ->
    Alert {
        +"To restore the default values, reload the page."
        severity = mui.material.AlertColor.info.unsafeCast<String>()
        sx {
            width = 100.pct
        }
        +props
    }
}