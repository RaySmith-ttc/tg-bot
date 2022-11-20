import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.serializer
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.raysmith.tgbot.model.bot.ChatId
import ru.raysmith.tgbot.model.network.response.LiveLocationResponse
import ru.raysmith.tgbot.model.network.User
import ru.raysmith.tgbot.model.network.message.MessageEntityType
import ru.raysmith.tgbot.model.network.menubutton.MenuButtonDefault
import ru.raysmith.tgbot.network.TelegramApi
import ru.raysmith.tgbot.utils.botContext
import ru.raysmith.tgbot.utils.buildHTMLString
import ru.raysmith.tgbot.utils.buildMarkdownString
import ru.raysmith.tgbot.utils.buildMarkdownV2String
import java.io.File
import java.nio.charset.Charset
import java.time.LocalDate
import kotlin.reflect.typeOf

class UnitTests {

    @Test
    fun allPhotos() {
        botContext {
            val r = User(ChatId.ID(243562346), false, "").getAllProfilePhotos(this)
            println(r)
        }
    }

    @Test
    fun ser() {
        println(TelegramApi.json.encodeToString(MenuButtonDefault))
    }

    @Test
    fun deserialize() {
        val booleanRes = """
            {
                "ok": true,
                "result": true
            }
        """.trimIndent()

        val messageRes = """
            {
                "ok": true,
                "result": {
                    "message_id": 1,
                    "date": 1,
                    "chat": {
                        "id": 1,
                        "type": "private"
                    }
                }
            }
        """.trimIndent()

        val r1: LiveLocationResponse = Json.decodeFromString(booleanRes)
        val r2: LiveLocationResponse = Json.decodeFromString(messageRes)

        println(r1)
        println(r2)
    }

    @Test
    fun a() {
        val r = LocalDate.of(1900, 1, 1)..LocalDate.of(2100, 1, 1)
        println(r.start)
        println(r.endInclusive)
        println(r.contains(LocalDate.now()))
    }

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

    @Test
    fun formatMarkdownV2StringWithoutEntities() {
        val markdown = buildMarkdownV2String {
            text("2+2=4")
        }

        assertEquals("2\\+2\\=4", markdown)
    }

    @Test
    fun encodeMutableCollection() {
        val json = Json
        val obj1 = mutableMapOf("foo" to "bar").toMap()
        val obj2 = mutableMapOf("foo" to "bar")

        val expected = "{\"foo\":\"bar\"}"

        assert(json.encodeToJsonElement(obj1).toString() == expected) // fine
//        assert(json.encodeToJsonElement(obj2).toString() == expected) // throw

        assert(json.encodeToString(serializer(typeOf<Map<String, String>>()), obj1) == expected) // fine
        assert(json.encodeToString(serializer(typeOf<Map<String, String>>()), obj2) == expected) // fine
//        assert(json.encodeToString(serializer(typeOf<MutableMap<String, String>>()), obj2) == expected) // throw
    }

