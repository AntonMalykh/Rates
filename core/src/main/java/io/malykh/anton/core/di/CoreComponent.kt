package io.malykh.anton.core.di

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [CoreModule::class]
)
interface CoreComponent {
}