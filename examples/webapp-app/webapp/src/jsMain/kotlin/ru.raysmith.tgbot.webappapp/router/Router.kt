package ru.raysmith.tgbot.webappapp.router

import js.objects.jso
import react.*
import react.router.RouterProvider
import react.router.dom.createBrowserRouter
import ru.raysmith.tgbot.hooks.useBackButton
import ru.raysmith.tgbot.webApp
import ru.raysmith.tgbot.webappapp.pages.RootPage
import ru.raysmith.tgbot.webappapp.pages.accelerometer.AccelerometerPage
import ru.raysmith.tgbot.webappapp.pages.backbutton.BackButtonPage
import ru.raysmith.tgbot.webappapp.pages.base.BaseInfoPage
import ru.raysmith.tgbot.webappapp.pages.biometric.BiometricPage
import ru.raysmith.tgbot.webappapp.pages.bottombutton.BottomButtonsPage
import ru.raysmith.tgbot.webappapp.pages.cloud.CloudStoragePage
import ru.raysmith.tgbot.webappapp.pages.gyroscope.GyroscopePage
import ru.raysmith.tgbot.webappapp.pages.haptic.HapticFeedbackPage
import ru.raysmith.tgbot.webappapp.pages.homescreen.HomeScreenIntegrationPage
import ru.raysmith.tgbot.webappapp.pages.location.LocationManagerPage
import ru.raysmith.tgbot.webappapp.pages.main.MainPage
import ru.raysmith.tgbot.webappapp.pages.nativeui.NativeInterfacesPage
import ru.raysmith.tgbot.webappapp.pages.orientation.DeviceOrientationPage
import ru.raysmith.tgbot.webappapp.pages.settingsbutton.SettingsButtonPage
import ru.raysmith.tgbot.webappapp.pages.state.StatePage
import ru.raysmith.tgbot.webappapp.pages.tg.TgInteractionPage
import ru.raysmith.tgbot.webappapp.pages.theme.ThemePage
import ru.raysmith.tgbot.webappapp.pages.viewport.ViewportPage
import web.function.VoidFunction

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
                jso {
                    path = Paths.bottomButtons
                    element = BottomButtonsPage.create()
                },
                jso {
                    path = Paths.cloud
                    element = CloudStoragePage.create()
                },
                jso {
                    path = Paths.accelerometer
                    element = AccelerometerPage.create()
                },
                jso {
                    path = Paths.deviceOrientation
                    element = DeviceOrientationPage.create()
                },
                jso {
                    path = Paths.gyroscope
                    element = GyroscopePage.create()
                },
                jso {
                    path = Paths.location
                    element = LocationManagerPage.create()
                },
                jso {
                    path = Paths.nativeInterfaces
                    element = NativeInterfacesPage.create()
                },
                jso {
                    path = Paths.tg
                    element = TgInteractionPage.create()
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

    useEffectOnce {
        if (webApp.initDataUnsafe.startParam == "info") {
            router.navigate(Paths.baseInfo)
        }
    }

    RouterProvider {
        this.router = router
        +props
    }

    +props.children
}