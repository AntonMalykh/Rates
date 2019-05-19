package io.malykh.anton.core.domain.request

import io.malykh.anton.core.data.repositories.RepositoryBase
import io.malykh.anton.core.domain.response.Response

/**
 * Base library [Request] implementation.
 */
internal abstract class RequestBase<T: RepositoryBase, U>(private val repository: T): Request<U> {

    /**
     * Processes the request using the given [repository]
     */
    internal abstract suspend fun run(repository: T): Response<U>

    override suspend fun execute(): Response<U> {
        return run(repository)
    }
}
