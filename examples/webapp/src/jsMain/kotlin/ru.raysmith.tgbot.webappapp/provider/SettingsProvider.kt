package ru.raysmith.tgbot.webappapp.provider

import js.objects.Object
import js.objects.unsafeJso
import react.*
import ru.raysmith.tgbot.webappapp.hooks.useLocalStorage
import web.cssom.Color

external interface SettingsContextProps : Props {
    var useTgTheme: Boolean
    var setToggleUseTgTheme: (Boolean) -> Unit
    var backgroundColor: String?
    var setBackgroundColor: (Color) -> Unit
}

external interface SettingsProviderProps : SettingsContextProps, PropsWithChildren

// ---------------------------------------------------------------------------------------------------------------------

val SettingsContext = createContext<SettingsContextProps>()
fun useSettingsContext(): SettingsContextProps = use(SettingsContext)
    ?: error("useSettingsContext must be use inside SettingsProvider")

// ---------------------------------------------------------------------------------------------------------------------

val SettingsProvider = FC<SettingsProviderProps> { props ->
    val settings = useLocalStorage("settings", unsafeJso<SettingsContextProps> {
        useTgTheme = true
    })

    val setToggleUseTgTheme = { value: Boolean ->
        settings.update("useTgTheme", value)
    }

    val setBackgroundColor = { color: Color ->
        settings.update("backgroundColor", color)
    }

    SettingsContext.Provider {
        value = useMemo(
            settings.reset,
            settings.update,
            settings.state
        ) {
            unsafeJso {
                Object.assign(this, settings.state)
                this.setToggleUseTgTheme = setToggleUseTgTheme
                this.setBackgroundColor = setBackgroundColor
            }
        }

        +props.children
    }
}
