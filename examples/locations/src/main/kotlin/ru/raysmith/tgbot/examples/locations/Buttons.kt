package ru.raysmith.tgbot.examples.locations

import ru.raysmith.tgbot.model.bot.message.keyboard.MessageInlineKeyboard
import ru.raysmith.tgbot.model.network.CallbackQuery
import ru.raysmith.tgbot.model.network.keyboard.KeyboardButton

private typealias InlineRow = MessageInlineKeyboard.Row

fun InlineRow.back(text: String = KeyboardButton.BACK) = button(text, CallbackQuery.BACK)
suspend inline fun MessageInlineKeyboard.back(text: String = KeyboardButton.BACK) = row { back(text) }

val KeyboardButton.Companion.BACK: String get() = "« Back"
val KeyboardButton.Companion.CANCEL: String get() = "« Cancel"


val CallbackQuery.Companion.BACK: String get() = "back"
val CallbackQuery.Companion.DP_BIRTH_DATE_PREFIX: String get() = "birth_date_"