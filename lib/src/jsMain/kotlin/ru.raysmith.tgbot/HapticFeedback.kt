package ru.raysmith.tgbot

/** This object controls haptic feedback. */
external interface HapticFeedback {

    /**
     * A method tells that an impact occurred.
     * The Telegram app may play the appropriate haptics based on style value passed.
     *
     * @since Bot API 6.1
     * */
    val impactOccurred: (style: HapticFeedbackStyle) -> HapticFeedback

    /**
     * A method tells that a task or action has succeeded, failed, or produced a warning.
     * The Telegram app may play the appropriate haptics based on type value passed.
     *
     * @since Bot API 6.1
     * */
    val notificationOccurred: (type: HapticFeedbackType) -> HapticFeedback

    /**
     * A method tells that the user has changed a selection. The Telegram app may play the appropriate haptics.
     *
     * *Do not use this feedback when the user makes or confirms a selection; use it only when the selection changes.*
     *
     * @since Bot API 6.1
     * */
    val selectionChanged: () -> HapticFeedback
}