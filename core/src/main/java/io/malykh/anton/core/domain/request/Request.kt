package io.malykh.anton.core.domain.request

import io.malykh.anton.core.domain.response.Response

/**
 * Request that can be executed by the core library.
 */
public interface Request<U> {
    /**
     * Launches processing of the request that is always finishes with [Response] which
     * contains an error if any error occurred
     */
    suspend fun execute(): Response<U>
}