package io.malykh.anton.core.domain.response

public interface Response<T> {
    fun getData(): T?
    fun getError(): CoreError?
    fun hasError() = getError() != null
}