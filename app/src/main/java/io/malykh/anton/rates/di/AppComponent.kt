package io.malykh.anton.rates.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.Provides
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import io.malykh.anton.core.di.CoreComponent
import io.malykh.anton.core.di.CoreModule
import io.malykh.anton.rates.RatesApp
import javax.inject.Singleton

@Component(
    modules = [
        AndroidSupportInjectionModule::class, // necessary if AndroidInjector is used
        AppModule::class,
        CoreModule::class,
        FeatureBindingModule::class
    ]
)
interface AppComponent: AndroidInjector<RatesApp> {

    @Component.Factory
    interface Factory: AndroidInjector.Factory<RatesApp>
}