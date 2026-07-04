package ru.raysmith.tgbot

import seskar.js.JsValue

/**
 * @property light indicates a collision between small or lightweight UI objects
 * @property medium indicates a collision between medium-sized or medium-weight UI objects
 * @property heavy indicates a collision between large or heavyweight UI objects
 * @property rigid indicates a collision between hard or inflexible UI objects
 * @property soft indicates a collision between soft or flexible UI objects
 * */
@Suppress("NESTED_CLASS_IN_EXTERNAL_INTERFACE")
sealed external interface HapticFeedbackStyle {
    companion object {

        /** Indicates a collision between small or lightweight UI objects */
        @JsValue("light")
        val light: HapticFeedbackStyle

        /** Indicates a collision between medium-sized or medium-weight UI objects */
        @JsValue("medium")
        val medium: HapticFeedbackStyle

        /** Indicates a collision between large or heavyweight UI objects */
        @JsValue("heavy")
        val heavy: HapticFeedbackStyle

        /** Indicates a collision between hard or inflexible UI objects */
        @JsValue("rigid")
        val rigid: HapticFeedbackStyle

        /** Indicates a collision between soft or flexible UI objects */
        @JsValue("soft")
        val soft: HapticFeedbackStyle
    }
}
