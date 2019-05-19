package io.malykh.anton.core.data.repositories

import io.malykh.anton.core.data.repositories.currency_rates.CurrencyRatesRepository
import io.malykh.anton.core.data.repositories.currency_rates.CurrencyRatesRepositoryImpl
import kotlin.reflect.KClass

/**
 * Factory for obtaining implementations of the [RepositoryBase]
 */
internal class RepositoryFactory {

    private val repoMap: MutableMap<KClass<out RepositoryBase>, in RepositoryBase> = mutableMapOf()

    /**
     * Provides an implementation of the given [repositoryType].
     */
    inline fun <reified T: RepositoryBase> get(repositoryType: KClass<T>): T {
        return when (repositoryType) {
            CurrencyRatesRepository::class -> {
                if (repoMap[repositoryType] == null) {
                    repoMap[repositoryType] = CurrencyRatesRepositoryImpl()
                }
                repoMap[repositoryType] as T
            }
            else -> throw Exception("UNSUPPORTED REPOSITORY CLASS")
        }
    }
}
