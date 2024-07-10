package ru.raysmith.tgbot.utils.datepicker

import kotlinx.coroutines.runBlocking
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotConfig
import ru.raysmith.tgbot.core.handler.EventHandler
import ru.raysmith.tgbot.core.handler.base.CallbackQueryHandler
import ru.raysmith.tgbot.model.bot.message.MessageText
import ru.raysmith.tgbot.model.bot.message.TextMessage
import ru.raysmith.tgbot.model.bot.message.keyboard.MessageInlineKeyboard
import ru.raysmith.tgbot.model.network.CallbackQuery
import ru.raysmith.tgbot.utils.BotFeature
import ru.raysmith.utils.letIf
import java.time.*
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.time.temporal.WeekFields
import java.util.*

internal typealias DPSetting<T> = suspend (botConfig: BotConfig, data: String?) -> T
internal typealias DPSettingWithState<T> = suspend (botConfig: BotConfig, data: String?, state: DatePickerState) -> T

/**
 * Creates new datepicker instance. Use it to [register][Bot.registerDatePicker]
 * and [send][TextMessage.datePicker] to users
 *
 * @param callbackQueryPrefix Callback query that be added as prefix to all date picker action buttons
 * @param setup configuration DSL builder
 * */
fun createDatePicker(
    callbackQueryPrefix: String,
    setup: DatePicker.() -> Unit
) = DatePicker(callbackQueryPrefix).apply(setup)

/**
 * Date picker bot feature. Use to suggest to the user select a date. To create and use date picker:
 *
 * 1. Create new instance with [createDatePicker]. Since the picker can be used in different bots,
 * each setting has an [BotConfig] argument. Also, you can use a data that you provide when sending a message.
 * ```
 * val CallbackQuery.Companion.DATE_PICKER_PREFIX: String get() = "dp"
 * createDatePicker(CallbackQuery.DATE_PICKER_PREFIX) {
 *      locale { botConfig, data ->
 *          botConfig.locale
 *      }
 * }
 * ```
 *
 * 2. Register new instance to the bot instance with [registerDatePicker][Bot.registerDatePicker]
 * ```
 * Bot()
 *     .registerDatePicker(datePicker)
 *     ...
 *     .locations {
 *         ...
 *     }
 * ```
 *
 * 3. Send a message with the picker:
 * ```
 * send {
 *     datePicker(datePicker)
 * }
 * ```
 * */
@Suppress("KDocUnresolvedReference")
class DatePicker(val callbackQueryPrefix: String) : BotFeature {

    var messageText: suspend MessageText.(botConfig: BotConfig, data: String?, state: DatePickerState) -> Unit = { _, _, _ -> text("Select a date") }
        private set

    /**
     * Sets the message text builder. By default — «Select a date».
     *
     * @param botConfig current bot config
     * @param data optional custom data provided when sending a message
     * @param state Current picker view state ([DatePickerState.DAY], [DatePickerState.MONTH] or [DatePickerState.YEAR])
     * */
    fun messageText(block: suspend MessageText.(botConfig: BotConfig, data: String?, state: DatePickerState) -> Unit) { messageText = block }

    /** Sets the message text builder that will be used for send a message. By default — «Select a date». */
    fun messageText(block: MessageText.() -> Unit) { messageText = {_, _, _ -> block() } }




    var additionalRows: suspend MessageInlineKeyboard.(botConfig: BotConfig, data: String?, state: DatePickerState) -> Unit = { _, _, _ -> }
        private set

    /**
     * Adds additional keyboard rows builder
     *
     * @param botConfig current bot config
     * @param data optional custom data provided when sending a message
     * @param state Current picker view state ([DatePickerState.DAY], [DatePickerState.MONTH] or [DatePickerState.YEAR])
     * */
    fun additionalRows(block: suspend MessageInlineKeyboard.(botConfig: BotConfig, data: String?, state: DatePickerState) -> Unit) { additionalRows = block }



    var additionalRowsPosition: DPSettingWithState<AdditionalRowsPosition> = { _, _, _ -> AdditionalRowsPosition.BOTTOM }
        private set

