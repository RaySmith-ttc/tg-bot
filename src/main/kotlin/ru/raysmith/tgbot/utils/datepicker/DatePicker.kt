package ru.raysmith.tgbot.utils.datepicker

import ru.raysmith.tgbot.core.handler.CallbackQueryHandler
import ru.raysmith.tgbot.model.bot.MessageInlineKeyboard
import ru.raysmith.tgbot.model.network.CallbackQuery
import ru.raysmith.tgbot.utils.AdditionalRowsPosition
import ru.raysmith.utils.PropertiesFactory
import ru.raysmith.utils.properties.getOrNull
import java.time.*
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.*

class DatePicker(val callbackQueryPrefix: String) {

    // TODO replace to days
    // how many months back user can browse
    var monthLimitBack = -1
        set(value) {
            require(value >= -1) { "monthLimitBack must be positive" }
            field = value
        }

    // how many months forward user can browse
    var monthLimitForward = -1
        set(value) {
            require(value >= -1) { "monthLimitForward must be positive" }
            field = value
        }

    var monthPickerEnabled = true
    var yearsPickerEnabled = true

    var allowToday = true
    var allowPastDays = true
        set(value) {
            monthLimitBack = 0
            field = value
        }
    var allowFutureDays = true
        set(value) {
            monthLimitForward = 0
            field = value
        }

    var defaultMessageText = "Pick the date"
    var yearsPickMessageText = { defaultMessageText }
    var monthPickMessageText: (year: Int) -> String = { _ -> defaultMessageText }
    var dayPickMessageText: (year: Int, month: Int) -> String = { _, _ -> defaultMessageText }

    var additionalRowsPosition: AdditionalRowsPosition = AdditionalRowsPosition.BOTTOM
    var additionalRows: MessageInlineKeyboard.() -> Unit = { }
    var additionalRowsVisibleOnStates = DatePickerState.values().toSet()

    var timeZone = ZoneId.systemDefault()
    var locale = PropertiesFactory.from("bot.properties").getOrNull("calendar_locale")?.let {
        Locale.forLanguageTag(it)
    } ?: Locale.getDefault()

    // TODO [date picker] change to two LocalDate
    var years = 1900..2100

    var yearsColumns = 5
    var yearsRows = 10

    private val now = LocalDate.now(timeZone)

    val handlerId = "date_picker_$callbackQueryPrefix"

    init {
        // 1 symbol for result prefix,
        // TODO [date piker | BC. support] add n symbol reserve (calculate for locale, e.g ru -> до н.э. (7), us -> BC (2))
        require(callbackQueryPrefix.length <= 52) { "Callback query prefix for date picker is too long" }
    }

    fun setupMarkup(messageInlineKeyboard: MessageInlineKeyboard) {
        messageInlineKeyboard.setupMarkup(DatePickerState.DAY) { setupDaysMarkup(now.year, now.monthValue) }
    }

    private fun MessageInlineKeyboard.setupMarkup(state: DatePickerState, setup: MessageInlineKeyboard.() -> Unit) {
        val isAllowedState = state in additionalRowsVisibleOnStates
        if (isAllowedState && additionalRowsPosition == AdditionalRowsPosition.TOP) {
            additionalRows()
        }
        setup()
        if (isAllowedState && additionalRowsPosition == AdditionalRowsPosition.BOTTOM) {
            additionalRows()
        }
    }

