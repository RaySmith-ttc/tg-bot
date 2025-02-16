package ru.raysmith.tgbot.webappapp.pages

import mui.material.Box
import mui.material.styles.Theme
import mui.material.styles.useTheme
import mui.system.sx
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.router.Outlet
import ru.raysmith.tgbot.CssVar
import ru.raysmith.tgbot.webappapp.wrappers.mt
import ru.raysmith.tgbot.webappapp.wrappers.pb
import ru.raysmith.tgbot.webappapp.wrappers.px
import web.cssom.GridArea

val RootPage = FC<Props> {
    val theme = useTheme<Theme>()

    Box {
        sx {
            gridArea = "content".unsafeCast<GridArea>()
            pb = theme.spacing(2)
            px = theme.spacing(2)
            mt = "calc(${CssVar.tgContentSafeAreaInsetTop<dynamic>()} + ${CssVar.tgSafeAreaInsetTop<dynamic>()} + ${theme.spacing(2)})"
        }

        component = ReactHTML.main

        Outlet {

        }
    }
}