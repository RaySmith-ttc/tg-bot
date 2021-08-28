package ru.raysmith.tgbot.core

fun ISender.send(text: String) = send { this.text = text }
fun IEditor.edit(text: String) = edit { this.text = text }