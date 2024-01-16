package ru.raysmith.tgbot.utils

// TODO replace lib with Provider class:
//  allow to set to a config pagination provider that determines how to get the page data from the entire list
//  create new module with exposed extensions; use a lambda that returns list query (e.g. by last id) with offset method by default
import org.jetbrains.exposed.sql.SizedIterable
import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.model.bot.message.keyboard.MessageInlineKeyboard
import ru.raysmith.tgbot.model.network.CallbackQuery

class Pagination<T>(
    private val data: Iterable<T>,
    private val callbackQueryPrefix: String,
    private val createButtons: suspend MessageInlineKeyboard.Row.(item: T) -> Unit
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

        // TODO use in addRows? [docs]
        fun <T> itemsFor(pagination: Pagination<T>, page: Long): List<T> {
            val dataCount = if (pagination.data is SizedIterable) pagination.data.count() else pagination.data.count().toLong()
            if (dataCount == 0L) return emptyList()
            val totalPages = ((dataCount / (pagination.rows * pagination.columns)) + if (dataCount % (pagination.rows * pagination.columns) != 0L) 1 else 0)
            val fixedPages: Long = when (page) {
                PAGE_FIRST -> 1L
                PAGE_LAST -> totalPages
                else -> if (totalPages < page) totalPages else if (page < 1) 1 else page
            }

            val offset = (fixedPages - 1) * pagination.rows * pagination.columns
            val dataList = pagination.data.let {
                if (it is SizedIterable) {
                    it.limit(pagination.rows * pagination.columns, offset).toList()
                } else it.toList()
            }

            val lastIndex = (offset + pagination.rows * pagination.columns) - 1
            val isLastPage = lastIndex > dataCount - 1
            val range = if (pagination.data is SizedIterable) {
                LongRange(0, (pagination.rows * pagination.columns).toLong())
            } else {
                LongRange(offset, if (isLastPage) dataCount - 1L else lastIndex)
            }

            return dataList.filterIndexed { index, _ -> index in range }
        }

        // TODO [docs]
        suspend fun <T> create(
            data: Iterable<T>,
            callbackQueryPrefix: String,
            page: Long = PAGE_FIRST,
            setup: suspend Pagination<T>.() -> Unit = {},
            createButtons: suspend MessageInlineKeyboard.Row.(item: T) -> Unit
        ) = Pagination(data, callbackQueryPrefix, createButtons)
            .apply { setup() }
            .apply { this.startPage = page }
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

    suspend fun setupMarkup(keyboard: MessageInlineKeyboard) {
        keyboard.addRows(startPage, createButtons)
    }

    private suspend fun MessageInlineKeyboard.addRows(pageN: Long, createButtons: suspend MessageInlineKeyboard.Row.(item: T) -> Unit) {
        val dataCount = if (data is SizedIterable) data.count() else data.count().toLong()
        if (dataCount == 0L) return
        val totalPages = ((dataCount / (this@Pagination.rows * columns)) + if (dataCount % (this@Pagination.rows * columns) != 0L) 1 else 0)
        val page: Long = when (pageN) {
            PAGE_FIRST -> 1L
            PAGE_LAST -> totalPages
            else -> if (totalPages < pageN) totalPages else if (pageN < 1) 1 else pageN
        }

        val offset = (page - 1) * this@Pagination.rows * columns
        val dataList = this@Pagination.data.let {
            if (it is SizedIterable) {
                it.limit(this@Pagination.rows * columns, offset).toList()
            } else it.toList()
        }

        val lastIndex = (offset + this@Pagination.rows * columns) - 1
        val isLastPage = lastIndex > dataCount - 1
        val range = if (data is SizedIterable) {
            LongRange(0, (this@Pagination.rows * columns).toLong())
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

                            // last page in a row for current state
                            val lastPage = ((firstPage + max_displayed_pages) - 1).coerceAtMost(totalPages)

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