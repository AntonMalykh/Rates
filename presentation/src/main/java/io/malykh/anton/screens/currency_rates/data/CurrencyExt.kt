package io.malykh.anton.screens.currency_rates.data

import android.graphics.drawable.Drawable
import io.malykh.anton.core.data.entity.CurrencyRate

/**
 * Extended [CurrencyRate] that has user friendly [localizedDescription] of the currency and
 * a flag icon [flag]
 */
data class CurrencyExt(val currencyRate: CurrencyRate,
                                val localizedDescription: String,
                                val flag: Drawable)