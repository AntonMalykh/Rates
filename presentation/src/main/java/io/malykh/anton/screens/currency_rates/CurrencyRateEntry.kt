package io.malykh.anton.screens.currency_rates

import io.malykh.anton.screens.currency_rates.data.CurrencyExt

/**
 * Entry for rendering the list of currency rates
 */
internal data class CurrencyRateEntry(val currencyExt: CurrencyExt,
                                      val amount: Float)
