package ru.raysmith.tgbot.webappapp.provider

import js.objects.Object
import js.objects.jso
import react.*
import ru.raysmith.tgbot.webappapp.hooks.useLocalStorage

external interface SettingsContextProps : Props {
    var useTgTheme: Boolean
    var setToggleUseTgTheme: (Boolean) -> Unit
}

external interface SettingsProviderProps : SettingsContextProps, PropsWithChildren

// ---------------------------------------------------------------------------------------------------------------------

val SettingsContext = createContext<SettingsContextProps>()
fun useSettingsContext(): SettingsContextProps = useContext(SettingsContext)
    ?: error("useSettingsContext must be use inside SettingsProvider")

// ---------------------------------------------------------------------------------------------------------------------

val SettingsProvider = FC<SettingsProviderProps> { props ->
    val settings = useLocalStorage("settings", jso<SettingsContextProps> {
        useTgTheme = true
    })

    val setToggleUseTgTheme = { value: Boolean ->
        settings.update("useTgTheme", value)
    }

    SettingsContext.Provider {
        value = useMemo(
            settings.reset,
            settings.update,
            settings.state
        ) {
            jso {
                Object.assign(this, settings.state)
                this.setToggleUseTgTheme = setToggleUseTgTheme
            }
        }

        +props.children
    }
}