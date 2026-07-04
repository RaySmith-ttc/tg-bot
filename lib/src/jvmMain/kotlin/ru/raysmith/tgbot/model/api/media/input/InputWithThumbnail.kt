package ru.raysmith.tgbot.model.api.media.input

internal interface InputWithThumbnail { // TODO move to model/bot; delete?
    /**
     * Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side.
     * The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height
     * should not exceed 320. Ignored if the file is not uploaded using multipart/form-data.
     * Thumbnails can't be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>”
     * if the thumbnail was uploaded using multipart/form-data under <file_attach_name>.
     * [More info on Sending Files »](https://core.telegram.org/bots/api#sending-files)
     * */
    val thumbnail: InputFile?
}
