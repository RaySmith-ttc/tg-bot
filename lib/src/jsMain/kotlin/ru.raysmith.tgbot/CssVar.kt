package ru.raysmith.tgbot

import web.cssom.CustomPropertyName
import web.cssom.`var`

object CssVar {

    /** --tg-viewport-height */
    fun <T : Any> tgViewportHeight() = `var`<T>(CustomPropertyName("--tg-viewport-height"))

    /** --tg-viewport-height */
    fun <T : Any> tgViewportHeight(fallback: T) = `var`(CustomPropertyName("--tg-viewport-width"), fallback)

    /** --tg-viewport-stable-height */
    fun <T : Any> tgViewportStableHeight() = `var`<T>(CustomPropertyName("--tg-viewport-stable-height"))

    /** --tg-viewport-stable-height */
    fun <T : Any> tgViewportStableHeight(fallback: T) = `var`(CustomPropertyName("--tg-viewport-stable-height"), fallback)

    /** --tg-color-scheme */
    fun <T : Any> tgColorScheme() = `var`<T>(CustomPropertyName("--tg-color-scheme"))

    /** --tg-color-scheme */
    fun <T : Any> tgColorScheme(fallback: T) = `var`(CustomPropertyName("--tg-color-scheme"), fallback)

    /** --tg-theme-bg-color */
    fun <T : Any> tgThemeBgColor() = `var`<T>(CustomPropertyName("--tg-theme-bg-color"))

    /** --tg-theme-bg-color */
    fun <T : Any> tgThemeBgColor(fallback: T) = `var`(CustomPropertyName("--tg-theme-bg-color"), fallback)

    /** --tg-theme-text-color */
    fun <T : Any> tgThemeTextColor() = `var`<T>(CustomPropertyName("--tg-theme-text-color"))

    /** --tg-theme-text-color */
    fun <T : Any> tgThemeTextColor(fallback: T) = `var`(CustomPropertyName("--tg-theme-text-color"), fallback)

    /** --tg-theme-hint-color */
    fun <T : Any> tgThemeHintColor() = `var`<T>(CustomPropertyName("--tg-theme-hint-color"))

    /** --tg-theme-hint-color */
    fun <T : Any> tgThemeHintColor(fallback: T) = `var`(CustomPropertyName("--tg-theme-hint-color"), fallback)

    /** --tg-theme-link-color */
    fun <T : Any> tgThemeLinkColor() = `var`<T>(CustomPropertyName("--tg-theme-link-color"))

    /** --tg-theme-link-color */
    fun <T : Any> tgThemeLinkColor(fallback: T) = `var`(CustomPropertyName("--tg-theme-link-color"), fallback)

    /** --tg-theme-button-color */
    fun <T : Any> tgThemeButtonColor() = `var`<T>(CustomPropertyName("--tg-theme-button-color"))

    /** --tg-theme-button-color */
    fun <T : Any> tgThemeButtonColor(fallback: T) = `var`(CustomPropertyName("--tg-theme-button-color"), fallback)

    /** --tg-theme-button-text-color */
    fun <T : Any> tgThemeButtonTextColor() = `var`<T>(CustomPropertyName("--tg-theme-button-text-color"))

    /** --tg-theme-button-text-color */
    fun <T : Any> tgThemeButtonTextColor(fallback: T) = `var`(CustomPropertyName("--tg-theme-button-text-color"), fallback)

    /** --tg-theme-secondary-bg-color */
    fun <T : Any> tgThemeSecondaryBgColor() = `var`<T>(CustomPropertyName("--tg-theme-secondary-bg-color"))

    /** --tg-theme-secondary-bg-color */
    fun <T : Any> tgThemeSecondaryBgColor(fallback: T) = `var`(CustomPropertyName("--tg-theme-secondary-bg-color"), fallback)

    /** --tg-theme-header-bg-color */
    fun <T : Any> tgThemeHeaderBgColor() = `var`<T>(CustomPropertyName("--tg-theme-header-bg-color"))

    /** --tg-theme-header-bg-color */
    fun <T : Any> tgThemeHeaderBgColor(fallback: T) = `var`(CustomPropertyName("--tg-theme-header-bg-color"), fallback)

    /** --tg-theme-bottom-bar-bg-color */
    fun <T : Any> tgThemeBottomBarBgColor() = `var`<T>(CustomPropertyName("--tg-theme-bottom-bar-bg-color"))

    /** --tg-theme-bottom-bar-bg-color */
    fun <T : Any> tgThemeBottomBarBgColor(fallback: T) = `var`(CustomPropertyName("--tg-theme-bottom-bar-bg-color"), fallback)

    /** --tg-theme-accent-text-color */
    fun <T : Any> tgThemeAccentTextColor() = `var`<T>(CustomPropertyName("--tg-theme-accent-text-color"))

    /** --tg-theme-accent-text-color */
    fun <T : Any> tgThemeAccentTextColor(fallback: T) = `var`(CustomPropertyName("--tg-theme-accent-text-color"), fallback)

    /** --tg-theme-section-bg-color */
    fun <T : Any> tgThemeSectionBgColor() = `var`<T>(CustomPropertyName("--tg-theme-section-bg-color"))

