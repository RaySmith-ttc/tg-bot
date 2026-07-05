package ru.raysmith.tgbot.webappapp.components

import mui.material.Box
import mui.material.CircularProgress
import mui.material.CircularProgressProps
import mui.system.sx
import react.FC
import ru.raysmith.tgbot.webappapp.wrappers.mt
import web.cssom.AlignItems
import web.cssom.Display
import web.cssom.JustifyContent

val Loading = FC<CircularProgressProps> { props ->
    Box {
        sx {
            display = Display.flex
            justifyContent = JustifyContent.center
            alignItems = AlignItems.center
        }

        CircularProgress {
            sx {
                mt = 2
            }
            +props
        }
    }
}