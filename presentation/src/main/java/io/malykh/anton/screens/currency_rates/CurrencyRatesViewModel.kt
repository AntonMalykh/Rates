package io.malykh.anton.screens.currency_rates

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.support.v7.util.DiffUtil
import io.malykh.anton.base.Diff
import io.malykh.anton.base.ViewModelBase
import io.malykh.anton.core.Core
import io.malykh.anton.core.data.entity.Currency
import io.malykh.anton.data.CurrencyMapper
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicReference

internal class CurrencyRatesViewModel(application: Application): ViewModelBase(application) {

    private companion object {
        const val DELAY_GET_CURRENCY_RATES_MS: Long = 1 * 60 * 1000
    }

    private val ratesLiveData = MutableLiveData<Diff<CurrencyRateEntry>>()
    private val mapper: CurrencyMapper by lazy { CurrencyMapper((getApplication() as Application).resources) }
    private val currentEntryList = emptyList<CurrencyRateEntry>()

    init {
        launch {
            while (isActive) {
                getCurrencyRates()
                delay(DELAY_GET_CURRENCY_RATES_MS)
            }
        }
    }

    fun getRatesLiveData(): LiveData<Diff<CurrencyRateEntry>> = ratesLiveData

    private suspend fun getCurrencyRates() {
        val response = Core.get().requests.getCurrencyRatesRequest(Currency.EUR).execute()
        if (response.hasError() || response.getData() == null)
            return
        val newEntries = mutableListOf<CurrencyRateEntry>()
        response.getData()!!.forEach {
            newEntries.add(
                CurrencyRateEntry(
                    mapper.map(it.currency),
                    it.rate * 1
                )
            )
        }
        withContext(Dispatchers.Main){
            ratesLiveData.value = Diff(currentEntryList, newEntries, CurrencyEntriesCallback())
        }
    }

    private class CurrencyEntriesCallback : DiffUtil.ItemCallback<CurrencyRateEntry>() {
        override fun areItemsTheSame(old: CurrencyRateEntry, new: CurrencyRateEntry): Boolean =
            old.currency === new.currency

        override fun areContentsTheSame(old: CurrencyRateEntry, new: CurrencyRateEntry): Boolean {
            return old.amount == new.amount
        }
    }

}