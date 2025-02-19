package ru.raysmith.tgbot.webappapp.components

import mui.material.Paper
import mui.material.PaperProps
import mui.material.styles.Theme
import mui.material.styles.useTheme
import mui.system.sx
import react.FC

val ControlsPaper = FC<PaperProps> { props ->
    val theme = useTheme<Theme>()

    Paper {
        sx { padding = theme.spacing(2) }
        +props
    }
}