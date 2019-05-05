package io.malykh.anton.screens.currency_rates

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.support.v7.util.DiffUtil
import io.malykh.anton.base.Diff
import io.malykh.anton.base.ViewModelBase
import io.malykh.anton.core.Core
import io.malykh.anton.core.data.entity.Currency
import io.malykh.anton.core.data.entity.CurrencyRate
import io.malykh.anton.data.CurrencyMapper
import kotlinx.coroutines.*
import java.text.DecimalFormat

internal class CurrencyRatesViewModel(application: Application): ViewModelBase(application) {

    private companion object {
        const val DELAY_GET_CURRENCY_RATES_MS: Long = 1 * 1000
    }

    private val ratesLiveData = MutableLiveData<Diff<CurrencyRateEntry>>()
    private val mapper: CurrencyMapper by lazy { CurrencyMapper((getApplication() as Application).resources) }
    private var currentEntryList = emptyList<CurrencyRateEntry>()

    private var baseCurrency = CurrencyRateEntry(mapper.map(CurrencyRate(Currency.EUR, 1f)), 1f)

    init {
        launch(Dispatchers.IO) {
            while (isActive) {
                getCurrencyRates()
                delay(DELAY_GET_CURRENCY_RATES_MS)
            }
        }
    }

    fun getRatesLiveData(): LiveData<Diff<CurrencyRateEntry>> = ratesLiveData

    fun onCurrencySelected(selectedEntry: CurrencyRateEntry) {
        baseCurrency = selectedEntry
    }

    private suspend fun getCurrencyRates() {
        val base = baseCurrency
        val response = Core.get().requests.getCurrencyRatesRequest(base.currencyExt.currencyRate.currency).execute()
        if (response.hasError() || response.getData() == null)
            return
        val newEntries = mutableListOf(baseCurrency)
        response.getData()!!.forEach {
            if (baseCurrency.currencyExt.currencyRate.currency != it.currency) {
                newEntries.add(
                    CurrencyRateEntry(
                        mapper.map(it),
                        String.format("%.2f", it.rate * baseCurrency.amount).toFloat()
                    )
                )
            }
        }
        val diff = Diff(currentEntryList, newEntries, CurrencyEntriesCallback())
        withContext(Dispatchers.Main) {
            ratesLiveData.value = diff
            currentEntryList = newEntries
        }
    }

    private class CurrencyEntriesCallback : DiffUtil.ItemCallback<CurrencyRateEntry>() {
        override fun areItemsTheSame(old: CurrencyRateEntry, new: CurrencyRateEntry): Boolean =
            old.currencyExt.currencyRate.currency === new.currencyExt.currencyRate.currency

        override fun areContentsTheSame(old: CurrencyRateEntry, new: CurrencyRateEntry): Boolean {
            return old.amount == new.amount
        }

        override fun getChangePayload(oldItem: CurrencyRateEntry, newItem: CurrencyRateEntry): Any? {
            return newItem.amount
        }
    }

}