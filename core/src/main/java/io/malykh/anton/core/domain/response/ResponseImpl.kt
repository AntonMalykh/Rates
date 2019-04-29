package io.malykh.anton.core.domain.response

internal class ResponseImpl<T>(private val dataImpl: T? = null,
                               private val errorImpl: CoreError? = null)
    : Response<T> {

    override fun getData(): T?  = dataImpl
    override fun getError(): CoreError? = errorImpl
}