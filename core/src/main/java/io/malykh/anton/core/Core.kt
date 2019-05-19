package io.malykh.anton.core

import io.malykh.anton.core.domain.RequestsImpl
import io.malykh.anton.core.data.repositories.RepositoryFactory

/**
 * Base class which should be used for interacting with the core library
 */
public class Core private constructor(){

    public companion object{
        private var INSTANCE: Core? = null
        /**
         * Provides [Core] instance.
         */
        fun get(): Core{
            if (INSTANCE == null) {
                INSTANCE = Core()
            }
            return INSTANCE!!
        }
    }

    /**
     * Provides [Requests] instance for making requests to the core library
     */
    public val requests: Requests = RequestsImpl(RepositoryFactory())
}