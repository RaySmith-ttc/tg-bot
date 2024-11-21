package ru.raysmith.tgbot.webappapp.pages

import mui.material.Box
import mui.system.sx
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.router.Outlet
import web.cssom.GridArea
import web.cssom.px

val RootPage = FC<Props> {

    Box {
        sx {
            gridArea = "content".unsafeCast<GridArea>()
            padding = 16.px
            paddingBottom = 80.px
        }

        component = ReactHTML.main

        Outlet {

        }
    }
}