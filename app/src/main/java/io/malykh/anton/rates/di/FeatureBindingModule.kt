package io.malykh.anton.rates.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.malykh.anton.screens.currency_rates.CurrencyRatesActivity

@Module
internal interface FeatureBindingModule {

    @ContributesAndroidInjector(
        modules = []
    )
    fun ratesFeature(): CurrencyRatesActivity
}