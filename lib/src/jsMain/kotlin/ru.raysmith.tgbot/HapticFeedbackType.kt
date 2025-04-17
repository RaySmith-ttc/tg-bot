package ru.raysmith.tgbot

import seskar.js.JsValue

@Suppress("NESTED_CLASS_IN_EXTERNAL_INTERFACE")
sealed external interface HapticFeedbackType {
    companion object {

        /** Indicates that a task or action has failed */
        @JsValue("error")
        val error: HapticFeedbackType

        /** Indicates that a task or action has completed successfully */
        @JsValue("success")
        val success: HapticFeedbackType

        /** Indicates that a task or action produced a warning */
        @JsValue("warning")
        val warning: HapticFeedbackType
    }
}