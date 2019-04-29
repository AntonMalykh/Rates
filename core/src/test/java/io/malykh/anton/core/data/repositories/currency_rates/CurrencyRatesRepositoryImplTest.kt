package io.malykh.anton.core.data.repositories.currency_rates

import io.malykh.anton.core.data.entity.Currency
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

class CurrencyRatesRepositoryImplTest {

    @Test
    fun getCurrencyRates() {
        val repo = CurrencyRatesRepositoryImpl()
        val response = runBlocking {
            repo.getCurrencyRates(Currency.EUR)
        }
        assertTrue(!response.hasError())
    }
}