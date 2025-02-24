package ru.raysmith.tgbot.webappapp.pages.homescreen

import mui.icons.material.AddToHomeScreen
import mui.material.*
import mui.system.responsive
import react.*
import ru.raysmith.tgbot.EventType
import ru.raysmith.tgbot.HomeScreenStatus
import ru.raysmith.tgbot.hooks.useBackButton
import ru.raysmith.tgbot.webApp
import ru.raysmith.tgbot.webappapp.components.applyControlButtonStyle
import ru.raysmith.tgbot.webappapp.pages.BaseSubPageLayout

val HomeScreenIntegrationPage = FC<Props> {
    val backButton = useBackButton()

    var status by useState<HomeScreenStatus?>(null)

    useEffectOnce {
        backButton.show()

        webApp.checkHomeScreenStatus {
            status = it
        }

        webApp.onEvent(EventType.homeScreenAdded) {
            status = HomeScreenStatus.added
        }
    }

    BaseSubPageLayout {
        title = "Home screen integration"
        when(status) {
            HomeScreenStatus.unsupported -> {
                Alert {
                    +"The feature is not supported, and it is not possible to add the icon to the home screen"
                    severity = AlertColor.error.unsafeCast<String>()
                }
            }
            HomeScreenStatus.unknown -> {
                Alert {
                    +"The feature is supported, and the icon can be added, but it is not possible to determine if the icon has already been added"
                    severity = AlertColor.info.unsafeCast<String>()
                }
            }
            HomeScreenStatus.added -> {
                Alert {
                    +"The icon has already been added to the home screen"
                    severity = AlertColor.success.unsafeCast<String>()
                }
            }
            HomeScreenStatus.missed -> {
                Stack {
                    direction = responsive(StackDirection.column)
                    spacing = responsive(2)

                    Alert {
                        +"The icon has not been added to the home screen"
                        severity = AlertColor.warning.unsafeCast<String>()
                    }

                    Button {
                        +"Add to home screen"
                        applyControlButtonStyle()
                        startIcon = AddToHomeScreen.create()
                        onClick = {
                            webApp.addToHomeScreen()
                        }
                    }
                }
            }
            else -> {}
        }
    }
}