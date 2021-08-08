package ru.raysmith.tgbot.utils

import ru.raysmith.tgbot.model.network.CallbackQuery
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardMarkup
import ru.raysmith.tgbot.model.network.keyboard.InlineKeyboardButton
import java.text.SimpleDateFormat
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*

val dateFormat = SimpleDateFormat("dd.MM.yyyy")
val dateTimeFormat = SimpleDateFormat("dd.MM HH:mm")
val dateTimeFormater = DateTimeFormatter.ofPattern("dd.MM.yyyy")
val shortDateFormat = SimpleDateFormat("dd.MM")

/**
 * Class for creating calendar keyboard.
 *
 * TODO docs
 *
 * */

// TODO refactoring, work with dsl objects, impl local
class CalendarKeyboardCreator(
    private val pageFirstQuery: String,
    private val pageQuery: String,
    private val pageLastQuery: String
) {

    companion object {

        const val PAGE_FIRST = -1
        const val PAGE_LAST = -2

        fun getYearRows(prefixCallbackData: String): MutableList<List<InlineKeyboardButton>> {
            val calendar = Calendar.getInstance()
            return mutableListOf(calendar.get(Calendar.YEAR).let { y ->
                mutableListOf<InlineKeyboardButton>().apply {
                    repeat(3) {
                        val yearString = (y - 2 + it).toString()
                        add(InlineKeyboardButton(yearString, "$prefixCallbackData$yearString"))
                    }
                }
            })
        }

        fun getMonthRows(prefixCallbackData: String): MutableList<List<InlineKeyboardButton>> {
            return mutableListOf(
                listOf(
                    InlineKeyboardButton("Январь", "${prefixCallbackData}1"),
                    InlineKeyboardButton("Февраль", "${prefixCallbackData}2"),
                    InlineKeyboardButton("Март", "${prefixCallbackData}3"),
                    InlineKeyboardButton("Апрель", "${prefixCallbackData}4"),
                ),
                listOf(
                    InlineKeyboardButton("Май", "${prefixCallbackData}5"),
                    InlineKeyboardButton("Июнь", "${prefixCallbackData}6"),
                    InlineKeyboardButton("Июль", "${prefixCallbackData}7"),
                    InlineKeyboardButton("Август", "${prefixCallbackData}8"),
                ),
                listOf(
                    InlineKeyboardButton("Сентябрь", "${prefixCallbackData}9"),
                    InlineKeyboardButton("Октябрь", "${prefixCallbackData}10"),
                    InlineKeyboardButton("Ноябрь", "${prefixCallbackData}11"),
                    InlineKeyboardButton("Декабрь", "${prefixCallbackData}12"),
                )
            )
        }

        fun getDayRows(
            year: Int,
            month: Int,
            prefixCallbackData: String,
            emptyDaySymbol: String = CallbackQuery.EMPTY_CALLBACK_DATA
        ): MutableList<List<InlineKeyboardButton>> {
            val yearMonth = YearMonth.of(year, month)
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
            return mutableListOf<List<InlineKeyboardButton>>().apply {
                fixedDays.chunked(7).forEach { chunk ->
                    add(chunk.map {
                        if (it == -1) InlineKeyboardButton(" ", "$prefixCallbackData$emptyDaySymbol")
                        else InlineKeyboardButton(it.toString(), "$prefixCallbackData$it")
                    })
                }
            }
        }
    }

    private var rows = 5
    private var columns = 1
    private var addPagesRow = true
    private var additionalRows: List<List<InlineKeyboardButton>> = listOf()
    private var inEnd: Boolean = true

    private val max_displayed_pages = 5
    private val pages_paddings = max_displayed_pages / 2


    fun setRows(rows: Int): CalendarKeyboardCreator {
        this.rows = rows
        return this
    }

    fun setColumns(columns: Int): CalendarKeyboardCreator {
        this.columns = columns
        return this
    }

    fun setAddPagesRow(addPagesRow: Boolean): CalendarKeyboardCreator {
        this.addPagesRow = addPagesRow
        return this
    }

    fun addAdditionalRows(rows: List<List<InlineKeyboardButton>>, inEnd: Boolean = true): CalendarKeyboardCreator {
        additionalRows = rows
        this.inEnd = inEnd
        return this
    }

    fun <T> create(from: Iterable<T>, pageN: Int, rowCreate: (T) -> InlineKeyboardButton): InlineKeyboardMarkup? {
        if (from.count() == 0 && additionalRows.isEmpty()) return null
        val totalPages = (from.count() / (rows * columns)) + if (from.count() % (rows * columns) != 0) 1 else 0
        val page = when (pageN) {
            PAGE_FIRST -> 1
            PAGE_LAST -> totalPages
            else -> if (totalPages < pageN) 1 else if (pageN < 1) 1 else pageN
        }
        val lastIndex = ((page - 1) * rows * columns + rows * columns) - 1
        val isLastPage = lastIndex > from.count() - 1
        val range = IntRange((page - 1) * rows * columns, if (isLastPage) from.count() - 1 else lastIndex)
        return InlineKeyboardMarkup(
            from.filterIndexed { index, _ -> index in range }
                .let { items ->
                    mutableListOf<List<InlineKeyboardButton>>().apply {
                        fun addAdditionalRows() = additionalRows.forEach { add(it) }

                        if (!inEnd) {
                            addAdditionalRows()
                        }

                        items.chunked(columns)
                            .map { row -> add(row.map { rowCreate(it) }) }
                            .apply {
                                if (addPagesRow && !(page == 1 && isLastPage)) {
                                    val row = listOf(
                                        mutableListOf<InlineKeyboardButton>().apply {
                                            if (page > pages_paddings + 1 && totalPages > max_displayed_pages) {
                                                add(InlineKeyboardButton("«", pageFirstQuery))
                                            }

                                            var firstPage = when {
                                                totalPages <= max_displayed_pages -> 1
                                                page < pages_paddings + 1 -> 1
                                                page == totalPages -> (page - pages_paddings) - 1
                                                else -> page - pages_paddings
                                            }

                                            val lastPage =
                                                if ((firstPage + max_displayed_pages) - 1 > totalPages) totalPages
                                                else (firstPage + max_displayed_pages) - 1

                                            (((lastPage - firstPage) - max_displayed_pages) + 1).let {
                                                if (totalPages > max_displayed_pages && it != 0) firstPage += it
                                            }

                                            if (totalPages > 1) {
                                                IntRange(firstPage, lastPage).forEach {
                                                    add(InlineKeyboardButton(
                                                        if (it == page) "[$it]" else it.toString(),
                                                        if (it == page) pageQuery else "$pageQuery$it"
                                                    ))
                                                }
                                            }

                                            if (lastPage < totalPages) add(InlineKeyboardButton("»", pageLastQuery))
                                        })
                                    addAll(row)
                                }
                            }

                        if (inEnd) {
                            addAdditionalRows()
                        }
                    }
                }
        )
    }
}