package ru.raysmith.tgbot.utils.pagination

interface PaginationFetcherFactory {
    fun <T> getFetcher(): PaginationFetcher<T>
}