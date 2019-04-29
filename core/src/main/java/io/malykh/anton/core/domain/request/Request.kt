package io.malykh.anton.core.domain.request

import io.malykh.anton.core.domain.response.Response

public interface Request<U> {
    suspend fun execute(): Response<U>
}