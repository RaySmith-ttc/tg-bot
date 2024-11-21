package ru.raysmith.tgbot.hooks

import js.objects.jso
import react.useEffectOnce
import react.useMemo
import react.useState
import ru.raysmith.tgbot.*
import web.cssom.Color

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

    return useMemo(
        colorScheme,
        themeParams,
        headerColor,
        backgroundColor,
        bottomBarColor
    ) {
        jso {
            this.colorScheme = colorScheme
            this.bgColor = themeParams.bgColor.unsafeCast<Color>()
            this.textColor = themeParams.textColor.unsafeCast<Color>()
            this.hintColor = themeParams.hintColor.unsafeCast<Color>()
            this.linkColor = themeParams.linkColor.unsafeCast<Color>()
            this.buttonColor = themeParams.buttonColor.unsafeCast<Color>()
            this.buttonTextColor = themeParams.buttonTextColor.unsafeCast<Color>()
            this.secondaryBgColor = themeParams.secondaryBgColor.unsafeCast<Color>()
            this.headerBgColor = themeParams.headerBgColor.unsafeCast<Color>()
            this.bottomBarBgColor = themeParams.bottomBarBgColor.unsafeCast<Color>()
            this.accentTextColor = themeParams.accentTextColor.unsafeCast<Color>()
            this.sectionBgColor = themeParams.sectionBgColor.unsafeCast<Color>()
            this.sectionHeaderTextColor = themeParams.sectionHeaderTextColor.unsafeCast<Color>()
            this.sectionSeparatorColor = themeParams.sectionSeparatorColor.unsafeCast<Color>()
            this.subtitleTextColor = themeParams.subtitleTextColor.unsafeCast<Color>()
            this.destructiveTextColor = themeParams.destructiveTextColor.unsafeCast<Color>()
            this.headerColor = headerColor.unsafeCast<Color>()
            this.backgroundColor = backgroundColor.unsafeCast<Color>()
            this.bottomBarColor = bottomBarColor.unsafeCast<Color>()
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
}