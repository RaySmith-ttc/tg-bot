package ru.raysmith.tgbot.hooks

import js.objects.jso
import react.useEffectOnce
import react.useMemo
import react.useState
import ru.raysmith.tgbot.*
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
        jso {
            this.colorScheme = colorScheme
            this.bgColor = themeParams.bgColor
            this.textColor = themeParams.textColor
            this.hintColor = themeParams.hintColor
            this.linkColor = themeParams.linkColor
            this.buttonColor = themeParams.buttonColor
            this.buttonTextColor = themeParams.buttonTextColor
            this.secondaryBgColor = themeParams.secondaryBgColor
            this.headerBgColor = themeParams.headerBgColor
            this.bottomBarBgColor = themeParams.bottomBarBgColor
            this.accentTextColor = themeParams.accentTextColor
            this.sectionBgColor = themeParams.sectionBgColor
            this.sectionHeaderTextColor = themeParams.sectionHeaderTextColor
            this.sectionSeparatorColor = themeParams.sectionSeparatorColor
            this.subtitleTextColor = themeParams.subtitleTextColor
            this.destructiveTextColor = themeParams.destructiveTextColor
            this.headerColor = headerColor
            this.backgroundColor = backgroundColor
            this.bottomBarColor = bottomBarColor
            this.setHeaderColor = setHeaderColor
            this.setBackgroundColor = setBackgroundColor
            this.setBottomBarColor = setBottomBarColor
        }
    }
}

external interface ThemeParamsHookType {

    /**
     * The color scheme currently used in the Telegram app. Also available as the CSS variable [CssVar.tgColorScheme].
     * */
    var colorScheme: ColorScheme

    /** Background color in the `#RRGGBB` format. Also available as the CSS variable [CssVar.tgThemeBgColor]. */
    var bgColor: Color?

    /** Main text color in the `#RRGGBB` format. Also available as the CSS variable [CssVar.tgThemeTextColor]. */
    var textColor: Color?

    /** Hint text color in the `#RRGGBB` format. Also available as the CSS variable [CssVar.tgThemeHintColor]. */
    var hintColor: Color?

    /** Link text color in the `#RRGGBB` format. Also available as the CSS variable [CssVar.tgThemeLinkColor]. */
    var linkColor: Color?

    /** Button color in the `#RRGGBB` format. Also available as the CSS variable [CssVar.tgThemeButtonColor]. */
    var buttonColor: Color?

    /**
     * Button text color in the `#RRGGBB` format. Also available as the CSS variable
     * [CssVar.tgThemeButtonTextColor].
     * */
    var buttonTextColor: Color?

    /**
     * Secondary background color in the `#RRGGBB` format. Also available as the CSS variable
     * [CssVar.tgThemeSecondaryBgColor].
     *
     * @since Bot API 6.1
     * */
    var secondaryBgColor: Color?

    /**
     * Header background color in the `#RRGGBB` format. Also available as the CSS variable
     * [CssVar.tgThemeHeaderBgColor].
     *
     * @since Bot API 7.0
     * */
    var headerBgColor: Color?

    /**
     * Bottom background color in the `#RRGGBB` format. Also available as the CSS variable
     * [CssVar.tgThemeBottomBarBgColor].
     *
     * @since Bot API 7.10
     * */
    var bottomBarBgColor: Color?

    /**
     * Accent text color in the `#RRGGBB` format. Also available as the CSS variable
     * [CssVar.tgThemeAccentTextColor].
     *
     * @since Bot API 7.0
     * */
    var accentTextColor: Color?

    /**
     * Background color for the section in the `#RRGGBB` format. It is recommended to use this in conjunction with
     * [secondaryBgColor]. Also available as the CSS variable [CssVar.tgThemeSectionBgColor].
     *
     * @since Bot API 7.0
     * */
    var sectionBgColor: Color?

    /**
     * Header text color for the section in the `#RRGGBB` format. Also available as the CSS variable
     * [CssVar.tgThemeSectionHeaderTextColor].
     *
     * @since Bot API 7.0
     * */
    var sectionHeaderTextColor: Color?

    /**
     * Section separator color in the `#RRGGBB` format. Also available as the CSS variable
     * [CssVar.tgThemeSectionSeparatorColor].
     *
     * @since Bot API 7.6
     * */
    var sectionSeparatorColor: Color?

    /**
     * Subtitle text color in the `#RRGGBB` format. Also available as the CSS variable
     * [CssVar.tgThemeSubtitleTextColor].
     *
     * @since Bot API 7.0
     * */
    var subtitleTextColor: Color?

    /**
     * Text color for destructive actions in the `#RRGGBB` format. Also available as the CSS variable
     * [CssVar.tgThemeDestructiveTextColor].
     *
     * @since Bot API 7.0
     * */
    var destructiveTextColor: Color?

    /** Current header color in the `#RRGGBB` format. */
    var headerColor: Color

    /** Current background color in the `#RRGGBB` format. */
    var backgroundColor: Color

    /** Current bottom bar color in the `#RRGGBB` format. */
    var bottomBarColor: Color

    /**
     * A method that sets the app header color in the `#RRGGBB` format. You can also use keywords *bg_color* and
     * *secondary_bg_color*.
     *
     * Up to `Bot API 6.9` You can only pass [bgColor] or [secondaryBgColor] as a color or *bg_color*,
     * *secondary_bg_color* keywords.
     *
     * @since Bot API 6.1
     * */
    var setHeaderColor: (color: Color) -> Unit

    /**
     * A method that sets the app background color in the `#RRGGBB` format. You can also use keywords *bg_color* and
     * *secondary_bg_color*.
     *
     * @since Bot API 6.1
     * */
    var setBackgroundColor: (color: Color) -> Unit

    /**
     * A method that sets the app's bottom bar color in the `#RRGGBB` format. You can also use the keywords *bg_color*,
     * *secondary_bg_color* and *bottom_bar_bg_color*. This color is also applied to the navigation bar on Android.
     *
     * @since Bot API 7.10
     * */
    var setBottomBarColor: (color: Color) -> Unit
}