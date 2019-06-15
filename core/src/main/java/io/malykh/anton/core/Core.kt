package io.malykh.anton.core

import io.malykh.anton.core.di.CoreComponent

interface Core {
    fun getRequests(): Requests

    fun getComponent(): CoreComponent
}