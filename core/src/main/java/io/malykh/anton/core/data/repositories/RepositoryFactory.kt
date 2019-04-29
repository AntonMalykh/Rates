package io.malykh.anton.core.data.repositories

import io.malykh.anton.core.data.repositories.currency_rates.CurrencyRatesRepository
import io.malykh.anton.core.data.repositories.currency_rates.CurrencyRatesRepositoryImpl
import kotlin.reflect.KClass

internal class RepositoryFactory {

    private val repoMap: MutableMap<KClass<out RepositoryBase>, in RepositoryBase> = mutableMapOf()

    inline fun <reified T: RepositoryBase> get(type: KClass<T>): T {
        return when (type) {
            CurrencyRatesRepository::class -> {
                if (repoMap[type] == null) {
                    repoMap[type] = CurrencyRatesRepositoryImpl()
                }
                repoMap[type] as T
            }
            else -> throw Exception("UNSUPPORTED REPOSITORY CLASS")
        }
    }
}
