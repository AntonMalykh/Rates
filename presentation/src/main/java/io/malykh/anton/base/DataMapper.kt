package io.malykh.anton.base

/**
 * Maps one data to another
 */
interface DataMapper<T, U> {
    fun map(from: T): U
}