package ru.raysmith.tgbot.webappapp.router

import react.*
import react.use.useConstant
import ru.raysmith.tgbot.hooks.useBackButton
import ru.raysmith.tgbot.webApp
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
import tanstack.react.router.RouteOptions
import tanstack.react.router.RouterOptions
import tanstack.react.router.RouterProvider
import tanstack.react.router.createRootRoute
import tanstack.react.router.createRoute
import tanstack.react.router.createRouter
import tanstack.router.core.RoutePath
import web.function.VoidFunction

val Router = FC<PropsWithChildren> { props ->
    val backButton = useBackButton()
    var isBackButtonDefaultOnClickEnabled by useState(true)

    val rootRoute = createRootRoute()
    val indexRoute = createRoute(
        options = RouteOptions(
            getParentRoute = { rootRoute },
            path = RoutePath(Paths.root),
            component = MainPage
        )
    )

    val baseInfoPageRoute = createRoute(
        options = RouteOptions(
            getParentRoute = { rootRoute },
            path = RoutePath(Paths.baseInfo),
            component = BaseInfoPage
        )
    )

    val viewportPageRoute = createRoute(
        options = RouteOptions(
            getParentRoute = { rootRoute },
            path = RoutePath(Paths.viewport),
            component = ViewportPage
        )
    )

    val biometricPageRoute = createRoute(
        options = RouteOptions(
            getParentRoute = { rootRoute },
            path = RoutePath(Paths.biometric),
            component = BiometricPage
        )
    )

    val hapticFeedbackPageRoute = createRoute(
        options = RouteOptions(
            getParentRoute = { rootRoute },
            path = RoutePath(Paths.hapticFeedback),
            component = HapticFeedbackPage
        )
    )

    val themePageRoute = createRoute(
        options = RouteOptions(
            getParentRoute = { rootRoute },
            path = RoutePath(Paths.theme),
            component = ThemePage
        )
    )

    val statePageRoute = createRoute(
        options = RouteOptions(
            getParentRoute = { rootRoute },
            path = RoutePath(Paths.state),
            component = StatePage
        )
    )

    val homeScreenIntegrationPageRoute = createRoute(
        options = RouteOptions(
            getParentRoute = { rootRoute },
            path = RoutePath(Paths.homeSceenIntegration),
            component = HomeScreenIntegrationPage
        )
    )

    val backButtonRouteComponent = FC<Props> {
        BackButtonPage {
            setIsBackButtonDefaultOnClickEnabled = { value ->
                isBackButtonDefaultOnClickEnabled = value
            }
        }
    }

    val backButtonPageRoute = createRoute(
        options = RouteOptions(
            getParentRoute = { rootRoute },
            path = RoutePath(Paths.backButton),
            component = backButtonRouteComponent
        )
    )

    val bottomButtonsPageRoute = createRoute(
        options = RouteOptions(
            getParentRoute = { rootRoute },
            path = RoutePath(Paths.bottomButtons),
            component = BottomButtonsPage
        )
    )

    val settingsButtonPageRoute = createRoute(
        options = RouteOptions(
            getParentRoute = { rootRoute },
            path = RoutePath(Paths.settingsButton),
            component = SettingsButtonPage
        )
    )

    val cloudStoragePageRoute = createRoute(
        options = RouteOptions(
            getParentRoute = { rootRoute },
            path = RoutePath(Paths.cloud),
            component = CloudStoragePage
        )
    )

    val accelerometerPageRoute = createRoute(
        options = RouteOptions(
            getParentRoute = { rootRoute },
            path = RoutePath(Paths.accelerometer),
            component = AccelerometerPage
        )
    )

    val deviceOrientationPageRoute = createRoute(
        options = RouteOptions(
            getParentRoute = { rootRoute },
            path = RoutePath(Paths.deviceOrientation),
            component = DeviceOrientationPage
        )
    )

    val gyroscopePageRoute = createRoute(
        options = RouteOptions(
            getParentRoute = { rootRoute },
            path = RoutePath(Paths.gyroscope),
            component = GyroscopePage
        )
    )

    val locationManagerPageRoute = createRoute(
        options = RouteOptions(
            getParentRoute = { rootRoute },
            path = RoutePath(Paths.location),
            component = LocationManagerPage
        )
    )

    val nativeInterfacesPageRoute = createRoute(
        options = RouteOptions(
            getParentRoute = { rootRoute },
            path = RoutePath(Paths.nativeInterfaces),
            component = NativeInterfacesPage
        )
    )

    val tgInteractionPageRoute = createRoute(
        options = RouteOptions(
            getParentRoute = { rootRoute },
            path = RoutePath(Paths.tg),
            component = TgInteractionPage
        )
    )

    rootRoute.addChildren(
        arrayOf(
            indexRoute,
            baseInfoPageRoute,
            viewportPageRoute,
            biometricPageRoute,
            hapticFeedbackPageRoute,
            themePageRoute,
            statePageRoute,
            homeScreenIntegrationPageRoute,
            backButtonPageRoute,
            bottomButtonsPageRoute,
            settingsButtonPageRoute,
            cloudStoragePageRoute,
            accelerometerPageRoute,
            deviceOrientationPageRoute,
            gyroscopePageRoute,
            locationManagerPageRoute,
            nativeInterfacesPageRoute,
            tgInteractionPageRoute,
        ),
    )

    val router = useConstant {
        createRouter(
            options = RouterOptions(
                routeTree = rootRoute,
            )
        )
    }

    val backButtonOnClick: VoidFunction = useCallback {
        router.asDynamic().history.go(-1)
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
            router.asDynamic().navigate(to = Paths.baseInfo)
        }
    }

    RouterProvider {
        this.router = router
        +props
    }

    +props.children
}
