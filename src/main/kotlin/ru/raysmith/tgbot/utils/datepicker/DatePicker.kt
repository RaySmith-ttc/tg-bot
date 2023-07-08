package ru.raysmith.tgbot.utils.datepicker

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.EventHandlerFactory
import ru.raysmith.tgbot.core.handler.CallbackQueryHandler
import ru.raysmith.tgbot.model.bot.message.MessageText
import ru.raysmith.tgbot.model.bot.message.keyboard.MessageInlineKeyboard
import ru.raysmith.tgbot.model.network.CallbackQuery
import ru.raysmith.utils.letIf
import java.time.*
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit

interface BotFeature {
    fun handler(h: (eventHandlerFactory: EventHandlerFactory) -> Unit)
}

class DatePicker(val callbackQueryPrefix: String) {

    @Deprecated("Removed soon") // how many months back user can browse
    var monthLimitBack = -1
        set(value) {
            require(value >= -1) { "monthLimitBack must be positive" }
            field = value
        }

    @Deprecated("Removed soon")
    var monthLimitForward = -1 // how many months forward user can browse
        set(value) {
            require(value >= -1) { "monthLimitForward must be positive" }
            field = value
        }

    var monthPickerEnabled = true
    var yearsPickerEnabled = true

    var messageText: MessageText.(data: String?, state: DatePickerState) -> Unit = { _, _ -> text("Pick the date") }

    var additionalRowsPosition: AdditionalRowsPosition = AdditionalRowsPosition.BOTTOM
    var additionalRows: MessageInlineKeyboard.(data: String?) -> Unit = { }
    var additionalRowsVisibleOnStates = DatePickerState.entries.toSet()

    var timeZone = ZoneId.systemDefault()
    var locale = Bot.config.locale

    private val now get() = LocalDate.now(timeZone)

    var initDate: LocalDate = now
    var startWithState = DatePickerState.DAY

    var dates: (data: String?) -> ClosedRange<LocalDate> = { LocalDate.of(1900, 1, 1)..LocalDate.of(2099, 12, 31) }
    
    
//    private val allowPastDays = dates
//    var allowFutureDays = true

    var yearsColumns = 5
        get() = field.coerceIn(1, 8)

    var yearsRows = 10

    var handlerId = "date_picker_$callbackQueryPrefix"

    init {
        // 1 symbol for result prefix,
        // TODO [date piker | BC. support] add n symbol reserve (calculate for locale, e.g ru -> до н.э. (7), us -> BC (2))
        //  ! if data for setupMarkup is release, than this require is redundant and should be deleted
        require(callbackQueryPrefix.length <= 52) { "Callback query prefix for date picker is too long" }
    }

    fun setupMarkup(messageInlineKeyboard: MessageInlineKeyboard, data: String?) {
        val datesRange = dates(data)
        val date = initDate ?: run {
            when {
                now.isBefore(datesRange.start) -> datesRange.start
                now.isAfter(datesRange.endInclusive) -> datesRange.endInclusive
                else -> now
            }
        }
        messageInlineKeyboard.setupMarkup(startWithState, data) {
            when(startWithState) {
                DatePickerState.DAY -> setupDaysMarkup(date.year, date.monthValue, data)
                DatePickerState.MONTH -> setupMonthsMarkup(date.year, data)
                DatePickerState.YEAR -> setupYearsMarkup(date.year, data)
            }
        }
    }

    private fun MessageInlineKeyboard.setupMarkup(state: DatePickerState, data: String?, setup: MessageInlineKeyboard.() -> Unit) {
        val isAllowedState = state in additionalRowsVisibleOnStates
        if (isAllowedState && additionalRowsPosition == AdditionalRowsPosition.TOP) {
            additionalRows(data)
        }
        setup()
        if (isAllowedState && additionalRowsPosition == AdditionalRowsPosition.BOTTOM) {
            additionalRows(data)
        }
    }

