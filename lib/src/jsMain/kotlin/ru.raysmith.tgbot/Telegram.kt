package ru.raysmith.tgbot

import org.w3c.dom.Window

inline val Window.Telegram: Telegram
    get() = asDynamic().Telegram.unsafeCast<Telegram>()

external interface Telegram {
    @Suppress("PropertyName")
    val WebApp: WebApp
}