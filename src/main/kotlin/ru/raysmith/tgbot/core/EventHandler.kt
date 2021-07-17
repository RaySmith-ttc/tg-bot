package ru.raysmith.tgbot.core

interface EventHandler : ISender, IEditor {
    suspend fun handle()
}