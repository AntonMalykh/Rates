package io.malykh.anton.core.data.repositories

import io.malykh.anton.core.data.repositories.currency_rates.CurrencyRatesRepository
import io.malykh.anton.core.data.repositories.currency_rates.CurrencyRatesRepositoryImpl
import org.junit.Assert.*
import org.junit.Test

class RepositoryFactoryTest{

    @Test
    fun get() {
        val factory = RepositoryFactory()
        val repoImpl = factory.get(CurrencyRatesRepository::class)
        assertTrue(repoImpl is CurrencyRatesRepositoryImpl)
    }
}