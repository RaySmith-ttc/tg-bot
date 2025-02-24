package ru.raysmith.tgbot.webappapp.components.datadisplay

import js.objects.jso
import kotlinx.coroutines.launch
import mui.icons.material.ContentCopy
import mui.material.*
import react.FC
import react.ReactNode
import react.create
import react.useState
import ru.raysmith.tgbot.webappapp.isNullOrUndefined
import ru.raysmith.tgbot.webappapp.mainScope
import ru.raysmith.tgbot.webappapp.other
import ru.raysmith.tgbot.webappapp.wrappers.InputProps
import ru.raysmith.tgbot.webappapp.wrappers.shrink
import web.navigator.navigator

external interface DataDisplayTextFieldProps : TextFieldProps {
    var copyToClipboard: Boolean
}

val DataDisplayTextField = FC<DataDisplayTextFieldProps> { props ->
    var snackbar by useState(false)

    TextField {
        variant = FormControlVariant.outlined
        disabled = props.value == null
        onFocus = { event -> event.target.blur() }
        focused = false
        InputProps = jso {
            readOnly = true
            shrink = true
            if (props.copyToClipboard) {
                endAdornment = InputAdornment.create {
                    position = InputAdornmentPosition.end
                    IconButton {
                        disabled = props.value.isNullOrUndefined() || props.value.toString().isEmpty()
                        onClick = {
                            mainScope.launch {
                                navigator.clipboard.writeText(props.value.toString())
                            }
                            snackbar = true
                        }

                        ContentCopy {}
                    }
                }
            }
        }

        +props.other("copyToClipboard")
    }

    Snackbar {
        open = snackbar
        onClose = { _, _ -> snackbar = false }
        message = ReactNode("Text copied to clipboard")
        onClick = { _ -> snackbar = false }
        autoHideDuration = 3000
    }
}