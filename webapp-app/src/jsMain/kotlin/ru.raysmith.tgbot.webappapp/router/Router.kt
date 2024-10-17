package ru.raysmith.tgbot.webappapp.router

import js.objects.jso
import react.FC
import react.PropsWithChildren
import react.create
import react.router.RouterProvider
import react.router.dom.createBrowserRouter
import ru.raysmith.tgbot.webappapp.pages.BaseInfoPage
import ru.raysmith.tgbot.webappapp.pages.BiometricalPage
import ru.raysmith.tgbot.webappapp.pages.MainPage
import ru.raysmith.tgbot.webappapp.pages.RootPage

val Router = FC<PropsWithChildren> { props ->
    RouterProvider {
        router = createBrowserRouter(arrayOf(
            jso {
                path = Paths.root
                element = RootPage.create()
                children = arrayOf(
                    jso {
                        index = true
                        element = MainPage.create()
                    },
                    jso {
                        path = Paths.biometrical
                        element = BiometricalPage.create()
                    },
                    jso {
                        path = Paths.baseInfo
                        element = BaseInfoPage.create()
                    }
                )
            }
        ))
        +props
    }

    +props.children
}