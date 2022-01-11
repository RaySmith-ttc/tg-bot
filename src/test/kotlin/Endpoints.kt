import kotlinx.serialization.decodeFromString
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import org.junit.jupiter.api.Test
import ru.raysmith.tgbot.model.network.Error
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.network.TelegramApiException
import java.io.File

class Endpoints {

    @Test
    fun getChat() {
        val body = TelegramApi.service.getChat("243562346").execute().body()
        println(body)
    }

    @Test
    fun service() {
        TelegramApi.serviceWithToken("1949082208:AAFiooABSxe1znPpwH9rVAaYcHHG5bi7-qo")
            .sendMessage("243562346", "test").execute()
    }

    @Test
    fun sendPhoto() {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                val response = chain.proceed(request)
                if (!response.isSuccessful && response.body != null) {
                    val error = TelegramApi.json.decodeFromString<Error>(response.body!!.string())
                    throw TelegramApiException(error, request)
                }
                response
            }
            .build()
        val file = File("C:\\Users\\Ray\\Desktop\\image1.png")
        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("chat_id", "243562346")
            .addFormDataPart("caption", "test")
            .addFormDataPart("photo", "photo.jpg", file.asRequestBody("image/jpg".toMediaType()))
            .build()

        val req = Request.Builder()
            .url("https://api.telegram.org/bot1729711415:AAEbCYOwweWehQ9OjRUiRtafSUk6T7qNzBQ/sendPhoto")
            .post(multipartBody)
            .build()

        client.newCall(req).execute()
    }

}