import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.constructors
import com.lemonappdev.konsist.api.ext.list.parameters
import com.lemonappdev.konsist.api.ext.list.properties
import com.lemonappdev.konsist.api.verify.assert
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Test
import retrofit2.http.Multipart
import retrofit2.http.Part
import retrofit2.http.Query
import ru.raysmith.tgbot.core.BotContext
import ru.raysmith.tgbot.network.TelegramService
import ru.raysmith.tgbot.network.TelegramService2

class KonsistTests {

    @Test
    fun `all nullable fields should have a default value in data classes`() {
        Konsist.scopeFromProject()
            .classes()
            .filter { it.hasDataModifier && it.hasAnnotation { a -> a.representsType(Serializable::class.qualifiedName!!) } }
            .constructors
            .parameters.filter { it.type.isNullable }
            .assert { it.hasDefaultValue() }
    }

    @Test
    fun `all Serializable classes and it's fields should have KDoc`() {
        val classes = Konsist.scopeFromProject()
            .classes()
            .filter { it.hasAnnotation { a -> a.representsType(Serializable::class.qualifiedName!!) } }
        val properties = classes.properties()

        classes.assert { it.hasKDoc }
        properties.assert { it.hasKDoc }
    }

    @Test
    fun parser() {
        val scope = Konsist.scopeFromProject()
        val serviceFunctions = scope.interfaces()
            .first { it.name == TelegramService::class.simpleName }
            .functions()

        val service2Functions = scope.interfaces()
            .first { it.name == TelegramService2::class.simpleName }
            .functions()
            .map { it.name }

        val botContextFunctions = scope.interfaces()
            .first { it.name == BotContext::class.simpleName }
            .functions()
            .map { it.name }

        serviceFunctions
            .filter { it.name !in service2Functions }
            .filter { it.name !in listOf("createInvoiceLink", "sendInvoice", "createNewStickerSet") }
            .forEach {

//        scope.interfaces()
//            .first { it.name == BotContext::class.simpleName }
//            .functions()
//            .filter { it.name !in listOf("withBot") }
//            .forEach {
            val docs = it.kDoc?.text?.let { text ->
                buildString {
                    append("/**\n* ")
                    append(text.split("\n").joinToString("\n* "))
                    append("\n* */")
                }
            }

            val funDef = buildString {
                append("suspend fun ").append(it.name).append("(\n")
                it.parameters.forEach { param ->
                    append("\t").append(param.name).append(": ").append(param.type.text)
                        .append(param.defaultValue?.let { " = $it" } ?: "").append(",\n")
                }

                val serviceFunction = serviceFunctions.first { sf ->
                    when (it.name) {
                        "downloadFile" -> "getFile"
                        else -> it.name
                    } == sf.name
                }
                val returnType = (it.returnType ?: serviceFunction.returnType!!).text
                    .replace("NetworkResponse<", "")
                    .let { if (it.contains("Call<")) it.drop(5).dropLast(2) else it }
                    .replace("MessageResponse", "Message>")


                append("\n)").append(" = request<").append(returnType).append(">").append(" {\n")
                    .append("\tclient.post(\"")
                    .append(serviceFunction.annotations.first { it.name == "POST" }.arguments.first().value)
                    .append("\") {\n")

                serviceFunction.parameters.forEach { sfParam ->
                    sfParam.annotations.find { a -> a.name == Query::class.simpleName }?.let { query ->
                        append("\t\tparameter(\"").append(query.arguments.first().value).append("\", ")
                            .append(sfParam.name).append(")\n")
                    }
                }

                if (serviceFunction.annotations.find { a -> a.name == Multipart::class.simpleName } != null) {
                    append("\t\tsetMultiPartFormDataBody(\n")
                    serviceFunction.parameters.forEach { sfParam ->
                        sfParam.annotations.find { a -> a.name == Part::class.simpleName }?.let { part ->
                            append("\t\t\t\"").append(part.arguments.firstOrNull()?.value ?: sfParam.name)
                                .append("\" to ").append(sfParam.name).append(",\n")
                        } ?: sfParam.annotations.find { a -> a.name == Query::class.simpleName }?.let { query ->
                            // do nothing
                        } ?: error("Unknown annotation")
                    }
                    append("\t\t)\n")
                }

                append("\t}\n")
                append("}")
            }.replace(",\n\n", ",\n")

            println(buildString {
                append(docs).append("\n")
                append(funDef)
            })
            println()
        }
    }
}