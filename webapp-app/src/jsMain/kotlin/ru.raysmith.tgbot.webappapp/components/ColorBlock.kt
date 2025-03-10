package ru.raysmith.tgbot.webappapp.components

import mui.material.Paper
import mui.system.Box
import mui.system.sx
import react.FC
import react.Props
import web.cssom.BackgroundColor
import web.cssom.pct
import web.cssom.px

external interface ColorBlockProps : Props {
    var backgroundColor: BackgroundColor?
}

val ColorBlock = FC<ColorBlockProps> { props ->
    Paper {
        elevation = if (props.backgroundColor == null) 0 else 4
        sx {
            width = 20.px
            height = 20.px
        }
        Box {
            sx {
                backgroundColor = props.backgroundColor
                height = 100.pct
            }
        }
    }
}