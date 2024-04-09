package ru.raysmith.tgbot.core.handler.base

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.BotHolder
import ru.raysmith.tgbot.model.network.CallbackQuery
import ru.raysmith.tgbot.utils.datepicker.DatePickerData
import java.time.LocalDate

data class DatePickerCallbackQueryHandler(
    override val query: CallbackQuery,
    val value: String,
    val prefix: String,
    override val bot: Bot
) : BaseCallbackHandler(query, bot.client), BotHolder {
    override val botConfig: BotConfig = bot.botConfig

    val datePickerData = DatePickerData.from(value)

    fun getDate(): LocalDate {
        return LocalDate.of(datePickerData.y!!, datePickerData.m!!, datePickerData.d!!)
    }
}