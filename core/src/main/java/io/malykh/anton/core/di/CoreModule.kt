package io.malykh.anton.core.di

import dagger.Module
import dagger.Provides
import io.malykh.anton.core.Core
import io.malykh.anton.core.CoreImpl
import javax.inject.Singleton

@Module
class CoreModule {

    @Singleton
    @Provides
    fun provideCore(): Core {
        return CoreImpl()
    }
}
