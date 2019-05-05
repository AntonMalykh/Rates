package io.malykh.anton.screens.currency_rates

import io.malykh.anton.data.CurrencyExt

internal data class CurrencyRateEntry(val currencyExt: CurrencyExt,
                                      val amount: Float)
