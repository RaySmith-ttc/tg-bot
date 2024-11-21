package ru.raysmith.tgbot.webappapp.pages.theme

import emotion.react.css
import mui.material.Paper
import mui.system.Box
import mui.system.sx
import react.FC
import react.Props
import react.dom.html.ReactHTML
import web.cssom.BackgroundColor
import web.cssom.px

external interface ColorBlockProps : Props {
    var backgroundColor: BackgroundColor?
}

val ColorBlock = FC<ColorBlockProps> { props ->
    Paper {
        elevation = if (props.backgroundColor == null) 0 else 4
        Box {
            sx {
                backgroundColor = props.backgroundColor
                width = 20.px
                height = 20.px
            }
        }
    }
}