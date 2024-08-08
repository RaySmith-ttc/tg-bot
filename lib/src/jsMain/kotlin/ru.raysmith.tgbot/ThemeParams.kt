package ru.raysmith.tgbot

/**
 * Mini Apps can [adjust the appearance](https://core.telegram.org/bots/webapps#color-schemes) of the interface to match
 * the Telegram user's app in real time. This object contains the user's current theme settings
 *
 * <img src="https://core.telegram.org/file/400780400851/2/6GwDkk6T-aY.416569/b591d589108b487d63" />
 * */
external interface ThemeParams {

    /** Background color in the `#RRGGBB` format. Also available as the CSS variable `var(--tg-theme-bg-color)`. */
    @JsName("bg_color")
    val bgColor: String?

    /** Main text color in the `#RRGGBB` format. Also available as the CSS variable `var(--tg-theme-text-color)`. */
    @JsName("text_color")
    val textColor: String?

    /** Hint text color in the `#RRGGBB` format. Also available as the CSS variable `var(--tg-theme-hint-color)`. */
    @JsName("hint_color")
    val hintColor: String?

    /** Link text color in the `#RRGGBB` format. Also available as the CSS variable `var(--tg-theme-link-color)`. */
    @JsName("link_color")
    val linkColor: String?

    /** Button color in the `#RRGGBB` format. Also available as the CSS variable `var(--tg-theme-button-color)`. */
    @JsName("button_color")
    val buttonColor: String?

    /**
     * Button text color in the `#RRGGBB` format. Also available as the CSS variable
     * `var(--tg-theme-button-text-color)`.
     * */
    @JsName("button_text_color")
    val buttonTextColor: String?

    /**
     * Secondary background color in the `#RRGGBB` format. Also available as the CSS variable
     * `var(--tg-theme-secondary-bg-color)`.
     *
     * @since Bot API 6.1
     * */
    @JsName("secondary_bg_color")
    val secondaryBgColor: String?

    /**
     * Header background color in the `#RRGGBB` format. Also available as the CSS variable
     * `var(--tg-theme-header-bg-color)`.
     *
     * @since Bot API 7.0
     * */
    @JsName("header_bg_color")
    val headerBgColor: String?

    /**
     * Accent text color in the `#RRGGBB` format. Also available as the CSS variable
     * `var(--tg-theme-accent-text-color)`.
     *
     * @since Bot API 7.0
     * */
    @JsName("accent_text_color")
    val accentTextColor: String?

    /**
     * Background color for the section in the `#RRGGBB` format. It is recommended to use this in conjunction with
     * [secondaryBgColor]. Also available as the CSS variable `var(--tg-theme-section-bg-color)`.
     *
     * @since Bot API 7.0
     * */
    @JsName("section_bg_color")
    val sectionBgColor: String?

    /**
     * Header text color for the section in the `#RRGGBB` format. Also available as the CSS variable
     * `var(--tg-theme-section-header-text-color)`.
     *
     * @since Bot API 7.0
     * */
    @JsName("section_header_text_color")
    val sectionHeaderTextColor: String?

    /**
     * Section separator color in the `#RRGGBB` format. Also available as the CSS variable
     * `var(--tg-theme-section-separator-color)`.
     *
     * @since Bot API 7.6
     * */
    @JsName("section_separator_color")
    val sectionSeparatorColor: String?

    /**
     * Subtitle text color in the `#RRGGBB` format. Also available as the CSS variable
     * `var(--tg-theme-subtitle-text-color)`.
     *
     * @since Bot API 7.0
     * */
    @JsName("subtitle_text_color")
    val subtitleTextColor: String?

    /**
     * Text color for destructive actions in the `#RRGGBB` format. Also available as the CSS variable
     * `var(--tg-theme-destructive-text-color)`.
     *
     * @since Bot API 7.0
     * */
    @JsName("destructive_text_color")
    val destructiveTextColor: String?
}