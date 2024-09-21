package ru.raysmith.tgbot.model.bot.message.group

import ru.raysmith.tgbot.model.bot.message.media.CaptionableMediaMessage
import ru.raysmith.tgbot.model.bot.message.media.ExtendedCaptionableMediaMessage
import ru.raysmith.tgbot.model.network.media.input.InputFile
import ru.raysmith.tgbot.model.network.media.input.NotReusableInputFile

internal class MediaRequestInternal : MediaRequest() {
    fun applyMediaExposed(media: InputFile) = super.applyMedia(media)
}

abstract class MediaRequest {

    companion object {
        val attachProtocol: String = "attach://"
        val attachProtocolLength = attachProtocol.length
    }

    internal val inputFiles = mutableListOf<InputFile>()

    private fun getFieldValue(): String {
        return "${attachProtocol}file${inputFiles.size + 1}"
    }

    protected fun applyMedia(media: InputFile) = when (media) {
        is InputFile.FileIdOrUrl -> media.value
        is InputFile.ByteArray -> getFieldValue()
        is InputFile.File -> getFieldValue()
    }.also {
        inputFiles.add(media)
    }

    protected fun applyMedia(media: NotReusableInputFile) = when (media) {
        is InputFile.ByteArray -> getFieldValue()
        is InputFile.File -> getFieldValue()
        else -> error("ReusableMedia can be only a file or byte array")
    }.also {
        inputFiles.add(media as InputFile)
    }
}

// TODO ?
abstract class MediaRequestWithCaption : CaptionableMediaMessage() {

    companion object {
        val attachProtocol: String = "attach://"
        val attachProtocolLength = attachProtocol.length
    }

    internal val inputFiles = mutableListOf<InputFile>()

    private fun getFieldValue(): String {
        return "${attachProtocol}file${inputFiles.size + 1}"
    }

    protected fun applyMedia(media: InputFile) = when (media) {
        is InputFile.FileIdOrUrl -> media.value
        is InputFile.ByteArray -> getFieldValue()
        is InputFile.File -> getFieldValue()
    }.also {
        inputFiles.add(media)
    }

    protected fun applyMedia(media: NotReusableInputFile) = when (media) {
        is InputFile.ByteArray -> getFieldValue()
        is InputFile.File -> getFieldValue()
        else -> error("ReusableMedia can be only a file or byte array")
    }.also {
        inputFiles.add(media as InputFile)
    }
}

// TODO ?
abstract class PrivateChatsAndGroupsMediaRequestWithCaption : ExtendedCaptionableMediaMessage() {

    companion object {
        val attachProtocol: String = "attach://"
        val attachProtocolLength = attachProtocol.length
    }

    internal val inputFiles = mutableListOf<InputFile>()

    private fun getFieldValue(): String {
        return "${attachProtocol}file${inputFiles.size + 1}"
    }

    protected fun applyMedia(media: InputFile) = when (media) {
        is InputFile.FileIdOrUrl -> media.value
        is InputFile.ByteArray -> getFieldValue()
        is InputFile.File -> getFieldValue()
    }.also {
        inputFiles.add(media)
    }

    protected fun applyMedia(media: NotReusableInputFile) = when (media) {
        is InputFile.ByteArray -> getFieldValue()
        is InputFile.File -> getFieldValue()
        else -> error("ReusableMedia can be only a file or byte array")
    }.also {
        inputFiles.add(media as InputFile)
    }
}