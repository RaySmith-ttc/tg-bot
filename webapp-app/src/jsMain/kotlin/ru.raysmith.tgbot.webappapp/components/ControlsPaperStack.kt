package ru.raysmith.tgbot.webappapp.components

import mui.material.*
import mui.material.styles.Theme
import mui.material.styles.TypographyVariant
import mui.material.styles.useTheme
import mui.system.responsive
import mui.system.sx
import react.FC
import react.ReactNode
import ru.raysmith.tgbot.webappapp.other
import ru.raysmith.tgbot.webappapp.wrappers.mb
import web.cssom.pct

external interface ControlsPaperStackProps : StackProps {
    var header: ReactNode?
}

val ControlsPaperStack = FC<ControlsPaperStackProps> { props ->
    val theme = useTheme<Theme>()

    Paper {
        sx {
            padding = theme.spacing(2)
            width = 100.pct
        }

        if (props.title != null) {
            Typography {
                variant = TypographyVariant.h6
                +props.title
                sx { mb = theme.spacing(2) }
            }
        }

        +props.header

        Stack {
            direction = responsive(StackDirection.row)
            spacing = responsive(1)
            sx {
                width = 100.pct
                +props.sx
            }

            +props.other("header", "title")
        }
    }
}

fun ButtonProps.applyControlButtonStyle() {
    fullWidth = true
    size = Size.large
}