    /**
     * Sets additional keyboard rows position — [AdditionalRowsPosition.TOP] or [AdditionalRowsPosition.BOTTOM] (by default).
     *
     * @param botConfig current bot config
     * @param data optional custom data provided when sending a message
     * @param state Current picker view state ([DatePickerState.DAY], [DatePickerState.MONTH] or [DatePickerState.YEAR])
     * */
    fun additionalRowsPosition(block: DPSettingWithState<AdditionalRowsPosition>) { additionalRowsPosition = block }

    /** Sets additional keyboard rows position — [AdditionalRowsPosition.TOP] or [AdditionalRowsPosition.BOTTOM] (by default) */
    fun additionalRowsPosition(additionalRowsPosition: AdditionalRowsPosition) { this.additionalRowsPosition = { _, _, _ -> additionalRowsPosition } }



    var timeZone: suspend (botConfig: BotConfig, data: String?) -> ZoneId = { _, _ -> ZoneId.systemDefault() }
        private set

    /**
     * Sets time zone to determine current datetime. By default — from the bot config.
     *
     * @param botConfig current bot config
     * @param data optional custom data provided when sending a message
     * @see BotConfig.locale
     * */
    fun timeZone(block: DPSetting<ZoneId>) { timeZone = block }

    /**
     * Sets time zone to determine current datetime. By default — from the bot config.
     *
     * @see BotConfig.locale
     * */
    fun timeZone(timeZone: ZoneId) { this.timeZone = { _, _ -> timeZone } }



    var locale: suspend (botConfig: BotConfig, data: String?) -> Locale = { botConfig, _ -> botConfig.locale }
        private set

    /**
     * Sets locale to localize months and days of week
     *
     * @param botConfig current bot config
     * @param data optional custom data provided when sending a message
     * */
    fun locale(block: DPSetting<Locale>) { locale = block }

    /** Sets locale to localize months and days of week */
    fun locale(locale: Locale) { this.locale = { _, _ -> locale } }



    var firstDayOfWeek: DPSetting<DayOfWeek> = { c, d -> WeekFields.of(locale(c, d)).firstDayOfWeek }
        private set

    /**
     * Sets first day of week. By default — from date picker locale.
     *
     * @param botConfig current bot config
     * @param data optional custom data provided when sending a message
     * */
    fun firstDayOfWeek(block: DPSetting<DayOfWeek>) { firstDayOfWeek = block }

    /** Sets first day of week. By default — from date picker locale. */
    fun firstDayOfWeek(firstDayOfWeek: DayOfWeek) { this.firstDayOfWeek = { _, _ -> firstDayOfWeek } }



    var startWithState: suspend (botConfig: BotConfig, data: String?) -> DatePickerState = { _, _ -> DatePickerState.DAY }
        private set

    /**
     * Sets the view state ([DatePickerState.DAY], [DatePickerState.MONTH] or [DatePickerState.YEAR])
     * that will be applied when the date picker is sent.
     *
     * @param botConfig current bot config
     * @param data optional custom data provided when sending a message
     * */
    fun startWithState(block: DPSetting<DatePickerState>) { startWithState = block }

    /**
     * Sets the view state ([DatePickerState.DAY], [DatePickerState.MONTH] or [DatePickerState.YEAR])
     * that will be applied when the date picker is sent.
     * */
    fun startWithState(startWithState: DatePickerState) { this.startWithState = { _, _ -> startWithState } }



    /**
     * Returns dates range
     *
     * @param botConfig current bot config
     * @param data optional custom data provided when sending a message
     * */
    var dates: suspend (botConfig: BotConfig, data: String?) -> ClosedRange<LocalDate> = { _, _ -> LocalDate.of(1900, 1, 1)..LocalDate.of(2099, 12, 31) }
        private set

    /**
     * Sets dates range. 1990-01-01 — 2099-01-01 by default.
     *
     * @param botConfig current bot config
     * @param data optional custom data provided when sending a message
     * */
    fun dates(block: DPSetting<ClosedRange<LocalDate>>) { dates = block }

    /** Sets dates range. 1990-01-01 — 2099-01-01 by default. */
    fun dates(dates: ClosedRange<LocalDate>) { this.dates = { _, _ -> dates } }

    /** Sets dates range. 1990-01-01 — 2099-01-01 by default. */
    fun dates(dateFrom: LocalDate, dateTo: LocalDate) { this.dates = { _, _ -> dateFrom..dateTo } }



