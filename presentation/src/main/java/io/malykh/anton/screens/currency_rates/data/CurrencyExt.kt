package io.malykh.anton.screens.currency_rates.data

import android.graphics.drawable.Drawable
import io.malykh.anton.core.data.entity.CurrencyRate

internal data class CurrencyExt(val currencyRate: CurrencyRate,
                                val localizedDescription: String,
                                val flag: Drawable)