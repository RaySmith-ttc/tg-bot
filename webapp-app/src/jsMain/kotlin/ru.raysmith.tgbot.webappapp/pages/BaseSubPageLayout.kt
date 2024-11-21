package ru.raysmith.tgbot.webappapp.pages

import react.FC
import react.PropsWithChildren
import react.useEffectOnce
import ru.raysmith.tgbot.hooks.useBackButton
import ru.raysmith.tgbot.webappapp.components.BottomAppBar

val BaseSubPageLayout = FC<PropsWithChildren> { props ->
    val backButton = useBackButton()

    useEffectOnce {
        backButton.show()
    }

    BottomAppBar {

    }

    +props.children
}