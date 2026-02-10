package ru.raysmith.tgbot.hooks

import kotlinx.js.JsPlainObject
import react.useEffectOnce
import react.useMemo
import react.useState
import ru.raysmith.tgbot.ColorScheme
import ru.raysmith.tgbot.CssVar
import ru.raysmith.tgbot.events.EventType
import ru.raysmith.tgbot.webApp
import web.cssom.Color

/** This hook contains the user's current theme settings */
fun useThemeParams(): ThemeParamsHookType {

    var colorScheme by useState(webApp.colorScheme)
    var themeParams by useState(webApp.themeParams)
    var headerColor by useState(webApp.headerColor)
    var backgroundColor by useState(webApp.backgroundColor)
    var bottomBarColor by useState(webApp.bottomBarColor)

    // themeChanged
    useEffectOnce {
        webApp.onEvent(EventType.themeChanged) {
            colorScheme = webApp.colorScheme
            themeParams = webApp.themeParams
            headerColor = webApp.headerColor
            backgroundColor = webApp.backgroundColor
            bottomBarColor = webApp.bottomBarColor
        }
    }

    val setHeaderColor = { color: Color ->
        webApp.setHeaderColor(color)
        headerColor = webApp.headerColor
    }

    val setBackgroundColor = { color: Color ->
        webApp.setBackgroundColor(color)
        backgroundColor = webApp.backgroundColor
    }

    val setBottomBarColor = { color: Color ->
        webApp.setBottomBarColor(color)
        bottomBarColor = webApp.bottomBarColor
    }

    return useMemo(
        colorScheme,
        themeParams,
        headerColor,
        backgroundColor,
        bottomBarColor
    ) {
        ThemeParamsHookType(
            colorScheme = colorScheme,
            bgColor = themeParams.bgColor,
            textColor = themeParams.textColor,
            hintColor = themeParams.hintColor,
            linkColor = themeParams.linkColor,
            buttonColor = themeParams.buttonColor,
            buttonTextColor = themeParams.buttonTextColor,
            secondaryBgColor = themeParams.secondaryBgColor,
            headerBgColor = themeParams.headerBgColor,
            bottomBarBgColor = themeParams.bottomBarBgColor,
            accentTextColor = themeParams.accentTextColor,
            sectionBgColor = themeParams.sectionBgColor,
            sectionHeaderTextColor = themeParams.sectionHeaderTextColor,
            sectionSeparatorColor = themeParams.sectionSeparatorColor,
            subtitleTextColor = themeParams.subtitleTextColor,
            destructiveTextColor = themeParams.destructiveTextColor,
            headerColor = headerColor,
            backgroundColor = backgroundColor,
            bottomBarColor = bottomBarColor,
            setHeaderColor = setHeaderColor,
            setBackgroundColor = setBackgroundColor,
            setBottomBarColor = setBottomBarColor,
        )
    }
}

@JsPlainObject
external interface ThemeParamsHookType {

    /**
     * The color scheme currently used in the Telegram app. Also available as the CSS variable [CssVar.tgColorScheme].
     * */
    val colorScheme: ColorScheme

    /** Background color in the `#RRGGBB` format. Also available as the CSS variable [CssVar.tgThemeBgColor]. */
    val bgColor: Color?

    /** Main text color in the `#RRGGBB` format. Also available as the CSS variable [CssVar.tgThemeTextColor]. */
    val textColor: Color?

    /** Hint text color in the `#RRGGBB` format. Also available as the CSS variable [CssVar.tgThemeHintColor]. */
    val hintColor: Color?

    /** Link text color in the `#RRGGBB` format. Also available as the CSS variable [CssVar.tgThemeLinkColor]. */
    val linkColor: Color?

    /** Button color in the `#RRGGBB` format. Also available as the CSS variable [CssVar.tgThemeButtonColor]. */
    val buttonColor: Color?

    /**
     * Button text color in the `#RRGGBB` format. Also available as the CSS variable
     * [CssVar.tgThemeButtonTextColor].
     * */
    val buttonTextColor: Color?

    /**
     * Secondary background color in the `#RRGGBB` format. Also available as the CSS variable
     * [CssVar.tgThemeSecondaryBgColor].
     *
     * @since Bot API 6.1
     * */
    val secondaryBgColor: Color?

    /**
     * Header background color in the `#RRGGBB` format. Also available as the CSS variable
     * [CssVar.tgThemeHeaderBgColor].
     *
     * @since Bot API 7.0
     * */
    val headerBgColor: Color?

    /**
     * Bottom background color in the `#RRGGBB` format. Also available as the CSS variable
     * [CssVar.tgThemeBottomBarBgColor].
     *
     * @since Bot API 7.10
     * */
    val bottomBarBgColor: Color?

    /**
     * Accent text color in the `#RRGGBB` format. Also available as the CSS variable
     * [CssVar.tgThemeAccentTextColor].
     *
     * @since Bot API 7.0
     * */
    val accentTextColor: Color?

    /**
     * Background color for the section in the `#RRGGBB` format. It is recommended to use this in conjunction with
     * [secondaryBgColor]. Also available as the CSS variable [CssVar.tgThemeSectionBgColor].
     *
     * @since Bot API 7.0
     * */
    val sectionBgColor: Color?

    /**
     * Header text color for the section in the `#RRGGBB` format. Also available as the CSS variable
     * [CssVar.tgThemeSectionHeaderTextColor].
     *
     * @since Bot API 7.0
     * */
    val sectionHeaderTextColor: Color?

    /**
     * Section separator color in the `#RRGGBB` format. Also available as the CSS variable
     * [CssVar.tgThemeSectionSeparatorColor].
     *
     * @since Bot API 7.6
     * */
    val sectionSeparatorColor: Color?

    /**
     * Subtitle text color in the `#RRGGBB` format. Also available as the CSS variable
     * [CssVar.tgThemeSubtitleTextColor].
     *
     * @since Bot API 7.0
     * */
    val subtitleTextColor: Color?

    /**
     * Text color for destructive actions in the `#RRGGBB` format. Also available as the CSS variable
     * [CssVar.tgThemeDestructiveTextColor].
     *
     * @since Bot API 7.0
     * */
    val destructiveTextColor: Color?

    /** Current header color in the `#RRGGBB` format. */
    val headerColor: Color

    /** Current background color in the `#RRGGBB` format. */
    val backgroundColor: Color

    /** Current bottom bar color in the `#RRGGBB` format. */
    val bottomBarColor: Color

    /**
     * A method that sets the app header color in the `#RRGGBB` format. You can also use keywords *bg_color* and
     * *secondary_bg_color*.
     *
     * Up to `Bot API 6.9` You can only pass [bgColor] or [secondaryBgColor] as a color or *bg_color*,
     * *secondary_bg_color* keywords.
     *
     * @since Bot API 6.1
     * */
    val setHeaderColor: (color: Color) -> Unit

    /**
     * A method that sets the app background color in the `#RRGGBB` format. You can also use keywords *bg_color* and
     * *secondary_bg_color*.
     *
     * @since Bot API 6.1
     * */
    val setBackgroundColor: (color: Color) -> Unit

    /**
     * A method that sets the app's bottom bar color in the `#RRGGBB` format. You can also use the keywords *bg_color*,
     * *secondary_bg_color* and *bottom_bar_bg_color*. This color is also applied to the navigation bar on Android.
     *
     * @since Bot API 7.10
     * */
    val setBottomBarColor: (color: Color) -> Unit
}
