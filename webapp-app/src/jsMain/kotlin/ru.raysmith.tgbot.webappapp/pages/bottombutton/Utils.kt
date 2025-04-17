package ru.raysmith.tgbot.webappapp.pages.bottombutton

import web.cssom.Color
import kotlin.math.floor
import kotlin.random.Random

fun getRandomColor(): Color {
    val letters = "0123456789ABCDEF"
    var color = "#"
    for (i in 0..5) {
        color += letters[floor(Random.nextFloat() * 16).toInt()]
    }
    return color.unsafeCast<Color>()
}