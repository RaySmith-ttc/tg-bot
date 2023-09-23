import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.constructors
import com.lemonappdev.konsist.api.ext.list.parameters
import com.lemonappdev.konsist.api.ext.list.properties
import com.lemonappdev.konsist.api.verify.assert
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Test

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
}