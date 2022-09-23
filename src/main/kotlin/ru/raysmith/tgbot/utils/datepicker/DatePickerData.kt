package ru.raysmith.tgbot.utils.datepicker

data class DatePickerData(val additionalData: String?, val y: Int?, val m: Int?, val d: Int?, val yearPage: Int? = null) {
    companion object {
        private fun getValueFor(rawData: String, prefix: Char): Int? {
            return rawData.lastIndexOf(prefix)
                .let { if (it == -1) null else {
                        val substr = rawData.substring(it + 1)
                        if (substr.contains("[^\\\\]}".toRegex())) return null
                        else "[-\\d]+".toRegex().find(substr)?.value
                    }
                }?.toInt()
        }

        fun from(rawData: String): DatePickerData {
            return DatePickerData(
                rawData.lastIndexOf("}")
                    .let { if (it == -1) null else rawData.substring(rawData.lastIndexOf("{") + 1, it) }
                    ?.ifEmpty { null },
                getValueFor(rawData, 'y'),
                getValueFor(rawData, 'm'),
                getValueFor(rawData, 'd'),
                getValueFor(rawData, 'p'),
            )
        }
    }
}