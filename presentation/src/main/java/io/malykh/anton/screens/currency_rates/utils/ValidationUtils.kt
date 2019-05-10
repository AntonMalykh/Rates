package io.malykh.anton.screens.currency_rates.utils


fun String.asMoneyInput(): String? {
    return when (val moneyValue = this.toFloatOrNull()) {
        null -> if (this.isEmpty()) null else this
        0f -> null
        else -> String.format("%.2f", moneyValue)
    }
}

fun Float.asMoney(): Float {
    if (this == 0f) return 0f
    return String.format("%.2f", this).toFloat()
}