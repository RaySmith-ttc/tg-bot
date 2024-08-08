package ru.raysmith.tgbot

import org.w3c.dom.Window

@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
inline val Window.Telegram: Telegram
    get() = asDynamic().Telegram as Telegram

external interface Telegram {
    @Suppress("PropertyName")
    val WebApp: WebApp
}