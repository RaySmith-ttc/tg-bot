package ru.raysmith.tgbot

/**
 * This object represents the content-defined safe area insets, providing padding values to ensure content remains
 * within visible boundaries, avoiding overlap with Telegram UI elements.
 *
 * <img src="https://core.telegram.org/file/400780400676/2/8VT7jCQvpsk.1386608/d249aa072662450345" />
 * */
external interface ContentSafeAreaInset {

    /**
     *  The top inset in pixels, representing the space to avoid at the top of the content area.
     *  Also available as the CSS variable [CssVar.tgContentSafeAreaInsetTop].
     *  */
    val top: Int

    /**
     * The bottom inset in pixels, representing the space to avoid at the bottom of the content area.
     * Also available as the CSS variable [CssVar.tgContentSafeAreaInsetBottom].
     * */
    val bottom: Int

    /**
     * The left inset in pixels, representing the space to avoid on the left side of the content area.
     * Also available as the CSS variable [CssVar.tgContentSafeAreaInsetLeft].
     * */
    val left: Int

    /**
     * The right inset in pixels, representing the space to avoid on the right side of the content area.
     * Also available as the CSS variable [CssVar.tgContentSafeAreaInsetRight].
     * */
    val right: Int
}