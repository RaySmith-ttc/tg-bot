package ru.raysmith.tgbot.core

suspend fun ISender.send(text: String) = send { this.text = text }
suspend fun IEditor.edit(text: String) = edit { this.text = text }