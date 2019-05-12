package io.malykh.anton.screens.currency_rates.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols


private val moneyFormat = DecimalFormat("0.00").apply {
    maximumFractionDigits = 2
    decimalFormatSymbols = DecimalFormatSymbols.getInstance()
}

fun String.asMoneyString(): String {
    return when (val moneyValue = this.asMoney()) {
        0f -> ""
        else -> moneyFormat.formatFloatLocalized(moneyValue)
    }
}

fun Float.asMoneyString(): String = moneyFormat.formatFloatLocalized(this)

fun Float.asMoney(): Float {
    if (this == 0f) return 0f
    return moneyFormat
        .formatFloatLocalized(this)
        .replace(moneyFormat.decimalFormatSymbols.decimalSeparator.toString(), ".")
        .toFloat()
}

fun String.asMoney(): Float {
    val withSeparator = this.replace(moneyFormat.decimalFormatSymbols.decimalSeparator.toString(), ".")
    return when (val moneyValue = withSeparator.toFloatOrNull()){
        null -> 0f
        else -> moneyValue
    }
}

private fun DecimalFormat.formatFloatLocalized(float: Float): String {
    this.decimalFormatSymbols = DecimalFormatSymbols.getInstance()
    return this.format(float)
}

internal class MoneyInputFormatter {

    private companion object {
        private val NUMBER_CHARS = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
    }

    private val acceptedChars: CharArray

    init {
        acceptedChars = NUMBER_CHARS.copyOf(NUMBER_CHARS.size + 1)
        acceptedChars[acceptedChars.size - 1] = moneyFormat.decimalFormatSymbols.decimalSeparator
    }

    fun formatMoneyInput(input: CharSequence, onCharSubstituted: (() -> Unit)? = null): String {

        if (input.isEmpty())
            return ""
        val sb = StringBuilder(input.length)
        var decimalSeparatorPosition = -1
        var ignoreZeros = input.length > 1
        loop@ for (i in 0 until input.length) {
            val c = input[i]
            if (!acceptedChars.contains(c))
                continue

            when{
                c == getDecimalSeparator() -> {
                    if (decimalSeparatorPosition == -1) {
                        if (sb.isEmpty()) {
                            sb.append(0)
                            onCharSubstituted?.invoke()
                        }
                        sb.append(c)
                        decimalSeparatorPosition = sb.lastIndex
                        ignoreZeros = false
                    }
                }
                decimalSeparatorPosition == -1 -> {
                    if (c == '0' && ignoreZeros)
                        continue@loop
                    sb.append(c)
                    ignoreZeros = false
                }
                else -> {
                    if (sb.lastIndex - decimalSeparatorPosition < 2) {
                        sb.append(c)
                    }
                }
            }
        }
        return sb.toString()
    }

    private fun getDecimalSeparator(): Char {
        return acceptedChars.let {
            if (it.last() != DecimalFormatSymbols.getInstance().decimalSeparator) {
                it[it.lastIndex] = DecimalFormatSymbols.getInstance().decimalSeparator
            }
            it[it.lastIndex]
        }
    }
}