    /** --tg-theme-section-bg-color */
    fun <T : Any> tgThemeSectionBgColor(fallback: T) = `var`(CustomPropertyName("--tg-theme-section-bg-color"), fallback)

    /** --tg-theme-section-header-text-color */
    fun <T : Any> tgThemeSectionHeaderTextColor() = `var`<T>(CustomPropertyName("--tg-theme-section-header-text-color"))

    /** --tg-theme-section-header-text-color */
    fun <T : Any> tgThemeSectionHeaderTextColor(fallback: T) = `var`(CustomPropertyName("--tg-theme-section-header-text-color"), fallback)

    /** --tg-theme-section-separator-color */
    fun <T : Any> tgThemeSectionSeparatorColor() = `var`<T>(CustomPropertyName("--tg-theme-section-separator-color"))

    /** --tg-theme-section-separator-color */
    fun <T : Any> tgThemeSectionSeparatorColor(fallback: T) = `var`(CustomPropertyName("--tg-theme-section-separator-color"), fallback)

    /** --tg-theme-subtitle-text-color */
    fun <T : Any> tgThemeSubtitleTextColor() = `var`<T>(CustomPropertyName("--tg-theme-subtitle-text-color"))

    /** --tg-theme-subtitle-text-color */
    fun <T : Any> tgThemeSubtitleTextColor(fallback: T) = `var`(CustomPropertyName("--tg-theme-subtitle-text-color"), fallback)

    /** --tg-theme-destructive-text-color */
    fun <T : Any> tgThemeDestructiveTextColor() = `var`<T>(CustomPropertyName("--tg-theme-destructive-text-color"))

    /** --tg-theme-destructive-text-color */
    fun <T : Any> tgThemeDestructiveTextColor(fallback: T) = `var`(CustomPropertyName("--tg-theme-destructive-text-color"), fallback)

    /** --tg-safe-area-inset-top */
    fun <T : Any> tgSafeAreaInsetTop() = `var`<T>(CustomPropertyName("--tg-safe-area-inset-top"))

    /** --tg-safe-area-inset-top */
    fun <T : Any> tgSafeAreaInsetTop(fallback: T) = `var`(CustomPropertyName("--tg-safe-area-inset-top"), fallback)

    /** --tg-safe-area-inset-bottom */
    fun <T : Any> tgSafeAreaInsetBottom() = `var`<T>(CustomPropertyName("--tg-safe-area-inset-bottom"))

    /** --tg-safe-area-inset-bottom */
    fun <T : Any> tgSafeAreaInsetBottom(fallback: T) = `var`(CustomPropertyName("--tg-safe-area-inset-bottom"), fallback)

    /** --tg-safe-area-inset-left */
    fun <T : Any> tgSafeAreaInsetLeft() = `var`<T>(CustomPropertyName("--tg-safe-area-inset-left"))

    /** --tg-safe-area-inset-left */
    fun <T : Any> tgSafeAreaInsetLeft(fallback: T) = `var`(CustomPropertyName("--tg-safe-area-inset-left"), fallback)

    /** --tg-safe-area-inset-right */
    fun <T : Any> tgSafeAreaInsetRight() = `var`<T>(CustomPropertyName("--tg-safe-area-inset-right"))

    /** --tg-safe-area-inset-right */
    fun <T : Any> tgSafeAreaInsetRight(fallback: T) = `var`(CustomPropertyName("--tg-safe-area-inset-right"), fallback)

    /** --tg-content-safe-area-inset-top */
    fun <T : Any> tgContentSafeAreaInsetTop() = `var`(CustomPropertyName("--tg-content-safe-area-inset-top"))

    /** --tg-content-safe-area-inset-top */
    fun <T : Any> tgContentSafeAreaInsetTop(fallback: T) = `var`(CustomPropertyName("--tg-content-safe-area-inset-top"), fallback)

    /** --tg-content-safe-area-inset-bottom */
    fun <T : Any> tgContentSafeAreaInsetBottom() = `var`<T>(CustomPropertyName("--tg-content-safe-area-inset-bottom"))

    /** --tg-content-safe-area-inset-bottom */
    fun <T : Any> tgContentSafeAreaInsetBottom(fallback: T) = `var`(CustomPropertyName("--tg-content-safe-area-inset-bottom"), fallback)

    /** --tg-content-safe-area-inset-left */
    fun <T : Any> tgContentSafeAreaInsetLeft() = `var`<T>(CustomPropertyName("--tg-content-safe-area-inset-left"))

    /** --tg-content-safe-area-inset-left */
    fun <T : Any> tgContentSafeAreaInsetLeft(fallback: T) = `var`(CustomPropertyName("--tg-content-safe-area-inset-left"), fallback)

    /** --tg-content-safe-area-inset-right */
    fun <T : Any> tgContentSafeAreaInsetRight() = `var`<T>(CustomPropertyName("--tg-content-safe-area-inset-right"))

    /** --tg-content-safe-area-inset-right */
    fun <T : Any> tgContentSafeAreaInsetRight(fallback: T) = `var`(CustomPropertyName("--tg-content-safe-area-inset-right"), fallback)
}