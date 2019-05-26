package io.malykh.anton.core

import io.malykh.anton.core.data.entity.Currency
import io.malykh.anton.core.data.entity.CurrencyRate
import io.malykh.anton.core.domain.request.Request

/**
 * Requests that are available to be made via [CoreImpl]
 */
public interface Requests {
    /**
     * Provides request that returns list of [CurrencyRate] bases on the given [baseCurrency]
     */
    fun getCurrencyRatesRequest(baseCurrency: Currency): Request<List<CurrencyRate>>
}