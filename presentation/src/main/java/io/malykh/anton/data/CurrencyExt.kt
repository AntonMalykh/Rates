package io.malykh.anton.data

import android.graphics.drawable.Drawable
import io.malykh.anton.core.data.entity.CurrencyRate

internal data class CurrencyExt(val currencyRate: CurrencyRate,
                                val localizedDescription: String,
                                val flag: Drawable)