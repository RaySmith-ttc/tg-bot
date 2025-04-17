package ru.raysmith.tgbot

import seskar.js.JsValue

@Suppress("NESTED_CLASS_IN_EXTERNAL_INTERFACE")
sealed external interface ColorScheme {
    companion object {
        @JsValue("light")
        val light: ColorScheme

        @JsValue("dark")
        val dark: ColorScheme
    }
}