package io.malykh.anton.core.domain

import io.malykh.anton.core.Requests
import io.malykh.anton.core.data.entity.Currency
import io.malykh.anton.core.data.entity.CurrencyRate
import io.malykh.anton.core.data.repositories.currency_rates.CurrencyRatesRepository
import io.malykh.anton.core.data.repositories.RepositoryFactory
import io.malykh.anton.core.domain.request.GetCurrencyRatesRequest
import io.malykh.anton.core.domain.request.Request

internal class RequestsImpl internal constructor (private val repositoryFactory: RepositoryFactory) : Requests {

    override fun getCurrencyRatesRequest(baseCurrency: Currency): Request<List<CurrencyRate>> {
        return GetCurrencyRatesRequest(
            repositoryFactory.get(CurrencyRatesRepository::class),
            baseCurrency)
    }
}