package ru.raysmith.tgbot.webappapp.components

import mui.material.Box
import mui.material.CircularProgress
import mui.material.CircularProgressProps
import mui.system.sx
import react.FC
import ru.raysmith.tgbot.hooks.useViewport
import web.cssom.*


// TODO fix markup
val FullPageLoading = FC<CircularProgressProps> { props ->
    val vp = useViewport()

    Box {
        sx {
            height = vp.viewportHeight.px
            display = Display.flex
            justifyContent = JustifyContent.center
            alignItems = AlignItems.center
        }

        CircularProgress {
            +props
        }
    }
}