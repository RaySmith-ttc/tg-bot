package ru.raysmith.tgbot.webappapp.components

import mui.material.Box
import mui.material.CircularProgress
import mui.material.CircularProgressProps
import mui.system.sx
import react.FC
import ru.raysmith.tgbot.CssVar
import web.cssom.AlignItems
import web.cssom.Display
import web.cssom.JustifyContent

val FullPageLoading = FC<CircularProgressProps> { props ->
    Box {
        sx {
            height = CssVar.tgViewportHeight()
            display = Display.flex
            justifyContent = JustifyContent.center
            alignItems = AlignItems.center
        }

        CircularProgress {
            +props
        }
    }
}