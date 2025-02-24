package ru.raysmith.tgbot.webappapp.components

import mui.material.Size
import mui.material.ToggleButton
import mui.material.ToggleButtonGroup
import mui.material.ToggleButtonGroupProps
import react.FC
import ru.raysmith.tgbot.webappapp.other

external interface ToggleButtonsGroupControlProps : ToggleButtonGroupProps {

    /**
     * The items of the group.
     */
    var items: Map<Pair<String, Any>, () -> Unit>
}

val ToggleButtonsGroupControl = FC<ToggleButtonsGroupControlProps> { props ->
    ToggleButtonGroup {
        +props.other("items", "debug", "onChange")

        exclusive = props.exclusive ?: true
        size = props.size ?: Size.small
        onChange = { _, value ->
            val key = props.items.keys.firstOrNull { it.second == value }
            props.items[key]?.invoke()
        }

        if (props.items != undefined) {
            props.items.forEach { (data, _) ->
                ToggleButton {
                    value = data.second
                    +data.first
                }
            }
        }
    }
}