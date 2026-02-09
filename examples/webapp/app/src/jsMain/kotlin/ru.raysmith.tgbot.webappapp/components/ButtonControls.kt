package ru.raysmith.tgbot.webappapp.components

import mui.material.Button
import mui.material.ButtonColor
import mui.material.ButtonVariant
import mui.system.sx
import react.FC
import react.Props
import web.cssom.pct
import web.function.VoidFunction

external interface ButtonControlsProps : Props {
    var value: Boolean
    var onStart: VoidFunction
    var onStop: VoidFunction
}

val ButtonsControl = FC<ButtonControlsProps> { props ->
    Button {
        sx { width = 100.pct }
        variant = ButtonVariant.contained
        color = ButtonColor.success
        disabled = props.value
        onClick = {
            props.onStart()
        }
        +"Start"
    }

    Button {
        sx { width = 100.pct }
        variant = ButtonVariant.contained
        color = ButtonColor.error
        disabled = !props.value
        onClick = {
            props.onStop()
        }
        +"Stop"
    }
}