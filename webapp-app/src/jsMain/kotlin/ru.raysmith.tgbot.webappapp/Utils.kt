package ru.raysmith.tgbot.webappapp

import react.dom.events.MouseEvent
import web.html.HTMLButtonElement

typealias OnClick = (OnClickEvent?) -> Unit
typealias OnClickEvent = MouseEvent<HTMLButtonElement, *>

fun String.capitalize() = replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
fun String.decapitalize() = replaceFirstChar { it.lowercase() }