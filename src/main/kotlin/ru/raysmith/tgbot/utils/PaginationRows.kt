package ru.raysmith.tgbot.utils

import ru.raysmith.tgbot.model.bot.MessageInlineKeyboard
import ru.raysmith.utils.PropertiesFactory

class PaginationRows(
    private val keyboard: MessageInlineKeyboard,
    private val pageFirstQuery: String,
    private val pageQuery: String,
    private val pageLastQuery: String
) {

    companion object {
        const val PAGE_FIRST = -1
        const val PAGE_LAST = -2

        private val propertiesManager = PropertiesFactory.from("bot.properties")

        val defaultRows: Int = propertiesManager.getOrDefault("pagination.rows", "5")
            .toIntOrNull() ?: throw IllegalArgumentException("Property pagination.rows is not Int")
        val defaultColumns: Int = propertiesManager.getOrDefault("pagination.columns", "1")
            .toIntOrNull() ?: throw IllegalArgumentException("Property pagination.columns is not Int")
        val firstPageSymbol = propertiesManager.getOrDefault("pagination.firstpagesymbol", "«")
        val lastPageSymbol = propertiesManager.getOrDefault("pagination.lastpagesymbol", "»")
    }

    var rows = defaultRows
    var columns = defaultColumns
    var addPagesRow = true

    private val max_displayed_pages = 5
    private val pages_paddings = max_displayed_pages / 2


    fun setRows(rows: Int): PaginationRows {
        this.rows = rows
        return this
    }

    fun setColumns(columns: Int): PaginationRows {
        this.columns = columns
        return this
    }

    fun setAddPagesRow(addPagesRow: Boolean): PaginationRows {
        this.addPagesRow = addPagesRow
        return this
    }

    fun <T> addRows(from: Iterable<T>, pageN: Int, buttonCreate: MessageInlineKeyboard.MessageInlineKeyboardRow.(item: T) -> Unit) {
        if (from.count() == 0) return
        val totalPages = (from.count() / (rows * columns)) + if (from.count() % (rows * columns) != 0) 1 else 0
        val page = when (pageN) {
            PAGE_FIRST -> 1
            PAGE_LAST -> totalPages
            else -> if (totalPages < pageN) 1 else if (pageN < 1) 1 else pageN
        }
        val lastIndex = ((page - 1) * rows * columns + rows * columns) - 1
        val isLastPage = lastIndex > from.count() - 1
        val range = IntRange((page - 1) * rows * columns, if (isLastPage) from.count() - 1 else lastIndex)
        from.filterIndexed { index, _ -> index in range }
            .let { items ->
                mutableListOf<MessageInlineKeyboard.MessageInlineKeyboardRow>().apply {
                    items.chunked(columns)
                        .map { row ->
                            keyboard.row {
                                row.forEach {
                                    buttonCreate(it)
                                }
                            }
                        }
                        .apply {
                            if (addPagesRow && !(page == 1 && isLastPage)) {
                                keyboard.row {
                                    if (page > pages_paddings + 1 && totalPages > max_displayed_pages) {
                                        button(firstPageSymbol, pageFirstQuery)
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
                                            button(
                                                if (it == page) "·$it·" else it.toString(),
                                                if (it == page) pageQuery else "$pageQuery$it"
                                            )
                                        }
                                    }

                                    if (lastPage < totalPages) button(lastPageSymbol, pageLastQuery)

                                }
                            }
                        }
                }
            }
    }
}