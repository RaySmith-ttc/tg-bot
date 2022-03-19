package helper

import java.io.IOException
import java.nio.file.Files

object FileLoader {
    @Throws(IOException::class)
    fun read(filePath: String): String {
        return javaClass.classLoader.getResourceAsStream(filePath)?.use {
            String(it.readBytes())
        } ?: throw IOException("file $filePath not found")
    }
}
