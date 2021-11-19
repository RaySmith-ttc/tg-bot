package ru.raysmith.tgbot.utils

data class DatePickerData(val y: Int?, val m: Int?, val d: Int?, val yearPage: Int? = null) {
    companion object {
        fun from(rawData: String): DatePickerData {
            return DatePickerData(
                rawData.lastIndexOf("y")
                    .let { if (it == -1) null else "[-\\d]+".toRegex().find(rawData.substring(it + 1))!!.value }
                    ?.toInt(),
                rawData.lastIndexOf("m")
                    .let { if (it == -1) null else "[\\d]+".toRegex().find(rawData.substring(it + 1))!!.value }
                    ?.toInt(),
                rawData.lastIndexOf("d")
                    .let { if (it == -1) null else "[\\d]+".toRegex().find(rawData.substring(it + 1))!!.value }
                    ?.toInt(),
                rawData.lastIndexOf("p")
                    .let { if (it == -1) null else "[\\d]+".toRegex().find(rawData.substring(it + 1))!!.value }
                    ?.toInt(),
            )
        }
    }
}