package ru.raysmith.tgbot.utils.pagination

/** Factory of [PaginationFetcher] */
interface PaginationFetcherFactory {
    fun <T> getFetcher(): PaginationFetcher<T>
}