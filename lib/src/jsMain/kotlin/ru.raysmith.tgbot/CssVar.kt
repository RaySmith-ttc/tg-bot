package ru.raysmith.tgbot

import web.cssom.CustomPropertyName
import web.cssom.`var`

object CssVar {
    /** --tg-viewport-height */
    fun <T : Any> tgViewportHeight() = `var`<T>(CustomPropertyName("--tg-viewport-height"))

    /** --tg-viewport-stable-height */
    fun <T : Any> tgViewportStableHeight() = `var`<T>(CustomPropertyName("--tg-viewport-stable-height"))

    /** --tg-color-scheme */
    fun <T : Any> tgColorScheme() = `var`<T>(CustomPropertyName("--tg-color-scheme"))

    /** --tg-theme-bg-color */
    fun <T : Any> tgThemeBgColor() = `var`<T>(CustomPropertyName("--tg-theme-bg-color"))

    /** --tg-theme-text-color */
    fun <T : Any> tgThemeTextColor() = `var`<T>(CustomPropertyName("--tg-theme-text-color"))

    /** --tg-theme-hint-color */
    fun <T : Any> tgThemeHintColor() = `var`<T>(CustomPropertyName("--tg-theme-hint-color"))

    /** --tg-theme-link-color */
    fun <T : Any> tgThemeLinkColor() = `var`<T>(CustomPropertyName("--tg-theme-link-color"))

    /** --tg-theme-button-color */
    fun <T : Any> tgThemeButtonColor() = `var`<T>(CustomPropertyName("--tg-theme-button-color"))

    /** --tg-theme-button-text-color */
    fun <T : Any> tgThemeButtonTextColor() = `var`<T>(CustomPropertyName("--tg-theme-button-text-color"))

    /** --tg-theme-secondary-bg-color */
    fun <T : Any> tgThemeSecondaryBgColor() = `var`<T>(CustomPropertyName("--tg-theme-secondary-bg-color"))

    /** --tg-theme-header-bg-color */
    fun <T : Any> tgThemeHeaderBgColor() = `var`<T>(CustomPropertyName("--tg-theme-header-bg-color"))

    /** --tg-theme-bottom-bar-bg-color */
    fun <T : Any> tgThemeBottomBarBgColor() = `var`<T>(CustomPropertyName("--tg-theme-bottom-bar-bg-color"))

    /** --tg-theme-accent-text-color */
    fun <T : Any> tgThemeAccentTextColor() = `var`<T>(CustomPropertyName("--tg-theme-accent-text-color"))

    /** --tg-theme-section-bg-color */
    fun <T : Any> tgThemeSectionBgColor() = `var`<T>(CustomPropertyName("--tg-theme-section-bg-color"))

    /** --tg-theme-section-header-text-color */
    fun <T : Any> tgThemeSectionHeaderTextColor() = `var`<T>(CustomPropertyName("--tg-theme-section-header-text-color"))

    /** --tg-theme-section-separator-color */
    fun <T : Any> tgThemeSectionSeparatorColor() = `var`<T>(CustomPropertyName("--tg-theme-section-separator-color"))

    /** --tg-theme-subtitle-text-color */
    fun <T : Any> tgThemeSubtitleTextColor() = `var`<T>(CustomPropertyName("--tg-theme-subtitle-text-color"))

    /** --tg-theme-destructive-text-color */
    fun <T : Any> tgThemeDestructiveTextColor() = `var`<T>(CustomPropertyName("--tg-theme-destructive-text-color"))
}