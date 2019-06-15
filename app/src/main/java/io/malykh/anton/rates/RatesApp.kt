package io.malykh.anton.rates

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.malykh.anton.core.Core
import io.malykh.anton.rates.di.DaggerAppComponent
import javax.inject.Inject
import javax.inject.Singleton

class RatesApp : DaggerApplication(){

    override fun onCreate() {
        super.onCreate()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent
            .factory()
            .create(this)
    }
}