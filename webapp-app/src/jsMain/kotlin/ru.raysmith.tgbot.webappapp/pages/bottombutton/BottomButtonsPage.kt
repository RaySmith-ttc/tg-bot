package ru.raysmith.tgbot.webappapp.pages.bottombutton

import mui.material.Typography
import mui.material.styles.TypographyVariant
import mui.system.Stack
import mui.system.StackDirection
import mui.system.responsive
import mui.system.sx
import react.FC
import ru.raysmith.tgbot.BottomButtonType
import ru.raysmith.tgbot.hooks.useBottomButton
import ru.raysmith.tgbot.webappapp.components.DefaultValuesHintAlert
import ru.raysmith.tgbot.webappapp.pages.BaseSubPageLayout
import ru.raysmith.tgbot.webappapp.wrappers.pt
import web.cssom.AlignItems

val BottomButtonsPage = FC {
    val mb = useBottomButton(BottomButtonType.main)
    val sb = useBottomButton(BottomButtonType.secondary)

    BaseSubPageLayout {
        title = "Bottom button"
        Stack {
            direction = responsive(StackDirection.column)
            spacing = responsive(1)
            sx { alignItems = AlignItems.center }

            Typography {
                +"Main button"
                variant = TypographyVariant.h6
                sx { pt = 2 }
            }

            BottomButtonView {
                button = mb
            }

            Typography {
                +"Secondary button"
                variant = TypographyVariant.h6
                sx { pt = 2 }
            }

            BottomButtonView {
                button = sb
            }

            DefaultValuesHintAlert()
        }
    }
}

