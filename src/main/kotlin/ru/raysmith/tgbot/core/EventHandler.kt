package ru.raysmith.tgbot.core

import ru.raysmith.tgbot.model.network.message.Message

interface EventHandler : ChatID {
    suspend fun handle()

    fun sendInvoice(chatId: String = this.chatId!!, invoice: SendInvoice.() -> Unit): Message {
        return SendInvoice().apply(invoice).send(chatId).body()!!.result
    }
}

