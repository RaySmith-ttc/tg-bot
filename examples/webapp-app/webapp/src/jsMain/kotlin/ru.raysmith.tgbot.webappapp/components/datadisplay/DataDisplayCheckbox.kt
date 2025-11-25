package ru.raysmith.tgbot.webappapp.components.datadisplay

import mui.material.Checkbox
import mui.material.CheckboxColor
import mui.material.CheckboxProps
import mui.material.FormControlLabel
import mui.material.styles.Theme
import mui.material.styles.useTheme
import mui.system.sx
import react.FC
import react.ReactNode
import react.create
import ru.raysmith.tgbot.webappapp.other
import ru.raysmith.tgbot.webappapp.wrappers.ml
import ru.raysmith.tgbot.webappapp.wrappers.pl
import ru.raysmith.tgbot.webappapp.wrappers.pr
import web.cssom.important
import web.cssom.px

external interface DataDisplayCheckboxProps : CheckboxProps {
    var label: String?
}

val DataDisplayCheckbox = FC<DataDisplayCheckboxProps> { props ->
    val theme = useTheme<Theme>()

    val checkbox = Checkbox.create {
        disableRipple = true
        onChange = { _, _ -> }
        sx {
            color = important(theme.palette.text.secondary)
            pl = 0.px

            +props.sx
        }


        +props.other("label", "sx")
    }

    val label = props.label
    if (label != null) {
        FormControlLabel {
            control = checkbox
            this.label = ReactNode(label)
        }
    } else {
        +checkbox
    }
}