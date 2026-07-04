package ru.raysmith.tgbot.model.bot.message

import ru.raysmith.tgbot.model.api.message.rich.InputRichMessage

class InputRichMessageBuilder {

    enum class Mode {
        MARKDOWN, HTML
    }

    var mode = Mode.MARKDOWN
    var isRtl: Boolean? = null
    var skipEntityDetection: Boolean? = null

    private fun markdown(): String? {
        if (mode != Mode.MARKDOWN) return null

        return ""
    }

    private fun html(): String? {
        if (mode != Mode.HTML) return null

        return ""
    }

    fun build(): InputRichMessage {
        return InputRichMessage(
            html = html(),
            markdown = markdown(),
            isRtl = isRtl,
            skipEntityDetection = skipEntityDetection,
        )
    }
}
