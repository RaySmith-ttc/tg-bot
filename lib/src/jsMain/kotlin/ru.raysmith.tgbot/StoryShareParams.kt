package ru.raysmith.tgbot

/**
 * This object describes additional sharing settings for the native story editor.
 *
 * @property text The caption to be added to the media, 0-200 characters for regular users
 *  and 0-2048 characters for premium subscribers.
 * @property widgetLink An object that describes a widget link to be included in the story. Note that only
 *  premium subscribers can post stories with links.
 * */
external interface StoryShareParams {
    var text: String?

    @JsName("widget_link")
    var widgetLink: StoryWidgetLink?
}

/**
 * This object describes a widget link to be included in the story.
 *
 * @property url The URL to be included in the story.
 * @property name The name to be displayed for the widget link, 0-48 characters.
 * */
external interface StoryWidgetLink {
    var url: String
    var name: String?
}