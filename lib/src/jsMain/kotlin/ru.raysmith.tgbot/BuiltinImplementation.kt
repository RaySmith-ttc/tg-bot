package ru.raysmith.tgbot

/**
 * This annotation marks a hook that is a built-in implementation of WebApp functionality. It may not meet optimization
 * requirements, use experimental API and not enough tested.
 * */
@RequiresOptIn
@Target(AnnotationTarget.FUNCTION)
annotation class BuiltinImplementation