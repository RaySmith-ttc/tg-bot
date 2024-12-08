package ru.raysmith.tgbot.core

// TODO move to interfaces

suspend fun ISender.send(text: String) = send { this.text = text }
suspend fun IEditor.edit(text: String) = edit { this.text = text }