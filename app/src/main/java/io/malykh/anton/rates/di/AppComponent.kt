package io.malykh.anton.rates.di

import dagger.Component
import dagger.Provides
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import io.malykh.anton.rates.RatesApp
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class, // necessary if AndroidInjector is used
        AppModule::class
//        FeatureBindingModule::class
    ]
)
interface AppComponent: AndroidInjector<RatesApp> {

    @Component.Factory
    interface Factory: AndroidInjector.Factory<RatesApp>
}