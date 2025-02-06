package ru.raysmith.tgbot.webappapp.router

import js.objects.jso
import react.*
import react.router.RouterProvider
import react.router.dom.createBrowserRouter
import ru.raysmith.tgbot.hooks.useBackButton
import ru.raysmith.tgbot.webappapp.pages.base.BaseInfoPage
import ru.raysmith.tgbot.webappapp.pages.biometric.BiometricPage
import ru.raysmith.tgbot.webappapp.pages.main.MainPage
import ru.raysmith.tgbot.webappapp.pages.RootPage
import ru.raysmith.tgbot.webappapp.pages.backbutton.BackButtonPage
import ru.raysmith.tgbot.webappapp.pages.bottombutton.BottomButtonsPage
import ru.raysmith.tgbot.webappapp.pages.haptic.HapticFeedbackPage
import ru.raysmith.tgbot.webappapp.pages.homescreen.HomeScreenIntegrationPage
import ru.raysmith.tgbot.webappapp.pages.settingsbutton.SettingsButtonPage
import ru.raysmith.tgbot.webappapp.pages.state.StatePage
import ru.raysmith.tgbot.webappapp.pages.theme.ThemePage
import ru.raysmith.tgbot.webappapp.pages.viewport.ViewportPage
import web.scheduling.VoidFunction

val Router = FC<PropsWithChildren> { props ->
    val backButton = useBackButton()
    var isBackButtonDefaultOnClickEnabled by useState(true)

    val router = createBrowserRouter(arrayOf(
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
                jso {
                    path = Paths.state
                    element = StatePage.create()
                },
                jso {
                    path = Paths.homeSceenIntegration
                    element = HomeScreenIntegrationPage.create()
                },
                jso {
                    path = Paths.backButton
                    element = BackButtonPage.create {
                        setIsBackButtonDefaultOnClickEnabled = { value: Boolean ->
                            isBackButtonDefaultOnClickEnabled = value
                        }
                    }
                },
                jso {
                    path = Paths.settingsButton
                    element = SettingsButtonPage.create()
                },
            )
        }
    ))
    val backButtonOnClick: VoidFunction = useCallback {
        router.navigate(-1)
    }

    useEffect(isBackButtonDefaultOnClickEnabled) {
        if (isBackButtonDefaultOnClickEnabled) {
            backButton.onClick(backButtonOnClick)
        } else {
            backButton.offClick(backButtonOnClick)
        }
    }

    RouterProvider {
        this.router = router
        +props
    }

    +props.children
}