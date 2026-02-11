package ru.raysmith.tgbot.webappapp.components

import mui.material.Button
import mui.material.ButtonGroup
import mui.material.ButtonGroupProps
import react.FC
import ru.raysmith.tgbot.webappapp.other

external interface ButtonsGroupControlProps : ButtonGroupProps {
    var items: Map<String, () -> Unit>
}

val ButtonsGroupControl = FC<ButtonsGroupControlProps> { props ->
    ButtonGroup {
        +props.other("items")

        props.items.forEach { (label, onClick) ->
            Button {
                +label
                this@Button.onClick = { e ->
                    onClick()
                }
            }
        }
    }
}