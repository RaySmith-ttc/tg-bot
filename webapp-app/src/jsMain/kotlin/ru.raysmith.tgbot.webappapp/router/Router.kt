package ru.raysmith.tgbot.webappapp.router

import js.objects.jso
import react.FC
import react.PropsWithChildren
import react.create
import react.router.RouterProvider
import react.router.dom.createBrowserRouter
import react.useEffectOnce
import ru.raysmith.tgbot.hooks.useBackButton
import ru.raysmith.tgbot.webappapp.pages.base.BaseInfoPage
import ru.raysmith.tgbot.webappapp.pages.biometric.BiometricPage
import ru.raysmith.tgbot.webappapp.pages.main.MainPage
import ru.raysmith.tgbot.webappapp.pages.RootPage
import ru.raysmith.tgbot.webappapp.pages.haptic.HapticFeedbackPage
import ru.raysmith.tgbot.webappapp.pages.theme.ThemePage
import ru.raysmith.tgbot.webappapp.pages.viewport.ViewportPage

val Router = FC<PropsWithChildren> { props ->
    val backButton = useBackButton()

    RouterProvider {

        val r = createBrowserRouter(arrayOf(
            jso {
                path = Paths.root
                element = RootPage.create()
                children = arrayOf(
                    jso {
                        index = true
                        element = MainPage.create()
                    },
                    jso {
                        path = Paths.baseInfo
                        element = BaseInfoPage.create()
                    },
                    jso {
                        path = Paths.viewport
                        element = ViewportPage.create()
                    },
                    jso {
                        path = Paths.biometric
                        element = BiometricPage.create()
                    },
                    jso {
                        path = Paths.hapticFeedback
                        element = HapticFeedbackPage.create()
                    },
                    jso {
                        path = Paths.theme
                        element = ThemePage.create()
                    },
                )
            }
        ))

        router = r

        useEffectOnce {
            backButton.onClick {
                r.navigate(-1)
            }
        }
        +props
    }

    +props.children
}