package ru.raysmith.tgbot.core

interface EventHandler {
    suspend fun handle()
}