package ru.raysmith.tgbot

import kotlinx.js.JsPlainObject
import org.w3c.dom.Window

inline val Window.Telegram: Telegram
    get() = asDynamic().Telegram.unsafeCast<Telegram>()

@JsPlainObject
external interface Telegram {
    @Suppress("PropertyName")
    val WebApp: WebApp
}
