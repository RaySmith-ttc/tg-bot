package ru.raysmith.tgbot.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import kotlin.math.pow

// TODO create test that verify data with https://core.telegram.org/bots/payments/currencies.json

/**
 * Represents a currency with its formatting and constraints.
 *
 * @property code The ISO-4217 currency code.
 * @property title The full name of the currency.
 * @property symbol The currency symbol.
 * @property native The native representation of the currency symbol.
 * @property thousandsSep The character used as a thousands separator.
 * @property decimalSep The character used as a decimal separator.
 * @property symbolLeft Indicates if the symbol is placed to the left of the amount.
 * @property spaceBetween Indicates if there is a space between the symbol and the amount.
 * @property exp The exponent used to define the smallest unit of the currency.
 * @property minAmount The minimum amount allowed for transactions in the smallest unit.
 * @property maxAmount The maximum amount allowed for transactions in the smallest unit.
 */
@Serializable
enum class Currency(
    val code: String,
    val title: String,
    val symbol: String,
    val native: String,
    val thousandsSep: String,
    val decimalSep: Char,
    val symbolLeft: Boolean,
    val spaceBetween: Boolean,
    val exp: Int,
    val minAmount: Long,
    val maxAmount: Long
) {
    @SerialName("XTR") XTR("XTR", "Telegram Stars", "⭐", "⭐", "", '.', true, true, 0, 1, Long.MAX_VALUE),

    /**
     * United Arab Emirates Dirham
     *
     * Format:
     * - native: `د.إ.‏`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `367`
     * - max amount: `3672991`
     * */
    @SerialName("AED") AED("AED", "United Arab Emirates Dirham", "AED", "د.إ.‏", ",", '.', true, true, 2, 367, 3672991),

    /**
     * Afghan Afghani
     *
     * Format:
     * - native: `؋`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `false`
     * - exponent: `2`
     * - min amount: `8503`
     * - max amount: `85030416`
     * */
    @SerialName("AFN") AFN("AFN", "Afghan Afghani", "AFN", "؋", ",", '.', true, false, 2, 8503, 85030416),

    /**
     * Albanian Lek
     *
     * Format:
     * - native: `ALL`
     * - thousands separator: `.`
     * - decimal separator: `,`
     * - symbol left: `false`
     * - space between: `false`
     * - exponent: `2`
     * - min amount: `10279`
     * - max amount: `102791475`
     * */
    @SerialName("ALL") ALL("ALL", "Albanian Lek", "ALL", "Lek", ".", ',', false, false, 2, 10279, 102791475),

    /**
     * Armenian Dram
     *
     * Format:
     * - native: `AMD`
     * - thousands separator: `.`
     * - decimal separator: `,`
     * - symbol left: `false`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `49053`
     * - max amount: `490536665`
     * */
    @SerialName("AMD") AMD("AMD", "Armenian Dram", "AMD", "դր.", ",", '.', false, true, 2, 49053, 490536665),

    /**
     * Argentine Peso
     *
     * Format:
     * - native: `$`
     * - thousands separator: `.`
     * - decimal separator: `,`
     * - symbol left: `true`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `9815`
     * - max amount: `98157992`
     * */
    @SerialName("ARS") ARS("ARS", "Argentine Peso", "ARS", "$", ".", ',', true, true, 2, 9815, 98157992),

    /**
     * Australian Dollar
     *
     * Format:
     * - native: `AU$`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `false`
     * - exponent: `2`
     * - min amount: `136`
     * - max amount: `1366680`
     * */
    @SerialName("AUD") AUD("AUD", "Australian Dollar", "AU$", "$", ",", '.', true, false, 2, 136, 1366680),

    /**
     * Azerbaijani Manat
     *
     * Format:
     * - native: `ман.`
     * - thousands separator: ` `
     * - decimal separator: `,`
     * - symbol left: `false`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `169`
     * - max amount: `1698045`
     * */
    @SerialName("AZN") AZN("AZN", "Azerbaijani Manat", "AZN", "ман.", " ", ',', false, true, 2, 169, 1698045),

    /**
     * Bosnia and Herzegovina Convertible Mark
     *
     * Format:
     * - native: `KM`
     * - thousands separator: `.`
     * - decimal separator: `,`
     * - symbol left: `false`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `165`
     * - max amount: `1654777`
     * */
    @SerialName("BAM") BAM("BAM", "Bosnia & Herzegovina Convertible Mark", "BAM", "KM", ".", ',', false, true, 2, 165, 1654777),

    /**
     * Bangladeshi Taka
     *
     * Format:
     * - native: `৳`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `8534`
     * - max amount: `85345579`
     * */
    @SerialName("BDT") BDT("BDT", "Bangladeshi Taka", "BDT", "৳", ",", '.', true, true, 2, 8534, 85345579),

    /**
     * Bulgarian Lev
     *
     * Format:
     * - native: `лв.`
     * - thousands separator: ` `
     * - decimal separator: `,`
     * - symbol left: `false`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `165`
     * - max amount: `1655478`
     * */
    @SerialName("BGN") BGN("BGN", "Bulgarian Lev", "BGN", "лв.", " ", ',', false, true, 2, 165, 1655478),

    /**
     * Bahraini Dinar
     *
     * Format:
     * - native: `ب.د.‏`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `false`
     * - exponent: `3`
     * - min amount: `265`
     * - max amount: `2650000`
     * */
    @SerialName("BND") BND("BND", "Brunei Dollar", "BND", "$", ".", ',', true, false, 2, 134, 1341800),

    /**
     * Bolivian Boliviano
     *
     * Format:
     * - native: `Bs`
     * - thousands separator: `.`
     * - decimal separator: `,`
     * - symbol left: `true`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `691`
     * - max amount: `6919953`
     * */
    @SerialName("BOB") BOB("BOB", "Bolivian Boliviano", "BOB", "Bs", ".", ',', true, true, 2, 691, 6919953),

    /**
     * Brazilian Real
     *
     * Format:
     * - native: `R$`
     * - thousands separator: `.`
     * - decimal separator: `,`
     * - symbol left: `true`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `522`
     * - max amount: `5223302`
     * */
    @SerialName("BRL") BRL("BRL", "Brazilian Real", "R$", "R$", ".", ',', true, true, 2, 522, 5223302),

    /**
     * Canadian Dollar
     *
     * Format:
     * - native: `CA$`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `false`
     * - exponent: `2`
     * - min amount: `126`
     * - max amount: `1263750`
     * */
    @SerialName("CAD") CAD("CAD", "Canadian Dollar", "CA$", "$", ",", '.', true, false, 2, 126, 1263750),

    /**
     * Swiss Franc
     *
     * Format:
     * - native: `CHF`
     * - thousands separator: `'`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `false`
     * - exponent: `2`
     * - min amount: `92`
     * - max amount: `922096`
     * */
    @SerialName("CHF") CHF("CHF", "Swiss Franc", "CHF", "CHF", "'", '.', false, true, 2, 92, 922096),

    /**
     * Chilean Peso
     *
     * Format:
     * - native: `CLP`
     * - thousands separator: `.`
     * - decimal separator: `,`
     * - symbol left: `true`
     * - space between: `true`
     * - exponent: `0`
     * - min amount: `778`
     * - max amount: `7780494`
     * */
    @SerialName("CLP") CLP("CLP", "Chilean Peso", "CLP", "$", ".", ',', true, true, 0, 778, 7780494),

    /**
     * Chinese Renminbi Yuan
     *
     * Format:
     * - native: `CN¥`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `false`
     * - exponent: `2`
     * - min amount: `644`
     * - max amount: `6444198`
     * */
    @SerialName("CNY") CNY("CNY", "Chinese Renminbi Yuan", "CN¥", "CN¥", ",", '.', true, false, 2, 644, 6444198),

    /**
     * Colombian Peso
     *
     * Format:
     * - native: `COP`
     * - thousands separator: `.`
     * - decimal separator: `,`
     * - symbol left: `true`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `381500`
     * - max amount: `3815000000`
     * */
    @SerialName("COP") COP("COP", "Colombian Peso", "COP", "$", ".", ',', true, true, 2, 381500, 3815000000),

    /**
     * Costa Rican Colón
     *
     * Format:
     * - native: `₡`
     * - thousands separator: `.`
     * - decimal separator: `,`
     * - symbol left: `true`
     * - space between: `false`
     * - exponent: `2`
     * - min amount: `62520`
     * - max amount: `625206230`
     * */
    @SerialName("CRC") CRC("CRC", "Costa Rican Colón", "CRC", "₡", ".", ',', true, false, 2, 62520, 625206230),

    /**
     * Czech Koruna
     *
     * Format:
     * - native: `Kč`
     * - thousands separator: ` `
     * - decimal separator: `,`
     * - symbol left: `false`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `2146`
     * - max amount: `21464597`
     * */
    @SerialName("CZK") CZK("CZK", "Czech Koruna", "CZK", "Kč", " ", ',', false, true, 2, 2146, 21464597),

    /**
     * Danish Krone
     *
     * Format:
     * - native: `kr`
     * - thousands separator: `.`
     * - decimal separator: `,`
     * - symbol left: `false`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `631`
     * - max amount: `6311260`
     * */
    @SerialName("DKK") DKK("DKK", "Danish Krone", "DKK", "kr", ".", ',', false, true, 2, 631, 6311260),

    /**
     * Dominican Peso
     *
     * Format:
     * - native: `$`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `false`
     * - exponent: `2`
     * - min amount: `5671`
     * - max amount: `56710634`
     * */
    @SerialName("DOP") DOP("DOP", "Dominican Peso", "DOP", "$", ",", '.', true, false, 2, 5671, 56710634),

    /**
     * Algerian Dinar
     *
     * Format:
     * - native: `د.ج.‏`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `13531`
     * - max amount: `135314991`
     * */
    @SerialName("DZD") DZD("DZD", "Algerian Dinar", "DZD", "د.ج.‏", ",", '.', true, true, 2, 13531, 135314991),

    /**
     * Egyptian Pound
     *
     * Format:
     * - native: `ج.م.‏`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `1571`
     * - max amount: `15713604`
     * */
    @SerialName("EGP") EGP("EGP", "Egyptian Pound", "EGP", "ج.م.‏", ",", '.', true, true, 2, 1571, 15713604),

    /**
     * Euro
     *
     * Format:
     * - native: `€`
     * - thousands separator: ` `
     * - decimal separator: `,`
     * - symbol left: `false`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `84`
     * - max amount: `848788`
     * */
    @SerialName("EUR") EUR("EUR", "Euro", "€", "€", " ", ',', false, true, 2, 84, 848788),

    /**
     * British Pound
     *
     * Format:
     * - native: `£`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `false`
     * - exponent: `2`
     * - min amount: `72`
     * - max amount: `723134`
     * */
    @SerialName("GBP") GBP("GBP", "British Pound", "£", "£", ",", '.', true, false, 2, 72, 723134),

    /**
     * Georgian Lari
     *
     * Format:
     * - native: `GEL`
     * - thousands separator: ` `
     * - decimal separator: `,`
     * - symbol left: `false`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `311`
     * - max amount: `3115020`
     * */
    @SerialName("GEL") GEL("GEL", "Georgian Lari", "GEL", "GEL", " ", ',', false, true, 2, 311, 3115020),

    /**
     * Guatemalan Quetzal
     *
     * Format:
     * - native: `Q`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `false`
     * - exponent: `2`
     * - min amount: `774`
     * - max amount: `7740475`
     * */
    @SerialName("GTQ") GTQ("GTQ", "Guatemalan Quetzal", "GTQ", "Q", ",", '.', true, false, 2, 774, 7740475),

    /**
     * Hong Kong Dollar
     *
     * Format:
     * - native: `$`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `false`
     * - exponent: `2`
     * - min amount: `778`
     * - max amount: `7783265`
     * */
    @SerialName("HKD") HKD("HKD", "Hong Kong Dollar", "HK$", "$", ",", '.', true, false, 2, 778, 7783265),

    /**
     * Honduran Lempira
     *
     * Format:
     * - native: `L`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `2425`
     * - max amount: `24250074`
     * */
    @SerialName("HNL") HNL("HNL", "Honduran Lempira", "HNL", "L", ",", '.', true, true, 2, 2425, 24250074),

    /**
     * Croatian Kuna
     *
     * Format:
     * - native: `kn`
     * - thousands separator: `.`
     * - decimal separator: `,`
     * - symbol left: `false`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `635`
     * - max amount: `6356995`
     * */
    @SerialName("HRK") HRK("HRK", "Croatian Kuna", "HRK", "kn", ".", ',', false, true, 2, 635, 6356995),

    /**
     * Hungarian Forint
     *
     * Format:
     * - native: `Ft`
     * - thousands separator: ` `
     * - decimal separator: `,`
     * - symbol left: `false`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `29587`
     * - max amount: `295874993`
     * */
    @SerialName("HUF") HUF("HUF", "Hungarian Forint", "HUF", "Ft", " ", ',', false, true, 2, 29587, 295874993),

    /**
     * Indonesian Rupiah
     *
     * Format:
     * - native: `Rp`
     * - thousands separator: `.`
     * - decimal separator: `,`
     * - symbol left: `true`
     * - space between: `false`
     * - exponent: `2`
     * - min amount: `1425350`
     * - max amount: `14253500000`
     * */
    @SerialName("IDR") IDR("IDR", "Indonesian Rupiah", "IDR", "Rp", ".", ',', true, false, 2, 1425350, 14253500000),

    /**
     * Israeli New Sheqel
     *
     * Format:
     * - native: `₪`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `320`
     * - max amount: `3208310`
     * */
    @SerialName("ILS") ILS("ILS", "Israeli New Sheqel", "₪", "₪", ",", '.', true, true, 2, 320, 3208310),

    /**
     * Indian Rupee
     *
     * Format:
     * - native: `₹`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `false`
     * - exponent: `2`
     * - min amount: `7342`
     * - max amount: `73420499`
     * */
    @SerialName("INR") INR("INR", "Indian Rupee", "₹", "₹", ",", '.', true, false, 2, 7342, 73420499),

    /**
     * Icelandic Króna
     *
     * Format:
     * - native: `kr`
     * - thousands separator: `.`
     * - decimal separator: `,`
     * - symbol left: `false`
     * - space between: `true`
     * - exponent: `0`
     * - min amount: `127`
     * - max amount: `1271395`
     * */
    @SerialName("ISK") ISK("ISK", "Icelandic Króna", "ISK", "kr", ".", ',', false, true, 0, 127, 1271395),

    /**
     * Jamaican Dollar
     *
     * Format:
     * - native: `$`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `false`
     * - exponent: `2`
     * - min amount: `14938`
     * - max amount: `149381097`
     * */
    @SerialName("JMD") JMD("JMD", "Jamaican Dollar", "JMD", "$", ",", '.', true, false, 2, 14938, 149381097),

    /**
     * Japanese Yen
     *
     * Format:
     * - native: `￥`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `false`
     * - exponent: `0`
     * - min amount: `109`
     * - max amount: `1093219`
     * */
    @SerialName("JPY") JPY("JPY", "Japanese Yen", "¥", "￥", ",", '.', true, false, 0, 109, 1093219),

    /**
     * Kenyan Shilling
     *
     * Format:
     * - native: `Ksh`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `false`
     * - exponent: `2`
     * - min amount: `11004`
     * - max amount: `110049923`
     * */
    @SerialName("KES") KES("KES", "Kenyan Shilling", "KES", "Ksh", ",", '.', true, false, 2, 11004, 110049923),

    /**
     * Kyrgyzstani Som
     *
     * Format:
     * - native: `KGS`
     * - thousands separator: ` `
     * - decimal separator: `-`
     * - symbol left: `false`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `8479`
     * - max amount: `84795972`
     * */
    @SerialName("KGS") KGS("KGS", "Kyrgyzstani Som", "KGS", "KGS", " ", '-', false, true, 2, 8479, 84795972),

    /**
     * South Korean Won
     *
     * Format:
     * - native: `₩`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `false`
     * - exponent: `0`
     * - min amount: `1172`
     * - max amount: `11720801`
     * */
    @SerialName("KRW") KRW("KRW", "South Korean Won", "₩", "₩", ",", '.', true, false, 0, 1172, 11720801),

    /**
     * Kazakhstani Tenge
     *
     * Format:
     * - native: `₸`
     * - thousands separator: ` `
     * - decimal separator: `-`
     * - symbol left: `true`
     * - space between: `false`
     * - exponent: `2`
     * - min amount: `42664`
     * - max amount: `426649632`
     * */
    @SerialName("KZT") KZT("KZT", "Kazakhstani Tenge", "KZT", "₸", " ", '-', true, false, 2, 42664, 426649632),

    /**
     * Lebanese Pound
     *
     * Format:
     * - native: `ل.ل.‏`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `152822`
     * - max amount: `1528222616`
     * */
    @SerialName("LBP") LBP("LBP", "Lebanese Pound", "LBP", "ل.ل.‏", ",", '.', true, true, 2, 152822, 1528222616),

    /**
     * Sri Lankan Rupee
     *
     * Format:
     * - native: `රු.`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `19964`
     * - max amount: `199641772`
     * */
    @SerialName("LKR") LKR("LKR", "Sri Lankan Rupee", "LKR", "රු.", ",", '.', true, true, 2, 19964, 199641772),

    /**
     * Moroccan Dirham
     *
     * Format:
     * - native: `د.م.‏`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `894`
     * - max amount: `8946498`
     * */
    @SerialName("MAD") MAD("MAD", "Moroccan Dirham", "MAD", "د.م.‏", ",", '.', true, true, 2, 894, 8946498),

    /**
     * Moldovan Leu
     *
     * Format:
     * - native: `MDL`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `false`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `1769`
     * - max amount: `17691657`
     * */
    @SerialName("MDL") MDL("MDL", "Moldovan Leu", "MDL", "MDL", ",", '.', false, true, 2, 1769, 17691657),

    /**
     * Mongolian Tögrög
     *
     * Format:
     * - native: `MNT`
     * - thousands separator: ` `
     * - decimal separator: `,`
     * - symbol left: `true`
     * - space between: `false`
     * - exponent: `2`
     * - min amount: `283923`
     * - max amount: `2839237786`
     * */
    @SerialName("MNT") MNT("MNT", "Mongolian Tögrög", "MNT", "MNT", " ", ',', true, false, 2, 283923, 2839237786),

    /**
     * Mauritian Rupee
     *
     * Format:
     * - native: `MUR`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `false`
     * - exponent: `2`
     * - min amount: `4265`
     * - max amount: `42650475`
     * */
    @SerialName("MUR") MUR("MUR", "Mauritian Rupee", "MUR", "MUR", ",", '.', true, false, 2, 4265, 42650475),

    /**
     * Maldivian Rufiyaa
     *
     * Format:
     * - native: `MVR`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `false`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `1539`
     * - max amount: `15399549`
     * */
    @SerialName("MVR") MVR("MVR", "Maldivian Rufiyaa", "MVR", "MVR", ",", '.', false, true, 2, 1539, 15399549),

    /**
     * Mexican Peso
     *
     * Format:
     * - native: `$`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `false`
     * - exponent: `2`
     * - min amount: `1987`
     * - max amount: `19870775`
     * */
    @SerialName("MXN") MXN("MXN", "Mexican Peso", "MX$", "$", ",", '.', true, false, 2, 1987, 19870775),

    /**
     * Malaysian Ringgit
     *
     * Format:
     * - native: `RM`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `false`
     * - exponent: `2`
     * - min amount: `415`
     * - max amount: `4158501`
     * */
    @SerialName("MYR") MYR("MYR", "Malaysian Ringgit", "MYR", "RM", ",", '.', true, false, 2, 415, 4158501),

    /**
     * Mozambican Metical
     *
     * Format:
     * - native: `MTn`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `false`
     * - exponent: `2`
     * - min amount: `6378`
     * - max amount: `63785009`
     * */
    @SerialName("MZN") MZN("MZN", "Mozambican Metical", "MZN", "MTn", ",", '.', true, false, 2, 6378, 63785009),

    /**
     * Nigerian Naira
     *
     * Format:
     * - native: `₦`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `false`
     * - exponent: `2`
     * - min amount: `41179`
     * - max amount: `411790459`
     * */
    @SerialName("NGN") NGN("NGN", "Nigerian Naira", "NGN", "₦", ",", '.', true, false, 2, 41179, 411790459),

    /**
     * Nicaraguan Córdoba
     *
     * Format:
     * - native: `C$`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `3514`
     * - max amount: `35149769`
     * */
    @SerialName("NIO") NIO("NIO", "Nicaraguan Córdoba", "NIO", "C$", ",", '.', true, true, 2, 3514, 35149769),

    /**
     * Norwegian Krone
     *
     * Format:
     * - native: `kr`
     * - thousands separator: ` `
     * - decimal separator: `,`
     * - symbol left: `true`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `859`
     * - max amount: `8596330`
     * */
    @SerialName("NOK") NOK("NOK", "Norwegian Krone", "NOK", "kr", " ", ',', true, true, 2, 859, 8596330),

    /**
     * Nepalese Rupee
     *
     * Format:
     * - native: `नेरू`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `false`
     * - exponent: `2`
     * - min amount: `11791`
     * - max amount: `117910536`
     * */
    @SerialName("NPR") NPR("NPR", "Nepalese Rupee", "NPR", "नेरू", ",", '.', true, false, 2, 11791, 117910536),

    /**
     * New Zealand Dollar
     *
     * Format:
     * - native: `$`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `false`
     * - exponent: `2`
     * - min amount: `140`
     * - max amount: `1405110`
     * */
    @SerialName("NZD") NZD("NZD", "New Zealand Dollar", "NZ$", "$", ",", '.', true, false, 2, 140, 1405110),

    /**
     * Panamanian Balboa
     *
     * Format:
     * - native: `B/.`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `100`
     * - max amount: `1000736`
     * */
    @SerialName("PAB") PAB("PAB", "Panamanian Balboa", "PAB", "B/.", ",", '.', true, true, 2, 100, 1000736),

    /**
     * Peruvian Nuevo Sol
     *
     * Format:
     * - native: `S/.`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `410`
     * - max amount: `4109527`
     * */
    @SerialName("PEN") PEN("PEN", "Peruvian Nuevo Sol", "PEN", "S/.", ",", '.', true, true, 2, 410, 4109527),

    /**
     * Philippine Peso
     *
     * Format:
     * - native: `₱`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `false`
     * - exponent: `2`
     * - min amount: `4994`
     * - max amount: `49945018`
     * */
    @SerialName("PHP") PHP("PHP", "Philippine Peso", "PHP", "₱", ",", '.', true, false, 2, 4994, 49945018),

    /**
     * Pakistani Rupee
     *
     * Format:
     * - native: `₨`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `false`
     * - exponent: `2`
     * - min amount: `16950`
     * - max amount: `169500733`
     * */
    @SerialName("PKR") PKR("PKR", "Pakistani Rupee", "PKR", "₨", ",", '.', true, false, 2, 16950, 169500733),

    /**
     * Polish Złoty
     *
     * Format:
     * - native: `zł`
     * - thousands separator: ` `
     * - decimal separator: `,`
     * - symbol left: `false`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `388`
     * - max amount: `3880010`
     * */
    @SerialName("PLN") PLN("PLN", "Polish Złoty", "PLN", "zł", " ", ',', false, true, 2, 388, 3880010),

    /**
     * Paraguayan Guaraní
     *
     * Format:
     * - native: `₲`
     * - thousands separator: `.`
     * - decimal separator: `,`
     * - symbol left: `true`
     * - space between: `true`
     * - exponent: `0`
     * - min amount: `6896`
     * - max amount: `68964321`
     * */
    @SerialName("PYG") PYG("PYG", "Paraguayan Guaraní", "PYG", "₲", ".", ',', true, true, 0, 6896, 68964321),

    /**
     * Qatari Riyal
     *
     * Format:
     * - native: `ر.ق.‏`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `364`
     * - max amount: `3640975`
     * */
    @SerialName("QAR") QAR("QAR", "Qatari Riyal", "QAR", "ر.ق.‏", ",", '.', true, true, 2, 364, 3640975),

    /**
     * Romanian Leu
     *
     * Format:
     * - native: `RON`
     * - thousands separator: `.`
     * - decimal separator: `,`
     * - symbol left: `false`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `419`
     * - max amount: `4199702`
     * */
    @SerialName("RON") RON("RON", "Romanian Leu", "RON", "RON", ".", ',', false, true, 2, 419, 4199702),

    /**
     * Serbian Dinar
     *
     * Format:
     * - native: `дин.`
     * - thousands separator: `.`
     * - decimal separator: `,`
     * - symbol left: `false`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `9948`
     * - max amount: `99481357`
     * */
    @SerialName("RSD") RSD("RSD", "Serbian Dinar", "RSD", "дин.", ".", ',', false, true, 2, 9948, 99481357),

    /**
     * Russian Ruble
     *
     * Format:
     * - native: `руб.`
     * - thousands separator: ` `
     * - decimal separator: `,`
     * - symbol left: `false`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `7244`
     * - max amount: `72446503`
     * */
    @SerialName("RUB") RUB("RUB", "Russian Ruble", "RUB", "руб.", " ", ',', false, true, 2, 7244, 72446503),

    /**
     * Saudi Riyal
     *
     * Format:
     * - native: `ر.س.‏`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `375`
     * - max amount: `3750410`
     * */
    @SerialName("SAR") SAR("SAR", "Saudi Riyal", "SAR", "ر.س.‏", ",", '.', true, true, 2, 375, 3750410),

    /**
     * Swedish Krona
     *
     * Format:
     * - native: `kr`
     * - thousands separator: `.`
     * - decimal separator: `,`
     * - symbol left: `false`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `860`
     * - max amount: `8608360`
     * */
    @SerialName("SEK") SEK("SEK", "Swedish Krona", "SEK", "kr", ".", ',', false, true, 2, 860, 8608360),

    /**
     * Singapore Dollar
     *
     * Format:
     * - native: `$`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `false`
     * - exponent: `2`
     * - min amount: `134`
     * - max amount: `1342580`
     * */
    @SerialName("SGD") SGD("SGD", "Singapore Dollar", "SGD", "$", ",", '.', true, false, 2, 134, 1342580),

    /**
     * Thai Baht
     *
     * Format:
     * - native: `฿`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `false`
     * - exponent: `2`
     * - min amount: `3297`
     * - max amount: `32976498`
     * */
    @SerialName("THB") THB("THB", "Thai Baht", "฿", "฿", ",", '.', true, false, 2, 3297, 32976498),

    /**
     * Tajikistani Somoni
     *
     * Format:
     * - native: `TJS`
     * - thousands separator: ` `
     * - decimal separator: `;`
     * - symbol left: `false`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `1134`
     * - max amount: `11343058`
     * */
    @SerialName("TJS") TJS("TJS", "Tajikistani Somoni", "TJS", "TJS", " ", ';', false, true, 2, 1134, 11343058),

    /**
     * Turkish Lira
     *
     * Format:
     * - native: `TL`
     * - thousands separator: `.`
     * - decimal separator: `,`
     * - symbol left: `false`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `844`
     * - max amount: `8444250`
     * */
    @SerialName("TRY") TRY("TRY", "Turkish Lira", "TRY", "TL", ".", ',', false, true, 2, 844, 8444250),

    /**
     * Trinidad and Tobago Dollar
     *
     * Format:
     * - native: `$`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `false`
     * - exponent: `2`
     * - min amount: `679`
     * - max amount: `6795496`
     * */
    @SerialName("TTD") TTD("TTD", "Trinidad and Tobago Dollar", "TTD", "$", ",", '.', true, false, 2, 679, 6795496),

    /**
     * New Taiwan Dollar
     *
     * Format:
     * - native: `NT$`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `false`
     * - exponent: `2`
     * - min amount: `2770`
     * - max amount: `27704501`
     * */
    @SerialName("TWD") TWD("TWD", "New Taiwan Dollar", "NT$", "NT$", ",", '.', true, false, 2, 2770, 27704501),

    /**
     * Tanzanian Shilling
     *
     * Format:
     * - native: `TSh`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `false`
     * - exponent: `2`
     * - min amount: `231899`
     * - max amount: `2318999792`
     * */
    @SerialName("TZS") TZS("TZS", "Tanzanian Shilling", "TZS", "TSh", ",", '.', true, false, 2, 231899, 2318999792),

    /**
     * Ukrainian Hryvnia
     *
     * Format:
     * - native: `₴`
     * - thousands separator: ` `
     * - decimal separator: `,`
     * - symbol left: `false`
     * - space between: `false`
     * - exponent: `2`
     * - min amount: `2666`
     * - max amount: `26661647`
     * */
    @SerialName("UAH") UAH("UAH", "Ukrainian Hryvnia", "UAH", "₴", " ", ',', false, false, 2, 2666, 26661647),

    /**
     * Ugandan Shilling
     *
     * Format:
     * - native: `USh`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `false`
     * - exponent: `0`
     * - min amount: `3532`
     * - max amount: `35325273`
     * */
    @SerialName("UGX") UGX("UGX", "Ugandan Shilling", "UGX", "USh", ",", '.', true, false, 0, 3532, 35325273),

    /**
     * United States Dollar
     *
     * Format:
     * - native: `$`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `false`
     * - exponent: `2`
     * - min amount: `100`
     * - max amount: `1000000`
     * */
    @SerialName("USD") USD("USD", "United States Dollar", "$", "$", ",", '.', true, false, 2, 100, 1000000),

    /**
     * Uruguayan Peso
     *
     * Format:
     * - native: `$`
     * - thousands separator: `.`
     * - decimal separator: `,`
     * - symbol left: `true`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `4278`
     * - max amount: `42785106`
     * */
    @SerialName("UYU") UYU("UYU", "Uruguayan Peso", "UYU", "$", ".", ',', true, true, 2, 4278, 42785106),

    /**
     * Uzbekistani Som
     *
     * Format:
     * - native: `UZS`
     * - thousands separator: ` `
     * - decimal separator: `,`
     * - symbol left: `false`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `1071361`
     * - max amount: `10713612481`
     * */
    @SerialName("UZS") UZS("UZS", "Uzbekistani Som", "UZS", "UZS", " ", ',', false, true, 2, 1071361, 10713612481),

    /**
     * Vietnamese Đồng
     *
     * Format:
     * - native: `₫`
     * - thousands separator: `.`
     * - decimal separator: `,`
     * - symbol left: `false`
     * - space between: `true`
     * - exponent: `0`
     * - min amount: `22756`
     * - max amount: `227565000`
     * */
    @SerialName("VND") VND("VND", "Vietnamese Đồng", "₫", "₫", ".", ',', false, true, 0, 22756, 227565000),

    /**
     * Yemeni Rial
     *
     * Format:
     * - native: `ر.ي.‏`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `25084`
     * - max amount: `250849446`
     * */
    @SerialName("YER") YER("YER", "Yemeni Rial", "YER", "ر.ي.‏", ",", '.', true, true, 2, 25084, 250849446),

    /**
     * South African Rand
     *
     * Format:
     * - native: `R`
     * - thousands separator: `,`
     * - decimal separator: `.`
     * - symbol left: `true`
     * - space between: `true`
     * - exponent: `2`
     * - min amount: `1448`
     * - max amount: `14484800`
     * */
    @SerialName("ZAR") ZAR("ZAR", "South African Rand", "ZAR", "R", ",", '.', true, true, 2, 1448, 14484800)

    ;

    /**
     * Converts a value from native currency to the smallest unit.
     * For example, for USD: `2.99` will converts to `299`.
     *
     * @param value The value in the native currency (integer)
     * @return The value in the smallest unit of the currency
     */
    fun ofNative(value: Int) = ofNative(BigDecimal(value))

    /**
     * Converts a value from native currency to the smallest unit.
     * For example, for USD: `2.99` will converts to `299`.
     *
     * @param value The value in the native currency (integer)
     * @return The value in the smallest unit of the currency
     */
    fun ofNative(value: Double) = ofNative(BigDecimal(value))

    /**
     * Converts a value from native currency to the smallest unit.
     * For example, for USD: `2.99` will converts to `299`.
     *
     * @param value The value in the native currency (integer)
     * @return The value in the smallest unit of the currency
     */
    fun ofNative(value: BigDecimal) = value.multiply(BigDecimal(10).pow(exp)).toInt()
}