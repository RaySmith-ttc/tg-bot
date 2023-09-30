import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.raysmith.tgbot.model.network.message.MessageEntityType
import ru.raysmith.tgbot.utils.buildHTMLString
import ru.raysmith.tgbot.utils.buildMarkdownString
import ru.raysmith.tgbot.utils.buildMarkdownV2String

class UnitTests {
    
    @Test
    fun formatHtmlString() {
        val html = buildHTMLString {
            bold("<&>")
        }

        assertThat(html).isEqualTo("""
            <b>&lt;&amp;&gt;</b>
        """.trimIndent())
    }

    @Test
    @Suppress("DEPRECATION")
    fun formatMarkdownString() {
        val markdown = buildMarkdownString {
            bold("2*2=4\n")
            italic("snake_case").text("\n")
            code("code").text("\n")
            pre("code", "unk").text("\n")
            text("te[xt")
        }

        assertThat(markdown).isEqualTo("""
            *2*\**2=4
            *_snake_\__case_
            `code`
            ```unk
            code
            ```
            te\[xt
        """.trimIndent())
    }

    @Test
    fun formatMarkdownV2String() {
        val markdown = buildMarkdownV2String {
            text("2+2=4\n")
            bold("2*2=4").text("\n")
            strikethrough("2+2=4").text("\n")
            italic("snake_case").text("\n")
            text("c: ").code("code`\\").text("\n")
            text("c: ").pre("code`\\", "unk").text("\n")
            bold("bo").mix("ld spo", MessageEntityType.BOLD, MessageEntityType.SPOILER).bold("iler").text("\n")
            bold("bo").mix("ld spo", MessageEntityType.SPOILER, MessageEntityType.BOLD).bold("iler").text("\n")
            text("te[xt").text("\n")
            spoiler("|||||").text("\n")
            text("_*[]???()~`>#+-=|{}.!\n")
//            italic("ital").mix("ik_under", MessageEntityType.ITALIC, MessageEntityType.UNDERLINE).underline("line").text("\n")
            mix("italic_underline", MessageEntityType.ITALIC, MessageEntityType.UNDERLINE)
        }

        val expected = """
            |2\+2\=4
            |*2\*2\=4*
            |~2\+2\=4~
            |_snake\_case_
            |c: `code\`\\`
            |c: ```unk
            |code\`\\
            |```
            |*bo**||ld spo||**iler*
            |*bo*||*ld spo*||*iler*
            |te\[xt
            |||\|\|\|\|\|||
            |\_\*\[\]???\(\)\~\`\>\#\+\-\=\|\{\}\.\!
            |___italic\_underline_${"\r"}__
        """.trimMargin()

        assertEquals(expected, markdown)
    }
}