package io.malykh.anton.data

import android.graphics.drawable.Drawable
import io.malykh.anton.core.data.entity.Currency

internal data class CurrencyExt(val currency: Currency,
                                val localizedDescription: String,
                                val flag: Drawable)