    var yearsColumns: suspend (botConfig: BotConfig, data: String?) -> Int = { _, _ -> 5 }
        private set

    /**
     * Sets count of columns for [DatePickerState.YEAR] view. 5 by default.
     *
     * @param botConfig current bot config
     * @param data optional custom data provided when sending a message
     * */
    fun yearsColumns(block: DPSetting<Int>) { yearsColumns = block }

    /** Sets count of columns for [DatePickerState.YEAR] view. 5 by default. */
    fun yearsColumns(yearsColumns: Int) { this.yearsColumns = { _, _ -> yearsColumns } }



    var yearsRows: suspend (botConfig: BotConfig, data: String?) -> Int = { _, _ -> 10 }
        private set

    /**
     * Sets count of rows for [DatePickerState.YEAR] view. 10 by default.
     *
     * @param botConfig current bot config
     * @param data optional custom data provided when sending a message
     * */
    fun yearsRows(block: DPSetting<Int>) { yearsRows = block }

    /** Sets count of rows for [DatePickerState.YEAR] view. 10 by default. */
    fun yearsRows(yearsRows: Int) { this.yearsRows = { _, _ -> yearsRows } }



    var monthPickerEnabled: suspend (botConfig: BotConfig, data: String?) -> Boolean = { _, _ -> true }
        private set

    /** Sets whether the [DatePickerState.MONTH] view can be displayed. *true* by default */
    fun monthPickerEnabled(block: DPSetting<Boolean>) { monthPickerEnabled = block }

    /** Sets whether the [DatePickerState.MONTH] view can be displayed. *true* by default */
    fun monthPickerEnabled(monthPickerEnabled: Boolean) { this.monthPickerEnabled = { _, _ -> monthPickerEnabled } }



    var yearsPickerEnabled: suspend (botConfig: BotConfig, data: String?) -> Boolean = { _, _ -> true }
        private set

    /** Sets whether the [DatePickerState.YEAR] view can be displayed. *true* by default */
    fun yearsPickerEnabled(block: DPSetting<Boolean>) { yearsPickerEnabled = block }

    /** Sets whether the [DatePickerState.YEAR] view can be displayed. *true* by default */
    fun yearsPickerEnabled(yearsPickerEnabled: Boolean) { this.yearsPickerEnabled = { _, _ -> yearsPickerEnabled } }



    init {
        // 1 symbol for result prefix,
        // TODO [date piker | BC. support] add n symbol reserve (calculate for locale, e.g ru -> до н.э. (7), us -> BC (2))
        //  ! if data for setupMarkup is release, than this require is redundant and should be deleted
        require(callbackQueryPrefix.length <= 52) { "Callback query prefix for date picker is too long" }
    }

    internal suspend fun setupMarkup(botConfig: BotConfig, messageInlineKeyboard: MessageInlineKeyboard, data: String?) {
        val now = LocalDate.now(timeZone(botConfig, data))
        val startWithState = startWithState(botConfig, data)
        messageInlineKeyboard.setupMarkup(startWithState, botConfig, data) {
            when(startWithState) {
                DatePickerState.DAY -> setupDaysMarkup(botConfig, now.year, now.monthValue, data)
                DatePickerState.MONTH -> setupMonthsMarkup(botConfig, now.year, data, now)
                DatePickerState.YEAR -> setupYearsMarkup(now.year, botConfig, data, now)
            }
        }
    }

    private suspend fun MessageInlineKeyboard.setupMarkup(state: DatePickerState, botConfig: BotConfig, data: String?, setup: suspend MessageInlineKeyboard.() -> Unit) {
        val additionalRowsPosition = additionalRowsPosition(botConfig, data, state)
        if (additionalRowsPosition == AdditionalRowsPosition.TOP) {
            additionalRows(botConfig, data, state)
        }
        setup()
        if (additionalRowsPosition == AdditionalRowsPosition.BOTTOM) {
            additionalRows(botConfig, data, state)
        }
    }

