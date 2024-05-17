package ru.raysmith.tgbot.utils.pagination

import ru.raysmith.tgbot.core.Bot
import ru.raysmith.tgbot.core.BotHolder
import ru.raysmith.tgbot.model.bot.message.keyboard.MessageInlineKeyboard
import ru.raysmith.tgbot.model.network.CallbackQuery
import ru.raysmith.tgbot.utils.getOrDefault

// TODO
//  • [completed] create new module with exposed extensions; use a lambda that returns list query (e.g. by last id) with offset method by default
//  • [completed] delete exposed dependencies
//  • impl callback data in pages + fetcher (possibility to optimize SizedIterable.limit(count, offset) that can be replaced with .adjustWhere { // some logic with sorting })
//  • docs
class Pagination<T>(
    override val bot: Bot,
    private val data: Iterable<T>,
    private val callbackQueryPrefix: String,
    private val createButtons: suspend MessageInlineKeyboard.Row.(item: T) -> Unit
): BotHolder {

    companion object {
        const val PAGE_FIRST = -1
        const val PAGE_LAST = -2

        const val SYMBOL_PAGE_PREFIX = 'p'
        const val SYMBOL_CURRENT_PAGE = '·'

        val defaultRows: Int = Bot.properties.getOrDefault("pagination.rows", "5").toIntOrNull()
            ?: throw IllegalArgumentException("Property pagination.rows is not Int")
        val defaultColumns: Int = Bot.properties.getOrDefault("pagination.columns", "1").toIntOrNull()
            ?: throw IllegalArgumentException("Property pagination.columns is not Int")
        val firstPageSymbol = Bot.properties.getOrDefault("pagination.firstpagesymbol", "«")
        val lastPageSymbol = Bot.properties.getOrDefault("pagination.lastpagesymbol", "»")

        // TODO use in addRows? [docs]
//        fun <T> itemsFor(pagination: Pagination<T>, page: Int, fetcher: PaginationFetcher<T> = bot.config.paginationFetcherFactory.getFetcher()): List<T> {
//            val chunkSize = pagination.rows * pagination.columns
//            val dataCount = fetcher.getCount(pagination.data)
//            if (dataCount == 0) return emptyList()
//            val totalPages = ((dataCount / chunkSize) + if (dataCount % chunkSize != 0) 1 else 0)
//            val fixedPages: Int = when (page) {
//                PAGE_FIRST -> 1
//                PAGE_LAST -> totalPages
//                else -> if (totalPages < page) totalPages else if (page < 1) 1 else page
//            }
//
//            val offset = (fixedPages - 1) * chunkSize
//            return fetcher.fetchData(pagination.data, fixedPages, offset, chunkSize, pagination.rows, pagination.columns).toList()
//        }

        // TODO [docs]
        suspend fun <T> create(
            bot: Bot,
            data: Iterable<T>,
            callbackQueryPrefix: String,
            page: Int = PAGE_FIRST,
            setup: suspend Pagination<T>.() -> Unit = {},
            createButtons: suspend MessageInlineKeyboard.Row.(item: T) -> Unit
        ) = Pagination(bot, data, callbackQueryPrefix, createButtons)
            .apply { setup() }
            .apply { this.startPage = page }
    }

    var fetcher = bot.botConfig.paginationFetcherFactory.getFetcher<T>()
    var rows = defaultRows
    var columns = defaultColumns
    var addPagesRow = true
    var startPage = PAGE_FIRST

    private val handlerId = "pagination_$callbackQueryPrefix"

    private val max_displayed_pages = 5
    private val pages_paddings = max_displayed_pages / 2

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

    private suspend fun MessageInlineKeyboard.addRows(pageN: Int, createButtons: suspend MessageInlineKeyboard.Row.(item: T) -> Unit) {
        val chunkSize = this@Pagination.rows * columns
        val dataCount = fetcher.getCount(data)
        if (dataCount == 0) return
        val totalPages = ((dataCount / chunkSize) + if (dataCount % chunkSize != 0) 1 else 0)
        val page = when (pageN) {
            PAGE_FIRST -> 1
            PAGE_LAST -> totalPages
            else -> if (totalPages < pageN) totalPages else if (pageN < 1) 1 else pageN
        }

        val offset = (page - 1) * chunkSize

        val lastIndex = (offset + chunkSize) - 1
        val isLastPage = lastIndex > dataCount - 1

        fetcher.fetchData(data, page, offset, chunkSize, this@Pagination.rows, columns).also { items ->
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
                    if (addPagesRow && !(page == 1 && isLastPage)) {
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
                                if (totalPages > max_displayed_pages && it != 0) firstPage += it
                            }

                            // page buttons
                            if (totalPages > 1) {
                                (firstPage..lastPage).forEach {
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