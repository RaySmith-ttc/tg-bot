package ru.raysmith.tgbot.webappapp.pages.haptic

import mui.material.*
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import react.useEffectOnce
import ru.raysmith.tgbot.HapticFeedbackStyle
import ru.raysmith.tgbot.HapticFeedbackType
import ru.raysmith.tgbot.hooks.useBackButton
import ru.raysmith.tgbot.hooks.useHapticFeedback
import ru.raysmith.tgbot.webappapp.pages.BaseSubPageLayout
import web.cssom.AlignItems
import web.cssom.px

val HapticFeedbackPage = FC<Props> {
    val fh = useHapticFeedback()
    val backButton = useBackButton()

    useEffectOnce {
        backButton.show()
    }

    BaseSubPageLayout {
        title = "Haptic feedback"
        Stack {
            spacing = responsive(1)
            sx {
                alignItems = AlignItems.center
            }

            Stack {
                sx {
                    alignItems = AlignItems.center
                }

                ButtonGroup {
                    variant = ButtonGroupVariant.contained

                    Button {
                        +"light"
                        onClick = { fh.impactOccurred(HapticFeedbackStyle.light) }
                    }
                    Button {
                        +"medium"
                        onClick = { fh.impactOccurred(HapticFeedbackStyle.medium) }
                    }
                    Button {
                        +"heavy"
                        onClick = { fh.impactOccurred(HapticFeedbackStyle.heavy) }
                    }
                }

                ButtonGroup {
                    variant = ButtonGroupVariant.contained

                    Button {
                        +"rigid"
                        sx { borderTopLeftRadius = 0.px }
                        onClick = { fh.impactOccurred(HapticFeedbackStyle.rigid) }
                    }
                    Button {
                        +"soft"
                        sx { borderTopRightRadius = 0.px }
                        onClick = { fh.impactOccurred(HapticFeedbackStyle.soft) }
                    }
                }
            }

            ButtonGroup {
                variant = ButtonGroupVariant.contained

                Button {
                    +"error"
                    onClick = { fh.notificationOccurred(HapticFeedbackType.error) }
                }
                Button {
                    +"success"
                    onClick = { fh.notificationOccurred(HapticFeedbackType.success) }
                }
                Button {
                    +"warning"
                    onClick = { fh.notificationOccurred(HapticFeedbackType.warning) }
                }
            }

            Button {
                +"Selection changed"
                variant = ButtonVariant.contained
                onClick = { fh.selectionChanged() }
            }
        }
    }

}