    override suspend fun handle(handler: EventHandler, handled: Boolean) {
        if (!handled && handler is CallbackQueryHandler) {
            handler.apply {
                isDataStartWith(callbackQueryPrefix) { data ->
                    val datePickerData = DatePickerData.from(data)
                    val now by lazy { runBlocking { LocalDate.now(timeZone(botConfig, datePickerData.additionalData)) } }
                    when {
                        datePickerData.yearPage != null -> {
                            edit {
                                textWithEntities { messageText(this@isDataStartWith.botConfig, datePickerData.additionalData, DatePickerState.YEAR) }
                                inlineKeyboard {
                                    setupMarkup(DatePickerState.YEAR, this@isDataStartWith.botConfig, datePickerData.additionalData) {
                                        setupYearsMarkup(datePickerData.yearPage, this@isDataStartWith.botConfig, datePickerData.additionalData, now)
                                    }
                                }
                            }
                        }
                        datePickerData.y != null && datePickerData.m != null -> {
                            edit {
                                textWithEntities { messageText(this@isDataStartWith.botConfig, datePickerData.additionalData, DatePickerState.DAY) }
                                inlineKeyboard {
                                    setupMarkup(DatePickerState.DAY, this@isDataStartWith.botConfig, datePickerData.additionalData) {
                                        setupDaysMarkup(this@isDataStartWith.botConfig, datePickerData.y, datePickerData.m, datePickerData.additionalData)
                                    }
                                }
                            }
                        }
                        datePickerData.y != null && datePickerData.m == null -> {
                            edit {
                                textWithEntities { messageText(this@isDataStartWith.botConfig, datePickerData.additionalData, DatePickerState.MONTH) }
                                inlineKeyboard {
                                    setupMarkup(DatePickerState.MONTH, this@isDataStartWith.botConfig, datePickerData.additionalData) {
                                        setupMonthsMarkup(this@isDataStartWith.botConfig, datePickerData.y, datePickerData.additionalData, now)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun Month.localized(locale: Locale, style: TextStyle = TextStyle.FULL_STANDALONE) = getDisplayName(style, locale)
    private fun Number.toIso() = if (this.toInt() < 10) "0$this" else this.toString()
    private fun Number.toIso(isYear: Boolean = false) = this.toString().padStart(if (isYear) 4 else 2, '0')
    private fun getMonthsDiff(year: Int, month: Int, date: LocalDate, now: LocalDate): Long {
        val maxLength = Month.of(month).length(Year.of(year).isLeap)
        val day = if (maxLength < now.dayOfMonth) maxLength else now.dayOfMonth
        return ChronoUnit.MONTHS.between(LocalDate.of(year, month, day), date)
    }

    private fun getFirstDate(datesRange: ClosedRange<LocalDate>, now: LocalDate) = now
        .withYear(datesRange.start.year)
        .withMonth(datesRange.start.monthValue)
    private fun getLastDate(datesRange: ClosedRange<LocalDate>, now: LocalDate) = now
        .withYear(datesRange.endInclusive.year)
        .withMonth(datesRange.endInclusive.monthValue)

    private suspend fun MessageInlineKeyboard.setupYearsMarkup(fromYear: Int, botConfig: BotConfig, data: String?, now: LocalDate) {
        val datesRange = dates(botConfig, data)
        val firstYear = getFirstDate(datesRange, now).year
        val lastYear = getLastDate(datesRange, now).year
        val yearsColumns = yearsColumns(botConfig, data)
        val yearsRows = yearsRows(botConfig, data)

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

    private suspend fun MessageInlineKeyboard.setupMonthsMarkup(botConfig: BotConfig, year: Int, data: String?, now: LocalDate) {
        val locale = locale(botConfig, data)
        val datesRange = dates(botConfig, data)
        val firstDate = getFirstDate(datesRange, now)
        val lastDate = getLastDate(datesRange, now)

        row {
            if (firstDate.year < year) button("«", "${callbackQueryPrefix}{${data ?: ""}}y${(year - 1).toIso(true)}")
            else button(" ", CallbackQuery.EMPTY_CALLBACK_DATA)

            button(year.toString(), if (yearsPickerEnabled(botConfig, data)) callbackQueryPrefix + "{${data ?: ""}}p${year}" else CallbackQuery.EMPTY_CALLBACK_DATA)

            if (lastDate.year > year) button("»", "${callbackQueryPrefix}{${data ?: ""}}y${(year + 1).toIso(true)}")
            else button(" ", CallbackQuery.EMPTY_CALLBACK_DATA)
        }

        val monthSequence = generateSequence(Month.of(1)) { it + 1 }
        monthSequence.take(12).chunked(4).toList().map { row ->
            row {
                row.forEach {
                    val diffWithLastDate = getMonthsDiff(year, it.value, lastDate, now)
                    val diffWithFirstDate = getMonthsDiff(year, it.value, firstDate, now)
                    val clickable = diffWithLastDate >= 0 && diffWithFirstDate <= 0

                    val title = if (clickable) if (year == now.year && it.value == now.monthValue) "·${it.localized(locale)}·" else it.localized(locale) else " "
                    val callbackQuery = if (clickable) "${callbackQueryPrefix}{${data ?: ""}}y${year.toIso(true)}m${it.value.toIso()}" else CallbackQuery.EMPTY_CALLBACK_DATA
                    button(title, callbackQuery)
                }
            }
        }
    }

    private suspend fun MessageInlineKeyboard.setupDaysMarkup(
        botConfig: BotConfig,
        callbackYear: Int,
        callbackMonth: Int,
        data: String?
    ) {
        val datesRange = dates(botConfig, data)
        val locale = locale(botConfig, data)
        val now = LocalDate.now(timeZone(botConfig, data))

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
            if (allowPastByDates) {
                val updatedYear = if (month == 1) year - 1 else year
                val updatedMonth = if (month == 1) 12 else month - 1
                button("«", "${callbackQueryPrefix}{${data ?: ""}}y${updatedYear.toIso(true)}m${updatedMonth.toIso()}")
            } else button(" ", CallbackQuery.EMPTY_CALLBACK_DATA)

            button(
                "${Month.of(month).localized(locale, TextStyle.SHORT_STANDALONE)} $year",
                if (monthPickerEnabled(botConfig, data)) "${callbackQueryPrefix}{${data ?: ""}}y${year.toIso(true)}"
                else CallbackQuery.EMPTY_CALLBACK_DATA
            )

            if (allowFutureByDates) {
                val updatedYear = if (month == 12) year + 1 else year
                val updatedMonth = if (month == 12) 1 else month + 1
                button("»", "${callbackQueryPrefix}{${data ?: ""}}y${updatedYear.toIso(true)}m${updatedMonth.toIso()}")
            } else button(" ", CallbackQuery.EMPTY_CALLBACK_DATA)
        }

        val yearMonth = YearMonth.of(year, month)

        // Determine the first day of the week based on the locale
        val firstDayOfWeek = firstDayOfWeek(botConfig, data)
        val firstDayOfMonth = yearMonth.atDay(1).dayOfWeek
        val daysBetweenFirstDayAndFirstWeekday = (firstDayOfMonth.value - firstDayOfWeek.value + 7) % 7

        val postfixDays = (7 - (yearMonth.atEndOfMonth().dayOfWeek.value - firstDayOfWeek.value + 1) % 7) % 7

        val fixedDays = buildList {
            if (daysBetweenFirstDayAndFirstWeekday > 0) {
                repeat(daysBetweenFirstDayAndFirstWeekday) {
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
                repeat(postfixDays) {
                    add(-1)
                }
            }
        }

        val dayOfWeekSequence = generateSequence(firstDayOfWeek) { it.plus(1) }.take(7).toList()
        fun DayOfWeek.localized() = getDisplayName(TextStyle.SHORT_STANDALONE, locale)

        // Day of week headers
        row {
            dayOfWeekSequence.forEach {
                button(it.localized(), CallbackQuery.EMPTY_CALLBACK_DATA)
            }
        }

        val daysChunks = fixedDays.chunked(7)

        daysChunks.forEach { chunk ->
            row {
                chunk.forEach {
                    if (it == -1) button(" ", CallbackQuery.EMPTY_CALLBACK_DATA)
                    else {
                        button(
                            text = if (isCurrentMonth && it == now.dayOfMonth) "·$it·" else it.toString(),
                            callbackData = "r${callbackQueryPrefix}{${data ?: ""}}y${year.toIso()}m${month.toIso()}d${it.toIso()}"
                        )
                    }
                }
            }
        }

        // Avoid keyboard size changes
        repeat(6 - daysChunks.size) {
            row {
                repeat(7) {
                    button(" ", CallbackQuery.EMPTY_CALLBACK_DATA)
                }
            }
        }
    }
}