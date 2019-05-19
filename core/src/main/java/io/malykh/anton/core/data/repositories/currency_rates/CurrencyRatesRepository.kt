package io.malykh.anton.core.data.repositories.currency_rates

import io.malykh.anton.core.data.entity.Currency
import io.malykh.anton.core.data.entity.CurrencyRate
import io.malykh.anton.core.data.repositories.RepositoryBase
import io.malykh.anton.core.domain.response.Response

/**
 * Repository for obtaining currency rates
 */
internal interface CurrencyRatesRepository: RepositoryBase {
    /**
     * Provides currency rates list based on the given [baseCurrency]
     */
    suspend fun getCurrencyRates(baseCurrency: Currency): Response<List<CurrencyRate>>
}