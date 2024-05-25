import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.constructors
import com.lemonappdev.konsist.api.ext.list.functions
import com.lemonappdev.konsist.api.ext.list.parameters
import com.lemonappdev.konsist.api.ext.list.properties
import com.lemonappdev.konsist.api.verify.assertTrue
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.PrimitiveKind
import org.junit.jupiter.api.Test
import ru.raysmith.tgbot.network.API
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.utils.letIf
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.functions

class KonsistTests {

    @Test
    fun `all nullable fields should have a default value in data classes`() {
        Konsist.scopeFromProject()
            .classes()
            .filter { it.hasDataModifier && it.hasAnnotation { a -> a.representsType(Serializable::class.qualifiedName!!) } }
            .constructors
            .parameters.filter { it.type.isNullable }
            .assertTrue { it.hasDefaultValue() }
    }

    @Test
    fun `all Serializable classes and it's fields should have KDoc`() {
        val classes = Konsist.scopeFromProject()
            .classes()
            .filter { it.hasAnnotation { a -> a.representsType(Serializable::class.qualifiedName!!) } }
        val properties = classes.properties()

        classes.assertTrue { it.hasKDoc }
        properties.assertTrue { it.hasKDoc }
    }

    @Test
    fun `all public API methods should have KDoc`() {
        Konsist.scopeFromProject()
            .interfaces()
            .filter { it.name == API::class.simpleName }
            .functions()
            .filter { it.hasPublicOrDefaultModifier }
            .assertTrue { it.hasKDoc }
    }

    @Test
    fun `all sealed serializable classes should be registered in json serializersModule`() {
        Konsist.scopeFromProject()
            .classes()
            .filter { it.hasSealedModifier }
            .filter { it.hasAnnotation { a -> a.representsType(Serializable::class.qualifiedName!!) } }
            .filter {
                val clazz = Class.forName(it.fullyQualifiedName).kotlin
                val serializer = clazz
                    .companionObject!!.functions.first { f -> f.name == "serializer" }
                    .call(clazz.companionObject!!.objectInstance) as KSerializer<*>

                serializer.descriptor.kind !is PrimitiveKind
            }
            .forEach { sealed ->
                val children = Konsist.scopeFromProject()
                    .classes()
                    .filter { it.hasParent { p ->
                        p.name == sealed.name && p.packagee?.fullyQualifiedName == sealed.packagee!!.fullyQualifiedName
                    } }

                children.forEach { childDeclaration ->
                    val classname = sealed.fullyQualifiedName
                    val child = Class.forName(childDeclaration.fullyQualifiedName.letIf(!childDeclaration.isTopLevel) {
                        it.replaceRange(it.lastIndexOf(".").let { it..it }, "$")
                    }).kotlin
                    val childSerializer = child.companionObject!!.functions.first { f -> f.name == "serializer" }.call(child.companionObject!!.objectInstance) as KSerializer<*>
                    if (childSerializer.descriptor.kind !is PrimitiveKind && childSerializer.descriptor.kind !is PolymorphicKind) {
                        val childSerialName = childSerializer.descriptor.serialName
                        val serializer = TelegramApi.json.serializersModule.getPolymorphic(Class.forName(classname).kotlin, childSerialName)
                        assert(serializer != null) { "Sealed: ${sealed}, child: ${childDeclaration.name}" }
                    }
                }
            }
    }
}