    fun handle(handler: CallbackQueryHandler) {
        handler.apply {
            isDataStartWith(callbackQueryPrefix) { data ->
                val datePickerData = DatePickerData.from(data)
                when {
                    datePickerData.yearPage != null -> {
                        edit {
                            textWithEntities { messageText(datePickerData.additionalData, DatePickerState.YEAR) }
                            inlineKeyboard {
                                setupMarkup(DatePickerState.YEAR, datePickerData.additionalData) {
                                    setupYearsMarkup(datePickerData.yearPage, datePickerData.additionalData)
                                }
                            }
                        }
                    }
                    datePickerData.y != null && datePickerData.m != null -> {
                        edit {
                            textWithEntities { messageText(datePickerData.additionalData, DatePickerState.DAY) }
                            inlineKeyboard {
                                setupMarkup(DatePickerState.DAY, datePickerData.additionalData) {
                                    setupDaysMarkup(datePickerData.y, datePickerData.m, datePickerData.additionalData)
                                }
                            }
                        }
                    }
                    datePickerData.y != null && datePickerData.m == null -> {
                        edit {
                            textWithEntities { messageText(datePickerData.additionalData, DatePickerState.MONTH) }
                            inlineKeyboard {
                                setupMarkup(DatePickerState.MONTH, datePickerData.additionalData) {
                                    setupMonthsMarkup(datePickerData.y, datePickerData.additionalData)
                                }
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
        val maxLength = Month.of(month).length(Year.of(year).isLeap)
        val day = if (maxLength < now.dayOfMonth) maxLength else now.dayOfMonth
        return ChronoUnit.MONTHS.between(LocalDate.of(year, month, day), date)
    }

    private fun getFirstDate(datesRange: ClosedRange<LocalDate>) = now
        .withYear(if (monthLimitBack == -1) datesRange.start.year else now.year - (monthLimitBack / 12) - if (now.monthValue - (monthLimitBack % 12) < 0) 1 else 0)
        .withMonth(if (monthLimitBack == -1) datesRange.start.monthValue else now.monthValue - (monthLimitBack % 12))
    private fun getLastDate(datesRange: ClosedRange<LocalDate>) = now
        .withYear(if (monthLimitForward == -1) datesRange.endInclusive.year else now.year + (monthLimitForward / 12) + if (now.monthValue + monthLimitForward > 12) 1 else 0)
        .withMonth(if (monthLimitForward == -1) datesRange.endInclusive.monthValue else (now.monthValue + (monthLimitForward % 12)) % 12)

    private fun MessageInlineKeyboard.setupYearsMarkup(fromYear: Int, data: String?) {
        val datesRange = dates(data)
        val firstYear = getFirstDate(datesRange).year
        val lastYear = getLastDate(datesRange).year

        val pages = (firstYear..lastYear).chunked(yearsRows * yearsColumns) as List<List<Int?>>
        val pageIndex = pages.indexOfFirst { it.contains(fromYear) }.letIf({ it == -1 }) {
            val diffWithStart = fromYear - datesRange.start.year
            val diffWithEnd = datesRange.endInclusive.year - fromYear
            
            if (diffWithStart < diffWithEnd) 0 else pages.lastIndex
        }

        if (!(pageIndex == 0 && pageIndex == pages.size - 1)) {
            row {
                if (pageIndex != 0) button("«", callbackQueryPrefix + "{${data ?: ""}}p${pages[pageIndex - 1].first()}")
                else button(" ", CallbackQuery.EMPTY_CALLBACK_DATA)

                if (pageIndex != pages.size - 1) button("»", callbackQueryPrefix + "{${data ?: ""}}p${pages[pageIndex + 1].first()}")
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
                        year?.let { if (it == now.year) "·$it·" else it.toString() } ?: " ",
                        year.let {
                            if (it == null) CallbackQuery.EMPTY_CALLBACK_DATA
                            else callbackQueryPrefix + "{${data ?: ""}}y${it.toIso(true)}"
                        }
                    )
                }
            }
        }
    }

    private fun MessageInlineKeyboard.setupMonthsMarkup(year: Int, data: String?) {
        val datesRange = dates(data)
        val firstDate = getFirstDate(datesRange)
        val lastDate = getLastDate(datesRange)

        row {
            if (firstDate.year < year) button("«", "${callbackQueryPrefix}{${data ?: ""}}y${(year - 1).toIso(true)}")
            else button(" ", CallbackQuery.EMPTY_CALLBACK_DATA)

            button(year.toString(), if (yearsPickerEnabled) callbackQueryPrefix + "{${data ?: ""}}p${year}" else CallbackQuery.EMPTY_CALLBACK_DATA)

            if (lastDate.year > year) button("»", "${callbackQueryPrefix}{${data ?: ""}}y${(year + 1).toIso(true)}")
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
                    val callbackQuery = if (clickable) "${callbackQueryPrefix}{${data ?: ""}}y${year.toIso(true)}m${it.value.toIso()}" else CallbackQuery.EMPTY_CALLBACK_DATA
                    button(title, callbackQuery)
                }
            }
        }
    }

    private fun MessageInlineKeyboard.setupDaysMarkup(callbackYear: Int, callbackMonth: Int, data: String?) {
        val datesRange = dates(data)

        val year = when {
            callbackYear < datesRange.start.year -> datesRange.start.year
            callbackYear > datesRange.endInclusive.year -> datesRange.endInclusive.year
            else -> callbackYear
        }

        val month = when {
            callbackYear < datesRange.start.year -> datesRange.start.monthValue
            callbackYear == datesRange.start.year -> {
                if (callbackMonth < datesRange.start.monthValue) datesRange.start.monthValue else callbackMonth
            }
            callbackYear > datesRange.endInclusive.year -> datesRange.endInclusive.monthValue
            callbackYear == datesRange.endInclusive.year -> {
                if (callbackMonth > datesRange.endInclusive.monthValue) datesRange.endInclusive.monthValue else callbackMonth
            }
            else -> callbackMonth
        }

        val isCurrentMonth = year == now.year && month == now.monthValue
        val allowPastByDates = year > datesRange.start.year || (year == datesRange.start.year && month > datesRange.start.monthValue)
        val allowFutureByDates = year < datesRange.endInclusive.year || (year == datesRange.endInclusive.year && month < datesRange.endInclusive.monthValue)

        row {
            val diff = getMonthsDiff(year, month)

            if ((monthLimitBack == -1 || diff < monthLimitBack) && allowPastByDates) {
                val updatedYear = (if (month == 1) year - 1 else year)
                val updatedMonth = (if (month == 1) 12 else month - 1)
                button("«", "${callbackQueryPrefix}{${data ?: ""}}y${updatedYear.toIso(true)}m${updatedMonth.toIso()}")
            }
            else button(" ", CallbackQuery.EMPTY_CALLBACK_DATA)

            button(
                "${Month.of(month).localized(TextStyle.SHORT_STANDALONE)} $year",
                if (monthPickerEnabled) "${callbackQueryPrefix}{${data ?: ""}}y${year.toIso(true)}"
                else CallbackQuery.EMPTY_CALLBACK_DATA
            )
            
            if ((monthLimitForward == -1 || diff > -monthLimitForward) && allowFutureByDates) {
                val updatedYear = (if (month == 12) year + 1 else year)
                val updatedMonth = (if (month == 12) 1 else month + 1)
                button("»", "${callbackQueryPrefix}{${data ?: ""}}y${updatedYear.toIso(true)}m${updatedMonth.toIso()}")
            }
            else button(" ", CallbackQuery.EMPTY_CALLBACK_DATA)
        }

        val yearMonth = YearMonth.of(year, month)

        // TODO correct day of week by locale
        val prefixDays = yearMonth.atDay(1).dayOfWeek.value - 1
        val postfixDays = 7 - yearMonth.atEndOfMonth().dayOfWeek.value

        val fixedDays = buildList {
            if (prefixDays > 0) {
                repeat((1..prefixDays).count()) {
                    add(-1)
                }
            }

            when {
                !allowPastByDates && !allowFutureByDates -> {
                    repeat(datesRange.start.dayOfMonth - 1) { add(-1) }
                    addAll(datesRange.start.dayOfMonth..datesRange.endInclusive.dayOfMonth)
                    repeat(yearMonth.lengthOfMonth() - datesRange.endInclusive.dayOfMonth) { add(-1) }
                }
                !allowPastByDates -> {
                    repeat(datesRange.start.dayOfMonth - 1) { add(-1) }
                    addAll(datesRange.start.dayOfMonth..yearMonth.lengthOfMonth())
                }
                !allowFutureByDates -> {
                    addAll(1..datesRange.endInclusive.dayOfMonth)
                    repeat(yearMonth.lengthOfMonth() - datesRange.endInclusive.dayOfMonth) { add(-1) }
                }
                else -> addAll(1..yearMonth.lengthOfMonth())
            }

            if (postfixDays > 0) {
                repeat((1..postfixDays).count()) {
                    add(-1)
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

        val daysChunks = fixedDays.chunked(7)

        daysChunks.forEach { chunk ->
            row {
                chunk.forEach {
                    if (it == -1) button(" ", CallbackQuery.EMPTY_CALLBACK_DATA)
                    else {
                        val title = when {
//                            !allowToday && it == now.dayOfMonth -> " "
//                            !allowPastDays && ((year <= now.year && month <= now.monthValue) && it < now.dayOfMonth) -> " "
//                            !allowFutureDays && ((year >= now.year && month >= now.monthValue) && it > now.dayOfMonth) -> " "
                            else -> if (isCurrentMonth && it == now.dayOfMonth) "·$it·" else it.toString()
                        }
                        val callbackQuery = when {
//                            !allowToday && it == now.dayOfMonth -> CallbackQuery.EMPTY_CALLBACK_DATA
//                            !allowPastDays && ((year <= now.year && month <= now.monthValue) && it < now.dayOfMonth) -> CallbackQuery.EMPTY_CALLBACK_DATA
//                            !allowFutureDays && ((year >= now.year && month >= now.monthValue) && it > now.dayOfMonth) -> CallbackQuery.EMPTY_CALLBACK_DATA
                            else -> "r${callbackQueryPrefix}{${data ?: ""}}y${year.toIso()}m${month.toIso()}d${it.toIso()}"
                        }
                        button(title, callbackQuery)
                    }
                }
            }
        }

        // avoid keyboard size changes
        repeat(6 - daysChunks.size) {
            row {
                repeat(7) {
                    button(" ", CallbackQuery.EMPTY_CALLBACK_DATA)
                }
            }
        }
    }
}