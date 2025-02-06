package ru.raysmith.tgbot

import js.objects.Object
import js.objects.jso
import kotlinx.browser.window

val webApp get() = window.Telegram.WebApp

/** Alias for `Object.assign(this, obj)` */
fun <T : Any> T.assign(obj: T) = Object.assign(this, obj)

/** Alias for `jso { Object.assign(this, obj) }` */
fun <T : Any, R> copyOf(obj: T): R = jso { Object.assign(this, obj) }