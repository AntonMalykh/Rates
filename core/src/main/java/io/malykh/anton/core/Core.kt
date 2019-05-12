package io.malykh.anton.core

import io.malykh.anton.core.domain.RequestsImpl
import io.malykh.anton.core.data.repositories.RepositoryFactory

public class Core private constructor(){

    public companion object{
        private var INSTANCE: Core? = null
        fun get(): Core{
            if (INSTANCE == null) {
                INSTANCE = Core()
            }
            return INSTANCE!!
        }
    }

    public val requests: Requests = RequestsImpl(RepositoryFactory())
}