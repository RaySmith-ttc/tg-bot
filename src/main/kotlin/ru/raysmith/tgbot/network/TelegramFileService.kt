package ru.raysmith.tgbot.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface TelegramFileService {
    @GET("{file_path}")
    fun downLoad(
        @Path("file_path") filePath: String
    ): Call<ResponseBody>
}