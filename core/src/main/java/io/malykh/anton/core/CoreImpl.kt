package io.malykh.anton.core

import io.malykh.anton.core.data.entity.Currency
import io.malykh.anton.core.data.entity.CurrencyRate
import io.malykh.anton.core.domain.RequestsImpl
import io.malykh.anton.core.data.repositories.RepositoryFactory
import io.malykh.anton.core.domain.request.Request

/**
 * Base class which should be used for interacting with the core library
 */
public class CoreImpl: Core {

    /**
     * Provides [Requests] instance for making requestsImpl to the core library
     */
    private val requestsImpl: Requests = RequestsImpl(RepositoryFactory())

    override fun getRequests(): Requests {
        return requestsImpl
    }
}