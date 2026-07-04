package ru.raysmith.tgbot.model.bot.message

@DslMarker
annotation class TextMessageDsl

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@DslMarker
annotation class MessageDsl