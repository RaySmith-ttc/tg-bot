package ru.raysmith.tgbot

/**
 * This object represents the system-defined safe area insets, providing padding values to ensure content remains within
 * visible boundaries, avoiding overlap with system UI elements like notches or navigation bars.
 *
 * <img src="https://core.telegram.org/file/400780400066/1/tTFDI7OC8tE.1374724/9e496dd312c7706a38" />
 * */
external interface SafeAreaInset {

    /**
     * The top inset in pixels, representing the space to avoid at the top of the screen.
     * Also available as the CSS variable [CssVar.tgSafeAreaInsetTop].
     * */
    val top: Int

    /**
     * The top inset in pixels, representing the space to avoid at the bottom of the screen.
     * Also available as the CSS variable [CssVar.tgSafeAreaInsetBottom].
     * */
    val bottom: Int

    /**
     * The top inset in pixels, representing the space to avoid at the left side of the screen.
     * Also available as the CSS variable [CssVar.tgSafeAreaInsetLeft].
     * */
    val left: Int

    /**
     * The top inset in pixels, representing the space to avoid at the right side of the screen.
     * Also available as the CSS variable [CssVar.tgSafeAreaInsetRight].
     * */
    val right: Int
}