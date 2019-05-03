package io.malykh.anton.base

internal interface DataMapper<T, U> {
    fun map(from: T): U
}