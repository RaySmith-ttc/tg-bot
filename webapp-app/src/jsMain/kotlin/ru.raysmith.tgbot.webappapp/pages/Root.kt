package ru.raysmith.tgbot.webappapp.pages

import mui.material.Box
import mui.system.sx
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.router.Outlet
import ru.raysmith.tgbot.CssVar
import ru.raysmith.tgbot.hooks.useInsets
import ru.raysmith.tgbot.webappapp.wrappers.mt
import web.cssom.GridArea
import web.cssom.px

val RootPage = FC<Props> {
    Box {
        sx {
            gridArea = "content".unsafeCast<GridArea>()
            padding = 16.px
            paddingBottom = 80.px
            mt = "calc(${CssVar.tgContentSafeAreaInsetTop<dynamic>()} + ${CssVar.tgSafeAreaInsetTop<dynamic>()})"
        }

        component = ReactHTML.main

        Outlet {

        }
    }
}