    fun generateCurrency() {
        val json = """
            {
            "AED": {
            "code": "AED",
            "title": "United Arab Emirates Dirham",
            "symbol": "AED",
            "native": "د.إ.‏",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": true,
            "exp": 2,
            "min_amount": "367",
            "max_amount": "3672991"
            },
            "AFN": {
            "code": "AFN",
            "title": "Afghan Afghani",
            "symbol": "AFN",
            "native": "؋",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": false,
            "exp": 2,
            "min_amount": "8503",
            "max_amount": "85030416"
            },
            "ALL": {
            "code": "ALL",
            "title": "Albanian Lek",
            "symbol": "ALL",
            "native": "Lek",
            "thousands_sep": ".",
            "decimal_sep": ",",
            "symbol_left": false,
            "space_between": false,
            "exp": 2,
            "min_amount": "10279",
            "max_amount": "102791475"
            },
            "AMD": {
            "code": "AMD",
            "title": "Armenian Dram",
            "symbol": "AMD",
            "native": "դր.",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": false,
            "space_between": true,
            "exp": 2,
            "min_amount": "49053",
            "max_amount": "490536665"
            },
            "ARS": {
            "code": "ARS",
            "title": "Argentine Peso",
            "symbol": "ARS",
            "native": "${'$'}",
            "thousands_sep": ".",
            "decimal_sep": ",",
            "symbol_left": true,
            "space_between": true,
            "exp": 2,
            "min_amount": "9815",
            "max_amount": "98157992"
            },
            "AUD": {
            "code": "AUD",
            "title": "Australian Dollar",
            "symbol": "AU${'$'}",
            "native": "${'$'}",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": false,
            "exp": 2,
            "min_amount": "136",
            "max_amount": "1366680"
            },
            "AZN": {
            "code": "AZN",
            "title": "Azerbaijani Manat",
            "symbol": "AZN",
            "native": "ман.",
            "thousands_sep": " ",
            "decimal_sep": ",",
            "symbol_left": false,
            "space_between": true,
            "exp": 2,
            "min_amount": "169",
            "max_amount": "1698045"
            },
            "BAM": {
            "code": "BAM",
            "title": "Bosnia & Herzegovina Convertible Mark",
            "symbol": "BAM",
            "native": "KM",
            "thousands_sep": ".",
            "decimal_sep": ",",
            "symbol_left": false,
            "space_between": true,
            "exp": 2,
            "min_amount": "165",
            "max_amount": "1654777"
            },
            "BDT": {
            "code": "BDT",
            "title": "Bangladeshi Taka",
            "symbol": "BDT",
            "native": "৳",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": true,
            "exp": 2,
            "min_amount": "8534",
            "max_amount": "85345579"
            },
            "BGN": {
            "code": "BGN",
            "title": "Bulgarian Lev",
            "symbol": "BGN",
            "native": "лв.",
            "thousands_sep": " ",
            "decimal_sep": ",",
            "symbol_left": false,
            "space_between": true,
            "exp": 2,
            "min_amount": "165",
            "max_amount": "1655478"
            },
            "BND": {
            "code": "BND",
            "title": "Brunei Dollar",
            "symbol": "BND",
            "native": "${'$'}",
            "thousands_sep": ".",
            "decimal_sep": ",",
            "symbol_left": true,
            "space_between": false,
            "exp": 2,
            "min_amount": "134",
            "max_amount": "1341800"
            },
            "BOB": {
            "code": "BOB",
            "title": "Bolivian Boliviano",
            "symbol": "BOB",
            "native": "Bs",
            "thousands_sep": ".",
            "decimal_sep": ",",
            "symbol_left": true,
            "space_between": true,
            "exp": 2,
            "min_amount": "691",
            "max_amount": "6919953"
            },
            "BRL": {
            "code": "BRL",
            "title": "Brazilian Real",
            "symbol": "R${'$'}",
            "native": "R${'$'}",
            "thousands_sep": ".",
            "decimal_sep": ",",
            "symbol_left": true,
            "space_between": true,
            "exp": 2,
            "min_amount": "522",
            "max_amount": "5223302"
            },
            "CAD": {
            "code": "CAD",
            "title": "Canadian Dollar",
            "symbol": "CA${'$'}",
            "native": "${'$'}",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": false,
            "exp": 2,
            "min_amount": "126",
            "max_amount": "1263750"
            },
            "CHF": {
            "code": "CHF",
            "title": "Swiss Franc",
            "symbol": "CHF",
            "native": "CHF",
            "thousands_sep": "'",
            "decimal_sep": ".",
            "symbol_left": false,
            "space_between": true,
            "exp": 2,
            "min_amount": "92",
            "max_amount": "922096"
            },
            "CLP": {
            "code": "CLP",
            "title": "Chilean Peso",
            "symbol": "CLP",
            "native": "${'$'}",
            "thousands_sep": ".",
            "decimal_sep": ",",
            "symbol_left": true,
            "space_between": true,
            "exp": 0,
            "min_amount": "778",
            "max_amount": "7780494"
            },
            "CNY": {
            "code": "CNY",
            "title": "Chinese Renminbi Yuan",
            "symbol": "CN¥",
            "native": "CN¥",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": false,
            "exp": 2,
            "min_amount": "644",
            "max_amount": "6444198"
            },
            "COP": {
            "code": "COP",
            "title": "Colombian Peso",
            "symbol": "COP",
            "native": "${'$'}",
            "thousands_sep": ".",
            "decimal_sep": ",",
            "symbol_left": true,
            "space_between": true,
            "exp": 2,
            "min_amount": "381500",
            "max_amount": "3815000000"
            },
            "CRC": {
            "code": "CRC",
            "title": "Costa Rican Colón",
            "symbol": "CRC",
            "native": "₡",
            "thousands_sep": ".",
            "decimal_sep": ",",
            "symbol_left": true,
            "space_between": false,
            "exp": 2,
            "min_amount": "62520",
            "max_amount": "625206230"
            },
            "CZK": {
            "code": "CZK",
            "title": "Czech Koruna",
            "symbol": "CZK",
            "native": "Kč",
            "thousands_sep": " ",
            "decimal_sep": ",",
            "symbol_left": false,
            "space_between": true,
            "exp": 2,
            "min_amount": "2146",
            "max_amount": "21464597"
            },
            "DKK": {
            "code": "DKK",
            "title": "Danish Krone",
            "symbol": "DKK",
            "native": "kr",
            "thousands_sep": "",
            "decimal_sep": ",",
            "symbol_left": false,
            "space_between": true,
            "exp": 2,
            "min_amount": "631",
            "max_amount": "6311260"
            },
            "DOP": {
            "code": "DOP",
            "title": "Dominican Peso",
            "symbol": "DOP",
            "native": "${'$'}",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": false,
            "exp": 2,
            "min_amount": "5671",
            "max_amount": "56710634"
            },
            "DZD": {
            "code": "DZD",
            "title": "Algerian Dinar",
            "symbol": "DZD",
            "native": "د.ج.‏",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": true,
            "exp": 2,
            "min_amount": "13531",
            "max_amount": "135314991"
            },
            "EGP": {
            "code": "EGP",
            "title": "Egyptian Pound",
            "symbol": "EGP",
            "native": "ج.م.‏",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": true,
            "exp": 2,
            "min_amount": "1571",
            "max_amount": "15713604"
            },
            "EUR": {
            "code": "EUR",
            "title": "Euro",
            "symbol": "€",
            "native": "€",
            "thousands_sep": " ",
            "decimal_sep": ",",
            "symbol_left": false,
            "space_between": true,
            "exp": 2,
            "min_amount": "84",
            "max_amount": "848788"
            },
            "GBP": {
            "code": "GBP",
            "title": "British Pound",
            "symbol": "£",
            "native": "£",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": false,
            "exp": 2,
            "min_amount": "72",
            "max_amount": "723134"
            },
            "GEL": {
            "code": "GEL",
            "title": "Georgian Lari",
            "symbol": "GEL",
            "native": "GEL",
            "thousands_sep": " ",
            "decimal_sep": ",",
            "symbol_left": false,
            "space_between": true,
            "exp": 2,
            "min_amount": "311",
            "max_amount": "3115020"
            },
            "GTQ": {
            "code": "GTQ",
            "title": "Guatemalan Quetzal",
            "symbol": "GTQ",
            "native": "Q",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": false,
            "exp": 2,
            "min_amount": "774",
            "max_amount": "7740475"
            },
            "HKD": {
            "code": "HKD",
            "title": "Hong Kong Dollar",
            "symbol": "HK${'$'}",
            "native": "${'$'}",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": false,
            "exp": 2,
            "min_amount": "778",
            "max_amount": "7783265"
            },
            "HNL": {
            "code": "HNL",
            "title": "Honduran Lempira",
            "symbol": "HNL",
            "native": "L",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": true,
            "exp": 2,
            "min_amount": "2425",
            "max_amount": "24250074"
            },
            "HRK": {
            "code": "HRK",
            "title": "Croatian Kuna",
            "symbol": "HRK",
            "native": "kn",
            "thousands_sep": ".",
            "decimal_sep": ",",
            "symbol_left": false,
            "space_between": true,
            "exp": 2,
            "min_amount": "635",
            "max_amount": "6356995"
            },
            "HUF": {
            "code": "HUF",
            "title": "Hungarian Forint",
            "symbol": "HUF",
            "native": "Ft",
            "thousands_sep": " ",
            "decimal_sep": ",",
            "symbol_left": false,
            "space_between": true,
            "exp": 2,
            "min_amount": "29587",
            "max_amount": "295874993"
            },
            "IDR": {
            "code": "IDR",
            "title": "Indonesian Rupiah",
            "symbol": "IDR",
            "native": "Rp",
            "thousands_sep": ".",
            "decimal_sep": ",",
            "symbol_left": true,
            "space_between": false,
            "exp": 2,
            "min_amount": "1425350",
            "max_amount": "14253500000"
            },
            "ILS": {
            "code": "ILS",
            "title": "Israeli New Sheqel",
            "symbol": "₪",
            "native": "₪",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": true,
            "exp": 2,
            "min_amount": "320",
            "max_amount": "3208310"
            },
            "INR": {
            "code": "INR",
            "title": "Indian Rupee",
            "symbol": "₹",
            "native": "₹",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": false,
            "exp": 2,
            "min_amount": "7342",
            "max_amount": "73420499"
            },
            "ISK": {
            "code": "ISK",
            "title": "Icelandic Króna",
            "symbol": "ISK",
            "native": "kr",
            "thousands_sep": ".",
            "decimal_sep": ",",
            "symbol_left": false,
            "space_between": true,
            "exp": 0,
            "min_amount": "127",
            "max_amount": "1271395"
            },
            "JMD": {
            "code": "JMD",
            "title": "Jamaican Dollar",
            "symbol": "JMD",
            "native": "${'$'}",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": false,
            "exp": 2,
            "min_amount": "14938",
            "max_amount": "149381097"
            },
            "JPY": {
            "code": "JPY",
            "title": "Japanese Yen",
            "symbol": "¥",
            "native": "￥",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": false,
            "exp": 0,
            "min_amount": "109",
            "max_amount": "1093219"
            },
            "KES": {
            "code": "KES",
            "title": "Kenyan Shilling",
            "symbol": "KES",
            "native": "Ksh",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": false,
            "exp": 2,
            "min_amount": "11004",
            "max_amount": "110049923"
            },
            "KGS": {
            "code": "KGS",
            "title": "Kyrgyzstani Som",
            "symbol": "KGS",
            "native": "KGS",
            "thousands_sep": " ",
            "decimal_sep": "-",
            "symbol_left": false,
            "space_between": true,
            "exp": 2,
            "min_amount": "8479",
            "max_amount": "84795972"
            },
            "KRW": {
            "code": "KRW",
            "title": "South Korean Won",
            "symbol": "₩",
            "native": "₩",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": false,
            "exp": 0,
            "min_amount": "1172",
            "max_amount": "11720801"
            },
            "KZT": {
            "code": "KZT",
            "title": "Kazakhstani Tenge",
            "symbol": "KZT",
            "native": "₸",
            "thousands_sep": " ",
            "decimal_sep": "-",
            "symbol_left": true,
            "space_between": false,
            "exp": 2,
            "min_amount": "42664",
            "max_amount": "426649632"
            },
            "LBP": {
            "code": "LBP",
            "title": "Lebanese Pound",
            "symbol": "LBP",
            "native": "ل.ل.‏",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": true,
            "exp": 2,
            "min_amount": "152822",
            "max_amount": "1528222616"
            },
            "LKR": {
            "code": "LKR",
            "title": "Sri Lankan Rupee",
            "symbol": "LKR",
            "native": "රු.",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": true,
            "exp": 2,
            "min_amount": "19964",
            "max_amount": "199641772"
            },
            "MAD": {
            "code": "MAD",
            "title": "Moroccan Dirham",
            "symbol": "MAD",
            "native": "د.م.‏",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": true,
            "exp": 2,
            "min_amount": "894",
            "max_amount": "8946498"
            },
            "MDL": {
            "code": "MDL",
            "title": "Moldovan Leu",
            "symbol": "MDL",
            "native": "MDL",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": false,
            "space_between": true,
            "exp": 2,
            "min_amount": "1769",
            "max_amount": "17691657"
            },
            "MNT": {
            "code": "MNT",
            "title": "Mongolian Tögrög",
            "symbol": "MNT",
            "native": "MNT",
            "thousands_sep": " ",
            "decimal_sep": ",",
            "symbol_left": true,
            "space_between": false,
            "exp": 2,
            "min_amount": "283923",
            "max_amount": "2839237786"
            },
            "MUR": {
            "code": "MUR",
            "title": "Mauritian Rupee",
            "symbol": "MUR",
            "native": "MUR",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": false,
            "exp": 2,
            "min_amount": "4265",
            "max_amount": "42650475"
            },
            "MVR": {
            "code": "MVR",
            "title": "Maldivian Rufiyaa",
            "symbol": "MVR",
            "native": "MVR",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": false,
            "space_between": true,
            "exp": 2,
            "min_amount": "1539",
            "max_amount": "15399549"
            },
            "MXN": {
            "code": "MXN",
            "title": "Mexican Peso",
            "symbol": "MX${'$'}",
            "native": "${'$'}",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": false,
            "exp": 2,
            "min_amount": "1987",
            "max_amount": "19870775"
            },
            "MYR": {
            "code": "MYR",
            "title": "Malaysian Ringgit",
            "symbol": "MYR",
            "native": "RM",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": false,
            "exp": 2,
            "min_amount": "415",
            "max_amount": "4158501"
            },
            "MZN": {
            "code": "MZN",
            "title": "Mozambican Metical",
            "symbol": "MZN",
            "native": "MTn",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": false,
            "exp": 2,
            "min_amount": "6378",
            "max_amount": "63785009"
            },
            "NGN": {
            "code": "NGN",
            "title": "Nigerian Naira",
            "symbol": "NGN",
            "native": "₦",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": false,
            "exp": 2,
            "min_amount": "41179",
            "max_amount": "411790459"
            },
            "NIO": {
            "code": "NIO",
            "title": "Nicaraguan Córdoba",
            "symbol": "NIO",
            "native": "C${'$'}",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": true,
            "exp": 2,
            "min_amount": "3514",
            "max_amount": "35149769"
            },
            "NOK": {
            "code": "NOK",
            "title": "Norwegian Krone",
            "symbol": "NOK",
            "native": "kr",
            "thousands_sep": " ",
            "decimal_sep": ",",
            "symbol_left": true,
            "space_between": true,
            "exp": 2,
            "min_amount": "859",
            "max_amount": "8596330"
            },
            "NPR": {
            "code": "NPR",
            "title": "Nepalese Rupee",
            "symbol": "NPR",
            "native": "नेरू",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": false,
            "exp": 2,
            "min_amount": "11791",
            "max_amount": "117910536"
            },
            "NZD": {
            "code": "NZD",
            "title": "New Zealand Dollar",
            "symbol": "NZ${'$'}",
            "native": "${'$'}",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": false,
            "exp": 2,
            "min_amount": "140",
            "max_amount": "1405110"
            },
            "PAB": {
            "code": "PAB",
            "title": "Panamanian Balboa",
            "symbol": "PAB",
            "native": "B/.",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": true,
            "exp": 2,
            "min_amount": "100",
            "max_amount": "1000736"
            },
            "PEN": {
            "code": "PEN",
            "title": "Peruvian Nuevo Sol",
            "symbol": "PEN",
            "native": "S/.",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": true,
            "exp": 2,
            "min_amount": "410",
            "max_amount": "4109527"
            },
            "PHP": {
            "code": "PHP",
            "title": "Philippine Peso",
            "symbol": "PHP",
            "native": "₱",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": false,
            "exp": 2,
            "min_amount": "4994",
            "max_amount": "49945018"
            },
            "PKR": {
            "code": "PKR",
            "title": "Pakistani Rupee",
            "symbol": "PKR",
            "native": "₨",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": false,
            "exp": 2,
            "min_amount": "16950",
            "max_amount": "169500733"
            },
            "PLN": {
            "code": "PLN",
            "title": "Polish Złoty",
            "symbol": "PLN",
            "native": "zł",
            "thousands_sep": " ",
            "decimal_sep": ",",
            "symbol_left": false,
            "space_between": true,
            "exp": 2,
            "min_amount": "388",
            "max_amount": "3880010"
            },
            "PYG": {
            "code": "PYG",
            "title": "Paraguayan Guaraní",
            "symbol": "PYG",
            "native": "₲",
            "thousands_sep": ".",
            "decimal_sep": ",",
            "symbol_left": true,
            "space_between": true,
            "exp": 0,
            "min_amount": "6896",
            "max_amount": "68964321"
            },
            "QAR": {
            "code": "QAR",
            "title": "Qatari Riyal",
            "symbol": "QAR",
            "native": "ر.ق.‏",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": true,
            "exp": 2,
            "min_amount": "364",
            "max_amount": "3640975"
            },
            "RON": {
            "code": "RON",
            "title": "Romanian Leu",
            "symbol": "RON",
            "native": "RON",
            "thousands_sep": ".",
            "decimal_sep": ",",
            "symbol_left": false,
            "space_between": true,
            "exp": 2,
            "min_amount": "419",
            "max_amount": "4199702"
            },
            "RSD": {
            "code": "RSD",
            "title": "Serbian Dinar",
            "symbol": "RSD",
            "native": "дин.",
            "thousands_sep": ".",
            "decimal_sep": ",",
            "symbol_left": false,
            "space_between": true,
            "exp": 2,
            "min_amount": "9948",
            "max_amount": "99481357"
            },
            "RUB": {
            "code": "RUB",
            "title": "Russian Ruble",
            "symbol": "RUB",
            "native": "руб.",
            "thousands_sep": " ",
            "decimal_sep": ",",
            "symbol_left": false,
            "space_between": true,
            "exp": 2,
            "min_amount": "7244",
            "max_amount": "72446503"
            },
            "SAR": {
            "code": "SAR",
            "title": "Saudi Riyal",
            "symbol": "SAR",
            "native": "ر.س.‏",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": true,
            "exp": 2,
            "min_amount": "375",
            "max_amount": "3750410"
            },
            "SEK": {
            "code": "SEK",
            "title": "Swedish Krona",
            "symbol": "SEK",
            "native": "kr",
            "thousands_sep": ".",
            "decimal_sep": ",",
            "symbol_left": false,
            "space_between": true,
            "exp": 2,
            "min_amount": "860",
            "max_amount": "8608360"
            },
            "SGD": {
            "code": "SGD",
            "title": "Singapore Dollar",
            "symbol": "SGD",
            "native": "${'$'}",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": false,
            "exp": 2,
            "min_amount": "134",
            "max_amount": "1342580"
            },
            "THB": {
            "code": "THB",
            "title": "Thai Baht",
            "symbol": "฿",
            "native": "฿",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": false,
            "exp": 2,
            "min_amount": "3297",
            "max_amount": "32976498"
            },
            "TJS": {
            "code": "TJS",
            "title": "Tajikistani Somoni",
            "symbol": "TJS",
            "native": "TJS",
            "thousands_sep": " ",
            "decimal_sep": ";",
            "symbol_left": false,
            "space_between": true,
            "exp": 2,
            "min_amount": "1134",
            "max_amount": "11343058"
            },
            "TRY": {
            "code": "TRY",
            "title": "Turkish Lira",
            "symbol": "TRY",
            "native": "TL",
            "thousands_sep": ".",
            "decimal_sep": ",",
            "symbol_left": false,
            "space_between": true,
            "exp": 2,
            "min_amount": "844",
            "max_amount": "8444250"
            },
            "TTD": {
            "code": "TTD",
            "title": "Trinidad and Tobago Dollar",
            "symbol": "TTD",
            "native": "${'$'}",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": false,
            "exp": 2,
            "min_amount": "679",
            "max_amount": "6795496"
            },
            "TWD": {
            "code": "TWD",
            "title": "New Taiwan Dollar",
            "symbol": "NT${'$'}",
            "native": "NT${'$'}",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": false,
            "exp": 2,
            "min_amount": "2770",
            "max_amount": "27704501"
            },
            "TZS": {
            "code": "TZS",
            "title": "Tanzanian Shilling",
            "symbol": "TZS",
            "native": "TSh",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": false,
            "exp": 2,
            "min_amount": "231899",
            "max_amount": "2318999792"
            },
            "UAH": {
            "code": "UAH",
            "title": "Ukrainian Hryvnia",
            "symbol": "UAH",
            "native": "₴",
            "thousands_sep": " ",
            "decimal_sep": ",",
            "symbol_left": false,
            "space_between": false,
            "exp": 2,
            "min_amount": "2666",
            "max_amount": "26661647"
            },
            "UGX": {
            "code": "UGX",
            "title": "Ugandan Shilling",
            "symbol": "UGX",
            "native": "USh",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": false,
            "exp": 0,
            "min_amount": "3532",
            "max_amount": "35325273"
            },
            "USD": {
            "code": "USD",
            "title": "United States Dollar",
            "symbol": "${'$'}",
            "native": "${'$'}",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": false,
            "exp": 2,
            "min_amount": "100",
            "max_amount": 1000000
            },
            "UYU": {
            "code": "UYU",
            "title": "Uruguayan Peso",
            "symbol": "UYU",
            "native": "${'$'}",
            "thousands_sep": ".",
            "decimal_sep": ",",
            "symbol_left": true,
            "space_between": true,
            "exp": 2,
            "min_amount": "4278",
            "max_amount": "42785106"
            },
            "UZS": {
            "code": "UZS",
            "title": "Uzbekistani Som",
            "symbol": "UZS",
            "native": "UZS",
            "thousands_sep": " ",
            "decimal_sep": ",",
            "symbol_left": false,
            "space_between": true,
            "exp": 2,
            "min_amount": "1071361",
            "max_amount": "10713612481"
            },
            "VND": {
            "code": "VND",
            "title": "Vietnamese Đồng",
            "symbol": "₫",
            "native": "₫",
            "thousands_sep": ".",
            "decimal_sep": ",",
            "symbol_left": false,
            "space_between": true,
            "exp": 0,
            "min_amount": "22756",
            "max_amount": "227565000"
            },
            "YER": {
            "code": "YER",
            "title": "Yemeni Rial",
            "symbol": "YER",
            "native": "ر.ي.‏",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": true,
            "exp": 2,
            "min_amount": "25084",
            "max_amount": "250849446"
            },
            "ZAR": {
            "code": "ZAR",
            "title": "South African Rand",
            "symbol": "ZAR",
            "native": "R",
            "thousands_sep": ",",
            "decimal_sep": ".",
            "symbol_left": true,
            "space_between": true,
            "exp": 2,
            "min_amount": "1448",
            "max_amount": "14484800"
            }
            }
        """.trimIndent()

        val jsonObject = Json.decodeFromString<JsonObject>(json)
        val list = mutableListOf<String>()
        jsonObject.mapKeys {
            val code = (it.value as JsonObject)["code"]
            val title = (it.value as JsonObject)["title"]
            val symbol = (it.value as JsonObject)["symbol"]
            val native = (it.value as JsonObject)["native"]
            val thousands_sep = (it.value as JsonObject)["thousands_sep"]
            val decimal_sep = (it.value as JsonObject)["decimal_sep"]
            val symbol_left = (it.value as JsonObject)["symbol_left"]
            val space_between = (it.value as JsonObject)["space_between"]
            val exp = (it.value as JsonObject)["exp"]
            val min_amount = (it.value as JsonObject)["min_amount"]
            val max_amount = (it.value as JsonObject)["max_amount"]
            list.add("${it.key}($code, $title, $symbol, $native, $thousands_sep, $decimal_sep, $symbol_left, $space_between, $exp, $min_amount, $max_amount)")
        }

        File("currency-output.txt").outputStream().use { file ->
            list.forEach {
                file.write(it.toByteArray(Charset.forName("UTF-8")))
                file.write("\n".toByteArray())
            }
        }
    }

}