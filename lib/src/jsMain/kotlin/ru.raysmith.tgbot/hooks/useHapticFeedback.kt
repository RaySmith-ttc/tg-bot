package ru.raysmith.tgbot.hooks

import js.objects.jso
import ru.raysmith.tgbot.HapticFeedbackStyle
import ru.raysmith.tgbot.HapticFeedbackType
import ru.raysmith.tgbot.webApp
import web.function.VoidFunction

/** This hook controls haptic feedback. */
fun useHapticFeedback(): HapticFeedbackHookType {

    val impactOccurred: (style: HapticFeedbackStyle) -> Unit = {
        webApp.HapticFeedback.impactOccurred(it)
    }

    val notificationOccurred: (type: HapticFeedbackType) -> Unit = {
        webApp.HapticFeedback.notificationOccurred(it)
    }

    val selectionChanged: VoidFunction = {
        webApp.HapticFeedback.selectionChanged()
    }

    return jso {
        this.impactOccurred = impactOccurred
        this.notificationOccurred = notificationOccurred
        this.selectionChanged = selectionChanged
    }
}

external interface HapticFeedbackHookType {

    /**
     * A method tells that an impact occurred.
     * The Telegram app may play the appropriate haptics based on style value passed.
     *
     * @since Bot API 6.1
     * */
    var impactOccurred: (style: HapticFeedbackStyle) -> Unit

    /**
     * A method tells that a task or action has succeeded, failed, or produced a warning.
     * The Telegram app may play the appropriate haptics based on type value passed.
     *
     * @since Bot API 6.1
     * */
    var notificationOccurred: (type: HapticFeedbackType) -> Unit

    /**
     * A method tells that the user has changed a selection. The Telegram app may play the appropriate haptics.
     *
     * *Do not use this feedback when the user makes or confirms a selection; use it only when the selection changes.*
     *
     * @since Bot API 6.1
     * */
    var selectionChanged: VoidFunction
}