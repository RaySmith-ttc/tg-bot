package ru.raysmith.tgbot.webappapp.pages.haptic

import mui.material.Orientation
import react.FC
import react.Props
import react.useEffectOnce
import ru.raysmith.tgbot.HapticFeedbackStyle
import ru.raysmith.tgbot.HapticFeedbackType
import ru.raysmith.tgbot.hooks.useBackButton
import ru.raysmith.tgbot.hooks.useHapticFeedback
import ru.raysmith.tgbot.webappapp.components.ButtonsGroupControl
import ru.raysmith.tgbot.webappapp.pages.BaseSubPageLayout

val HapticFeedbackPage = FC<Props> {
    val fh = useHapticFeedback()

    BaseSubPageLayout {
        title = "Haptic feedback"

        ButtonsGroupControl {
            orientation = Orientation.vertical
            items = mapOf(
                "light" to { fh.impactOccurred(HapticFeedbackStyle.light) },
                "medium" to { fh.impactOccurred(HapticFeedbackStyle.medium) },
                "heavy" to { fh.impactOccurred(HapticFeedbackStyle.heavy) },
                "rigid" to { fh.impactOccurred(HapticFeedbackStyle.rigid) },
                "soft" to { fh.impactOccurred(HapticFeedbackStyle.soft) },
            )
        }

        ButtonsGroupControl {
            orientation = Orientation.vertical
            items = mapOf(
                "error" to { fh.notificationOccurred(HapticFeedbackType.error) },
                "success" to { fh.notificationOccurred(HapticFeedbackType.success) },
                "warning" to { fh.notificationOccurred(HapticFeedbackType.warning) },
            )
        }

        ButtonsGroupControl {
            orientation = Orientation.vertical
            items = mapOf(
                "Selection changed" to { fh.selectionChanged() }
            )
        }
    }
}