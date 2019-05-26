package io.malykh.anton.rates.di

import android.content.Context
import dagger.Module
import dagger.Provides
import io.malykh.anton.core.Core
import io.malykh.anton.core.CoreImpl
import io.malykh.anton.rates.RatesApp
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    fun provideContext(application: RatesApp): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    fun provideCore(): Core {
        return CoreImpl()
    }
}