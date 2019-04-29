package io.malykh.anton.core.domain.request

import io.malykh.anton.core.data.entity.Currency
import io.malykh.anton.core.data.entity.CurrencyRate
import io.malykh.anton.core.data.repositories.currency_rates.CurrencyRatesRepository
import io.malykh.anton.core.domain.response.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal class GetCurrencyRatesRequest(repository: CurrencyRatesRepository,
                                       private val baseCurrency: Currency
)
    : RequestBase<CurrencyRatesRepository, List<CurrencyRate>>(repository) {

    override suspend fun run(repository: CurrencyRatesRepository): Response<List<CurrencyRate>> {
        return withContext(Dispatchers.IO){
            repository.getCurrencyRates(baseCurrency)
        }
    }
}