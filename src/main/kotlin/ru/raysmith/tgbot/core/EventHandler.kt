package ru.raysmith.tgbot.core

interface EventHandler : ChatIdHolder, IEditor, ISender {
    suspend fun handle()
}