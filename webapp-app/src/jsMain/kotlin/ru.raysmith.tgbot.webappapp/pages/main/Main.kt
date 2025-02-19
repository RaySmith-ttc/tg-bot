package ru.raysmith.tgbot.webappapp.pages.main

import mui.icons.material.*
import mui.material.*
import mui.material.Size
import mui.material.styles.Theme
import mui.material.styles.useTheme
import mui.system.PropsWithSx
import mui.system.responsive
import mui.system.sx
import react.*
import ru.raysmith.tgbot.CssVar
import ru.raysmith.tgbot.hooks.useViewport
import ru.raysmith.tgbot.webApp
import ru.raysmith.tgbot.webappapp.components.RouterLink
import ru.raysmith.tgbot.webappapp.router.Paths
import ru.raysmith.tgbot.webappapp.wrappers.mt
import ru.raysmith.tgbot.webappapp.wrappers.pt
import web.cssom.*

external interface VerificationButtonProps : PropsWithSx {
    var onClick: () -> Unit
}

val VerificationButton = FC<VerificationButtonProps> { props ->
    IconButton {
        size = Size.large
        edge = IconButtonEdge.end
        color = IconButtonColor.inherit
        onClick = { props.onClick() }
        sx {
            +props.sx
        }

        PrivacyTip {}
    }
}

val MainPage = FC<Props> {
    var showVerificationInfo by useState(false)
    val theme = useTheme<Theme>()

    useEffectOnce {
        webApp.BackButton.hide()
    }

    VerificationButton {
        sx {
            position = Position.absolute
            pt = theme.spacing(1)
            right = theme.spacing(2)
            color = theme.palette.primary.main
            zIndex = integer(1)
        }
        onClick = {
            showVerificationInfo = true
        }
    }

    Stack {
        spacing = responsive(2)
        direction = responsive(StackDirection.column)
        sx {
            alignItems = AlignItems.flexStart
        }

        SubPageButton {
            +"Base information"
            href = Paths.baseInfo
            startIcon = Info.create()
        }

        SubPageButton {
            +"Viewport"
            href = Paths.viewport
            startIcon = PhotoSizeSelectLarge.create()
        }

        SubPageButton {
            +"Biometric"
            href = Paths.biometric
            startIcon = Fingerprint.create()
        }

        SubPageButton {
            +"Haptic feedback"
            href = Paths.hapticFeedback
            startIcon = Vibration.create()
        }

        SubPageButton {
            +"Theme"
            href = Paths.theme
            startIcon = Palette.create()
        }

        SubPageButton {
            +"State"
            href = Paths.state
            startIcon = Traffic.create()
        }

        SubPageButton {
            +"Home Screen Integration"
            href = Paths.homeSceenIntegration
            startIcon = AddToHomeScreen.create()
        }

        SubPageButton {
            +"Back button"
            href = Paths.backButton
            startIcon = ArrowBack.create()
        }

        SubPageButton {
            +"Bottom buttons"
            href = Paths.bottomButtons
            startIcon = SmartButton.create()
        }

        SubPageButton {
            +"Settings button"
            href = Paths.settingsButton
            startIcon = Settings.create()
        }

        SubPageButton {
            +"Cloud storage"
            href = Paths.cloud
            startIcon = Cloud.create()
        }

        SubPageButton {
            +"Accelerometer"
            href = Paths.accelerometer
            startIcon = ViewInAr.create()
        }

        SubPageButton {
            +"Device orientation"
            href = Paths.deviceOrientation
            startIcon = ScreenRotation.create()
        }

        SubPageButton {
            +"Gyroscope"
            href = Paths.gyroscope
            startIcon = ThreeSixty.create()
        }

        SubPageButton {
            +"Location manager"
            href = Paths.location
            startIcon = LocationOn.create()
        }
    }

    VerificationDialog {
        open = showVerificationInfo
        onClose = { _, _ -> showVerificationInfo = false }
        onOkButtonClick = { showVerificationInfo = false }
        sx {
            mt = "calc(${CssVar.tgContentSafeAreaInsetTop<dynamic>()} + ${CssVar.tgSafeAreaInsetTop<dynamic>()})"
        }
    }
}

// ------------------------------------------------ SubPageButton ------------------------------------------------------

private val SubPageButton = FC<ButtonProps> { props ->
    Button {
        component = RouterLink
        size = Size.large
        fullWidth = true

        sx {
            justifyContent = JustifyContent.start
        }

        +props
    }
}

