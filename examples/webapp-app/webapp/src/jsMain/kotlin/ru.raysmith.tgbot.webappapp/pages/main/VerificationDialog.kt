package ru.raysmith.tgbot.webappapp.pages.main

import mui.icons.material.Check
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import react.ReactNode
import react.dom.events.MouseEventHandler
import react.dom.html.ReactHTML
import ru.raysmith.tgbot.webApp
import ru.raysmith.tgbot.webappapp.other
import web.cssom.Display
import web.cssom.JustifyContent
import web.cssom.WhiteSpace
import web.cssom.WordWrap
import web.html.HTMLButtonElement

external interface VerificationDialogProps : DialogProps {
    var onOkButtonClick: MouseEventHandler<HTMLButtonElement>
}

val VerificationDialog = FC<VerificationDialogProps> { props ->
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
            VerificationInfoContent()
        }

        DialogActions {
            Button {
                +"Ok"
                onClick = props.onOkButtonClick
            }
        }
    }
}

// -------------------------------------------- VerificationInfoContent ------------------------------------------------

private val VerificationInfoContent = FC<Props> {
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

// ---------------------------------------------- VerificationInfoItem -------------------------------------------------

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