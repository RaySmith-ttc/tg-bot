package ru.raysmith.tgbot.utils

import org.jetbrains.exposed.sql.SizedIterable
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.model.bot.MessageInlineKeyboard
import ru.raysmith.tgbot.model.network.CallbackQuery

class Pagination<T>(
    private val data: Iterable<T>,
    private val callbackQueryPrefix: String,
    private val createButtons: MessageInlineKeyboard.MessageInlineKeyboardRow.(item: T) -> Unit
) {

    companion object {
        const val PAGE_FIRST = -1L
        const val PAGE_LAST = -2L

        const val SYMBOL_PAGE_PREFIX = 'p'
        const val SYMBOL_CURRENT_PAGE = '·'

        val defaultRows: Int = Bot.properties.getOrDefault("pagination.rows", "5").toIntOrNull()
            ?: throw IllegalArgumentException("Property pagination.rows is not Int")
        val defaultColumns: Int = Bot.properties.getOrDefault("pagination.columns", "1").toIntOrNull()
            ?: throw IllegalArgumentException("Property pagination.columns is not Int")
        val firstPageSymbol = Bot.properties.getOrDefault("pagination.firstpagesymbol", "«")
        val lastPageSymbol = Bot.properties.getOrDefault("pagination.lastpagesymbol", "»")
    }

    var rows = defaultRows
    var columns = defaultColumns
    var addPagesRow = true
    var startPage = PAGE_FIRST

    private val handlerId = "pagination_$callbackQueryPrefix"

    private val max_displayed_pages = 5
    private val pages_paddings = max_displayed_pages / 2L

    fun setRows(rows: Int): Pagination<T> {
        this.rows = rows
        return this
    }

    fun setColumns(columns: Int): Pagination<T> {
        this.columns = columns
        return this
    }

    fun setAddPagesRow(addPagesRow: Boolean): Pagination<T> {
        this.addPagesRow = addPagesRow
        return this
    }

    fun setupMarkup(keyboard: MessageInlineKeyboard) {
        keyboard.addRows(startPage, createButtons)
    }

    private fun MessageInlineKeyboard.addRows(pageN: Long, createButtons: MessageInlineKeyboard.MessageInlineKeyboardRow.(item: T) -> Unit) {
        val dataCount = if (data is SizedIterable) data.count() else data.count().toLong()
        if (dataCount == 0L) return
        val totalPages = ((dataCount / (rows * columns)) + if (dataCount % (rows * columns) != 0L) 1 else 0)
        val page: Long = when (pageN) {
            PAGE_FIRST -> 1L
            PAGE_LAST -> totalPages
            else -> if (totalPages < pageN) totalPages else if (pageN < 1) 1 else pageN
        }

        val offset = (page - 1) * rows * columns
        val dataList = this@Pagination.data.let {
            if (it is SizedIterable) {
                it.limit(rows * columns, offset).toList()
            } else it.toList()
        }

        val lastIndex = (offset + rows * columns) - 1
        val isLastPage = lastIndex > dataCount - 1
        val range = if (data is SizedIterable) {
            LongRange(0, (rows * columns).toLong())
        } else {
            LongRange(offset, if (isLastPage) dataCount - 1L else lastIndex)
        }

        dataList.filterIndexed { index, _ -> index in range }.let { items ->
            items.chunked(columns)
                .map { row ->
                    row {
                        row.forEach {
                            createButtons(it)
                        }
                    }
                }
                .apply {

                    // add a pages row if needed and it is enabled
                    if (addPagesRow && !(page == 1L && isLastPage)) {
                        row {

                            // [«] button
                            if (page > pages_paddings + 1 && totalPages > max_displayed_pages) {
                                button(firstPageSymbol, "$callbackQueryPrefix${SYMBOL_PAGE_PREFIX}1")
                            }

                            // first page in a row for current state
                            var firstPage = when {
                                totalPages <= max_displayed_pages -> 1
                                page < pages_paddings + 1 -> 1
                                page == totalPages -> (page - pages_paddings) - 1
                                else -> page - pages_paddings
                            }

                            // last page in a row for current state // TODO replace with coerceMethod
                            val lastPage =
                                if ((firstPage + max_displayed_pages) - 1 > totalPages) totalPages
                                else (firstPage + max_displayed_pages) - 1

                            (((lastPage - firstPage) - max_displayed_pages) + 1).let {
                                if (totalPages > max_displayed_pages && it != 0L) firstPage += it
                            }

                            // page buttons
                            if (totalPages > 1) {
                                LongRange(firstPage, lastPage).forEach {
                                    if (it == page) button("$SYMBOL_CURRENT_PAGE$it$SYMBOL_CURRENT_PAGE", CallbackQuery.EMPTY_CALLBACK_DATA)
                                    else button(it.toString(), "$callbackQueryPrefix$SYMBOL_PAGE_PREFIX$it")
                                }
                            }

                            // [»] button
                            if (lastPage < totalPages) button(lastPageSymbol, "$callbackQueryPrefix$SYMBOL_PAGE_PREFIX$totalPages")
                        }
                    }
                }
        }
    }
}