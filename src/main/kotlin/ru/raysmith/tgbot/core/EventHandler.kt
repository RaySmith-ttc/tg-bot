package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.model.network.message.Message

interface EventHandler : ChatIdHolder, IEditor, ISender {
    suspend fun handle()

    fun sendInvoice(chatId: String = this.getChatIdOrThrow(), invoice: InvoiceSender.() -> Unit): Message {
        return InvoiceSender(service).apply(invoice).send(chatId).body()!!.result
    }
}