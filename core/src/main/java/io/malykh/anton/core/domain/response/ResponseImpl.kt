package io.malykh.anton.core.domain.response

/**
 * Library implementation of [Response]
 * [dataImpl] response payload data
 * [errorImpl] error that was happened during processing of a [Request]
 */
internal class ResponseImpl<T>(private val dataImpl: T? = null,
                               private val errorImpl: CoreError? = null)
    : Response<T> {

    override fun getData(): T?  = dataImpl
    override fun getError(): CoreError? = errorImpl
}