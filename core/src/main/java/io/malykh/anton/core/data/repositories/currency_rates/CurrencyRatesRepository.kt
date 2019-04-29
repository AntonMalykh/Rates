package io.malykh.anton.core.data.repositories.currency_rates

import io.malykh.anton.core.data.entity.Currency
import io.malykh.anton.core.data.entity.CurrencyRate
import io.malykh.anton.core.data.repositories.RepositoryBase
import io.malykh.anton.core.domain.response.Response

internal interface CurrencyRatesRepository: RepositoryBase {
    suspend fun getCurrencyRates(baseCurrency: Currency): Response<List<CurrencyRate>>
}