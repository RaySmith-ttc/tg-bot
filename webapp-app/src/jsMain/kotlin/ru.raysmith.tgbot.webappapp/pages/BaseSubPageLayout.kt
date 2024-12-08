package ru.raysmith.tgbot.webappapp.pages

import react.FC
import react.PropsWithChildren
import react.useEffectOnce
import ru.raysmith.tgbot.hooks.useBackButton

val BaseSubPageLayout = FC<PropsWithChildren> { props ->
    val backButton = useBackButton()

    useEffectOnce {
        backButton.show()
    }

    +props.children
}