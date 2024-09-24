package ru.raysmith.tgbot

/** This object describes additional sharing settings for the native story editor. */
external interface StoryShareParams {

    /**
     * The caption to be added to the media, 0-200 characters for regular users
     * and 0-2048 characters for premium subscribers.
     * */
    val text: String?

    /**
     * An object that describes a widget link to be included in the story. Note that only
     * [premium](https://telegram.org/faq_premium#telegram-premium) subscribers can post stories with links.
     * */
    @JsName("widget_link")
    val widgetLink: StoryWidgetLink?
}

/** This object describes a widget link to be included in the story. */
external interface StoryWidgetLink {

    /** The URL to be included in the story. */
    val url: String

    /** The name to be displayed for the widget link, 0-48 characters. */
    val name: String?
}