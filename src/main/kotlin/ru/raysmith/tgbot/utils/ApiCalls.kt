package ru.raysmith.tgbot.utils

import ru.raysmith.tgbot.network.TelegramApi

@Suppress("BlockingMethodInNonBlockingContext")
fun getMe() = TelegramApi.service.getMe().execute().body()!!

@Suppress("BlockingMethodInNonBlockingContext")
fun getChat(id: String) = TelegramApi.service.getChat(id).execute().body()!!

@Suppress("BlockingMethodInNonBlockingContext")
fun deleteMessage(chatId: String, messageId: Long) = TelegramApi.service.deleteMessage(chatId, messageId).execute().body()!!