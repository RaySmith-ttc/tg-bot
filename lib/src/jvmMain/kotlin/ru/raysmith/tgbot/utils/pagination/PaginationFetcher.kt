package ru.raysmith.tgbot.utils.pagination

/** Fetching data for pagination */
interface PaginationFetcher<T> {

    /** Returns elements count in [data] */
    fun getCount(data: Iterable<T>): Int = data.count()

    /** Returns data for [page] */
    fun fetchData(data: Iterable<T>, page: Int, offset: Int, count: Int, rows: Int, columns: Int): Iterable<T>
}