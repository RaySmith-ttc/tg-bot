package ru.raysmith.tgbot.network

import kotlinx.serialization.SerialName

internal object NetworkUtils {
    private inline fun <reified A : Annotation> Enum<*>.getEnumFieldAnnotation(): A? =
        javaClass.getDeclaredField(name).getAnnotation(A::class.java)

    fun Enum<*>.getSerialName(): String = getEnumFieldAnnotation<SerialName>()?.value ?: name
}