package ru.raysmith.tgbot.webappapp.pages

import mui.icons.material.Check
import mui.icons.material.Fingerprint
import mui.icons.material.Info
import mui.icons.material.PrivacyTip
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.responsive
import mui.system.sx
import react.*
import react.dom.events.MouseEventHandler
import react.dom.html.ReactHTML
import ru.raysmith.tgbot.webApp
import ru.raysmith.tgbot.webappapp.components.Header
import ru.raysmith.tgbot.webappapp.components.RouterLink
import ru.raysmith.tgbot.webappapp.other
import ru.raysmith.tgbot.webappapp.router.Paths
import ru.raysmith.tgbot.webappapp.wrappers.mr
import web.cssom.Display
import web.cssom.JustifyContent
import web.cssom.WhiteSpace
import web.cssom.WordWrap
import web.html.HTMLButtonElement

val MainPage = FC<Props> {
    var showVerificationInfo by useState(false)

    Header {
        IconButton {
            size = Size.large
            edge = IconButtonEdge.end
            color = IconButtonColor.inherit
            sx { mr = 2 }

            PrivacyTip {
                onClick = {
                    showVerificationInfo = true
                }
            }
        }
    }

    Stack {
        spacing = responsive(2)
        direction = responsive(StackDirection.column)

        Button {
            component = RouterLink
            this.href = Paths.baseInfo
            startIcon = Info.create()
            +"Base information"
        }

        Button {
            component = RouterLink
            this.href = Paths.biometrical
            startIcon = Fingerprint.create()
            +"Biometrical"
        }

    }

    VerificationDialog {
        open = showVerificationInfo
        onClose = { _, _ -> showVerificationInfo = false }
        onOkButtonClick = { showVerificationInfo = false }
    }
}

external interface VerificationDialogProps : DialogProps {
    var onOkButtonClick: MouseEventHandler<HTMLButtonElement>
}

private val VerificationDialog = FC<VerificationDialogProps> { props ->
    Dialog {
       +props.other("onOkButtonClick")

        DialogTitle {
            Stack {
                direction = responsive(StackDirection.row)
                sx {
                    display = Display.flex
                    justifyContent = JustifyContent.spaceBetween
                }

                Typography {
                    +"Verification Info"
                    component = ReactHTML.h2
                    variant = TypographyVariant.h6
                }

                Box {
                    sx {
                        display = Display.flex
                        justifyContent = JustifyContent.end
                    }

                    Stack {
                        direction = responsive(StackDirection.row)

                        Typography {
                            variant = TypographyVariant.subtitle1
                            +"Verified: "
                        }

                        Check {
                            color = SvgIconColor.success
                        }
                    }
                }
            }
        }

        DialogContent {
            dividers = true
            VerificationInfo()
        }

        DialogActions {
            Button {
                +"Ok"
                onClick = props.onOkButtonClick
            }
        }
    }
}

private val VerificationInfo = FC<Props> {
    Stack {
        spacing = responsive(2)
        direction = responsive(StackDirection.column)

        VerificationInfoItem {
            title = "initData"
            body = webApp.initData
        }
        VerificationInfoItem {
            title = "initDataUnsafe"
            body = JSON.stringify(webApp.initDataUnsafe, undefined, 2)
        }
    }
}

external interface VerificationInfoItemProps : Props {
    var title: String
    var body: String

    var renderBody: (() -> ReactNode)?
}
private val VerificationInfoItem = FC<VerificationInfoItemProps> { props ->
    Typography {
        gutterBottom = true
        variant = TypographyVariant.subtitle1
        +props.title
    }

    props.renderBody?.invoke() ?: run {
        Typography {
            variant = TypographyVariant.body2
            sx {
                wordWrap = WordWrap.breakWord
                whiteSpace = WhiteSpace.breakSpaces
            }

            +props.body
        }
    }
}