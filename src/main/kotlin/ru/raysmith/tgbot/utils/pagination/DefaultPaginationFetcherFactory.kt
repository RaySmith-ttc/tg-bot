package ru.raysmith.tgbot.utils.pagination

// TODO docs
class DefaultPaginationFetcherFactory : PaginationFetcherFactory {
    override fun <T> getFetcher(): PaginationFetcher<T> {
        return object : PaginationFetcher<T> {
            override fun fetchData(data: Iterable<T>, page: Int, offset: Int, count: Int, rows: Int, columns: Int): Iterable<T> {
                val dataCount = getCount(data)
                val lastIndex = (offset + rows * columns) - 1
                val isLastPage = lastIndex > dataCount - 1
                val range = IntRange(offset, if (isLastPage) dataCount - 1 else lastIndex)

                return if (data is List<T>) {
                    data.subList(offset, if (isLastPage) dataCount else lastIndex)
                } else {
                    data.filterIndexed { index, _ -> index in range }
                }
            }
        }
    }
}