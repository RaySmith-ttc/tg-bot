package ru.raysmith.tgbot

import web.cssom.Color

/**
 * Mini Apps can [adjust the appearance](https://core.telegram.org/bots/webapps#color-schemes) of the interface to match
 * the Telegram user's app in real time. This object contains the user's current theme settings
 *
 * <img src="https://core.telegram.org/file/400780400851/2/6GwDkk6T-aY.416569/b591d589108b487d63" />
 * */
external interface ThemeParams {

    /** Background color in the `#RRGGBB` format. Also available as the CSS variable [CssVar.tgThemeBgColor]. */
    @JsName("bg_color")
    val bgColor: Color?

    /** Main text color in the `#RRGGBB` format. Also available as the CSS variable [CssVar.tgThemeTextColor]. */
    @JsName("text_color")
    val textColor: Color?

    /** Hint text color in the `#RRGGBB` format. Also available as the CSS variable [CssVar.tgThemeHintColor]. */
    @JsName("hint_color")
    val hintColor: Color?

    /** Link text color in the `#RRGGBB` format. Also available as the CSS variable [CssVar.tgThemeLinkColor]. */
    @JsName("link_color")
    val linkColor: Color?

    /** Button color in the `#RRGGBB` format. Also available as the CSS variable [CssVar.tgThemeButtonColor]. */
    @JsName("button_color")
    val buttonColor: Color?

    /**
     * Button text color in the `#RRGGBB` format. Also available as the CSS variable
     * [CssVar.tgThemeButtonTextColor].
     * */
    @JsName("button_text_color")
    val buttonTextColor: Color?

    /**
     * Secondary background color in the `#RRGGBB` format. Also available as the CSS variable
     * [CssVar.tgThemeSecondaryBgColor].
     *
     * @since Bot API 6.1
     * */
    @JsName("secondary_bg_color")
    val secondaryBgColor: Color?

    /**
     * Header background color in the `#RRGGBB` format. Also available as the CSS variable
     * [CssVar.tgThemeHeaderBgColor].
     *
     * @since Bot API 7.0
     * */
    @JsName("header_bg_color")
    val headerBgColor: Color?

    /**
     * Bottom background color in the `#RRGGBB` format. Also available as the CSS variable
     * [CssVar.tgThemeBottomBarBgColor].
     *
     * @since Bot API 7.10
     * */
    @JsName("bottom_bar_bg_color")
    val bottomBarBgColor: Color?

    /**
     * Accent text color in the `#RRGGBB` format. Also available as the CSS variable
     * [CssVar.tgThemeAccentTextColor].
     *
     * @since Bot API 7.0
     * */
    @JsName("accent_text_color")
    val accentTextColor: Color?

    /**
     * Background color for the section in the `#RRGGBB` format. It is recommended to use this in conjunction with
     * [secondaryBgColor]. Also available as the CSS variable [CssVar.tgThemeSectionBgColor].
     *
     * @since Bot API 7.0
     * */
    @JsName("section_bg_color")
    val sectionBgColor: Color?

    /**
     * Header text color for the section in the `#RRGGBB` format. Also available as the CSS variable
     * [CssVar.tgThemeSectionHeaderTextColor].
     *
     * @since Bot API 7.0
     * */
    @JsName("section_header_text_color")
    val sectionHeaderTextColor: Color?

    /**
     * Section separator color in the `#RRGGBB` format. Also available as the CSS variable
     * [CssVar.tgThemeSectionSeparatorColor].
     *
     * @since Bot API 7.6
     * */
    @JsName("section_separator_color")
    val sectionSeparatorColor: Color?

    /**
     * Subtitle text color in the `#RRGGBB` format. Also available as the CSS variable
     * [CssVar.tgThemeSubtitleTextColor].
     *
     * @since Bot API 7.0
     * */
    @JsName("subtitle_text_color")
    val subtitleTextColor: Color?

    /**
     * Text color for destructive actions in the `#RRGGBB` format. Also available as the CSS variable
     * [CssVar.tgThemeDestructiveTextColor].
     *
     * @since Bot API 7.0
     * */
    @JsName("destructive_text_color")
    val destructiveTextColor: Color?
}