    fun handle(handler: CallbackQueryHandler) {
        handler.apply {
            isDataStartWith(callbackQueryPrefix) { data ->
                val datePickerData = DatePickerData.from(data)
                when {
                    datePickerData.yearPage != null -> {
                        edit {
                            text = yearsPickMessageText()
                            inlineKeyboard {
                                setupMarkup(DatePickerState.YEAR) { setupYearsMarkup(datePickerData.yearPage) }
                            }
                        }
                    }
                    datePickerData.y != null && datePickerData.m != null -> {
                        edit {
                            text = dayPickMessageText(datePickerData.y, datePickerData.m)
                            inlineKeyboard {
                                setupMarkup(DatePickerState.DAY) { setupDaysMarkup(datePickerData.y, datePickerData.m) }
                            }
                        }
                    }
                    datePickerData.y != null && datePickerData.m == null -> {
                        edit {
                            text = monthPickMessageText(datePickerData.y)
                            inlineKeyboard {
                                setupMarkup(DatePickerState.MONTH) { setupMonthsMarkup(datePickerData.y) }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun Month.localized(style: TextStyle = TextStyle.FULL_STANDALONE) = getDisplayName(style, locale)
    private fun Number.toIso() = if (this.toInt() < 10) "0$this" else this.toString()
    private fun Number.toIso(isYear: Boolean = false) = this.toString().padStart(if (isYear) 4 else 2, '0')
    private fun getMonthsDiff(year: Int, month: Int, date: LocalDate = now): Long {
        return ChronoUnit.MONTHS.between(LocalDate.of(year, month, now.dayOfMonth), date)
    }

    private fun getFirstDate() = now
        .withYear(if (monthLimitBack == -1) years.first else now.year - (monthLimitBack / 12) - if (now.monthValue - (monthLimitBack % 12) < 0) 1 else 0)
        .withMonth(if (monthLimitBack == -1) 1 else now.monthValue - (monthLimitBack % 12))
    private fun getLastDate() = now
        .withYear(if (monthLimitForward == -1) years.last else now.year + (monthLimitForward / 12) + if (now.monthValue + monthLimitForward > 12) 1 else 0)
        .withMonth(if (monthLimitForward == -1) 12 else (now.monthValue + (monthLimitForward % 12)) % 12)

    private fun MessageInlineKeyboard.setupYearsMarkup(fromYear: Int) {
        val firstYear = getFirstDate().year
        val lastYear = getLastDate().year

        val pages = (firstYear..lastYear).chunked(yearsRows * yearsColumns) as List<List<Int?>>
        val pageIndex = pages.indexOfFirst { it.contains(fromYear) }

        if (!(pageIndex == 0 && pageIndex == pages.size - 1)) {
            row {
                if (pageIndex != 0) button("«", callbackQueryPrefix + "p${pages[pageIndex - 1].first()}")
                else button(" ", CallbackQuery.EMPTY_CALLBACK_DATA)

                if (pageIndex != pages.size - 1) button("»", callbackQueryPrefix + "p${pages[pageIndex + 1].first()}")
                else button(" ", CallbackQuery.EMPTY_CALLBACK_DATA)
            }
        }

        pages[pageIndex].chunked(yearsColumns).map { it.toMutableList() }.toMutableList().let { rows ->
            val nullValue: Int? = null
            repeat(yearsRows - rows.size) {
                rows.add(MutableList(yearsColumns) { nullValue })
            }

            rows.onEach { row ->
                repeat(yearsColumns - row.size) {
                    row.add(nullValue)
                }
            }

            rows

        }.forEach { row ->
            row {
                row.forEach { year ->
                    button(
                        year?.toString() ?: " ",
                        year.let {
                            if (it == null) CallbackQuery.EMPTY_CALLBACK_DATA
                            else callbackQueryPrefix + "y${it.toIso(true)}"
                        }
                    )
                }
            }
        }
    }

    private fun MessageInlineKeyboard.setupMonthsMarkup(year: Int) {
        val firstDate = getFirstDate()
        val lastDate = getLastDate()

        row {
            if (firstDate.year < year) button("«", "${callbackQueryPrefix}y${(year - 1).toIso(true)}}")
            else button(" ", CallbackQuery.EMPTY_CALLBACK_DATA)

            button(year.toString(), if (yearsPickerEnabled) callbackQueryPrefix + "p${year}" else CallbackQuery.EMPTY_CALLBACK_DATA)

            if (lastDate.year > year) button("»", "${callbackQueryPrefix}y${(year + 1).toIso(true)}}")
            else button(" ", CallbackQuery.EMPTY_CALLBACK_DATA)
        }

        val monthSequence = generateSequence(Month.of(1)) { it + 1 }
        monthSequence.take(12).chunked(4).toList().map { row ->
            row {
                row.forEach {

                    val diffWithLastDate = getMonthsDiff(year, it.value, lastDate)
                    val diffWithFirstDate = getMonthsDiff(year, it.value, firstDate)
                    val clickable = diffWithLastDate >= 0 && diffWithFirstDate <= 0

                    val title = if (clickable) if (year == now.year && it.value == now.monthValue) "·${it.localized()}·" else it.localized() else " "
                    val callbackQuery = if (clickable) "${callbackQueryPrefix}y${year.toIso(true)}m${it.value.toIso()}" else CallbackQuery.EMPTY_CALLBACK_DATA
                    button(title, callbackQuery)
                }
            }
        }
    }

    private fun MessageInlineKeyboard.setupDaysMarkup(year: Int, month: Int) {
        val isCurrentMonth = year == now.year && month == now.monthValue
        row {
            val diff = getMonthsDiff(year, month)
            val allowByPastDaysDeny = !(!allowPastDays && isCurrentMonth)
            val allowByFutureDaysDeny = !(!allowFutureDays && isCurrentMonth)
            if ((monthLimitBack == -1 || diff < monthLimitBack) && allowByPastDaysDeny) {
                val updatedYear = if (month == 1) year - 1 else year
                val updatedMonth = if (month == 1) 12 else month - 1
                button("«", "${callbackQueryPrefix}y${updatedYear.toIso(true)}m${updatedMonth.toIso()}")
            }
            else button(" ", CallbackQuery.EMPTY_CALLBACK_DATA)

            button(
                "${Month.of(month).localized(TextStyle.SHORT_STANDALONE)} $year",
                if (monthPickerEnabled) "${callbackQueryPrefix}y${year.toIso(true)}"
                else CallbackQuery.EMPTY_CALLBACK_DATA
            )

            if ((monthLimitForward == -1 || diff > -monthLimitForward) && allowByFutureDaysDeny) {
                val updatedYear = if (month == 12) year + 1 else year
                val updatedMonth = if (month == 12) 1 else month + 1
                button("»", "${callbackQueryPrefix}y${updatedYear.toIso(true)}m${updatedMonth.toIso()}")
            }
            else button(" ", CallbackQuery.EMPTY_CALLBACK_DATA)
        }

        val yearMonth = YearMonth.of(year, month)

        // TODO correct day of week by locale
        val prefixDays = yearMonth.atDay(1).dayOfWeek.value - 1
        val postfixDays = 7 - yearMonth.atEndOfMonth().dayOfWeek.value

        val fixedDays = (1..yearMonth.lengthOfMonth()).let { range ->
            mutableListOf<Int>().apply {
                if (prefixDays > 0) {
                    repeat((1..prefixDays).count()) {
                        add(-1)
                    }
                }
                addAll(range)
                if (postfixDays > 0) {
                    repeat((1..postfixDays).count()) {
                        add(-1)
                    }
                }
            }
        }

        val dayOfWeekSequence = generateSequence(DayOfWeek.of(1)) { it + 1}
        fun DayOfWeek.localized() = getDisplayName(TextStyle.SHORT_STANDALONE, locale)

        // Day of week headers
        row {
            dayOfWeekSequence.take(7).forEach {
                button(it.localized(), CallbackQuery.EMPTY_CALLBACK_DATA)
            }
        }

        fixedDays.chunked(7).forEach { chunk ->
            row {
                chunk.forEach {
                    if (it == -1) button(" ", CallbackQuery.EMPTY_CALLBACK_DATA)
                    else {
                        val title = when {
                            !allowToday && it == now.dayOfMonth -> " "
                            !allowPastDays && ((year <= now.year && month <= now.monthValue) && it < now.dayOfMonth) -> " "
                            !allowFutureDays && ((year >= now.year && month >= now.monthValue) && it > now.dayOfMonth) -> " "
                            else -> if (isCurrentMonth && it == now.dayOfMonth) "·$it·" else it.toString()
                        }
                        val callbackQuery = when {
                            !allowToday && it == now.dayOfMonth -> CallbackQuery.EMPTY_CALLBACK_DATA
                            !allowPastDays && ((year <= now.year && month <= now.monthValue) && it < now.dayOfMonth) -> CallbackQuery.EMPTY_CALLBACK_DATA
                            !allowFutureDays && ((year >= now.year && month >= now.monthValue) && it > now.dayOfMonth) -> CallbackQuery.EMPTY_CALLBACK_DATA
                            else -> "r${callbackQueryPrefix}y${year.toIso()}m${month.toIso()}d${it.toIso()}"
                        }
                        button(title, callbackQuery)
                    }
                }
            }
        }

        // avoid keyboard size changes
        repeat(8 - rowsCount) {
            row {
                repeat(7) {
                    button(" ", CallbackQuery.EMPTY_CALLBACK_DATA)
                }
            }
        }
    }
}