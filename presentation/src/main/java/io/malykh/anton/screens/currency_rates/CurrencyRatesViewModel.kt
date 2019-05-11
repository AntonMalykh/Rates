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
import io.malykh.anton.screens.currency_rates.data.CurrencyMapper
import io.malykh.anton.screens.currency_rates.utils.asMoney
import kotlinx.coroutines.*

internal class CurrencyRatesViewModel(application: Application): ViewModelBase(application) {

    private companion object {
        const val DELAY_GET_CURRENCY_RATES_MS: Long = 1 * 1000
    }

    private val ratesLiveData = MutableLiveData<Diff<CurrencyRateEntry>>()
    private val mapper: CurrencyMapper by lazy { CurrencyMapper((getApplication() as Application).resources) }
    private var currentEntryList = emptyList<CurrencyRateEntry>()

    private var baseCurrency = CurrencyRateEntry(mapper.map(CurrencyRate(Currency.EUR, 1f)), 1f)

    private val entriesDiffCallback = CurrencyEntriesCallback()

    init {
        launch(Dispatchers.IO) {
            while (isActive) {
                getCurrencyRates()
                delay(DELAY_GET_CURRENCY_RATES_MS)
            }
        }
    }

    fun getRatesLiveData(): LiveData<Diff<CurrencyRateEntry>> = ratesLiveData

    fun isBaseEntry(entry: CurrencyRateEntry) = entry == baseCurrency

    fun onCurrencySelected(selectedEntry: CurrencyRateEntry) {
        baseCurrency = selectedEntry
    }

    fun onBaseCurrencyAmountChanged(newValue: Float) {
        baseCurrency = baseCurrency.withAmount(newValue)
        val newEntries = currentEntryList.fold(
            ArrayList<CurrencyRateEntry>(currentEntryList.size),
            {
                acc, currencyRateEntry ->
                acc.add(
                    currencyRateEntry.withAmount(
                        (currencyRateEntry.currencyExt.currencyRate.rate * newValue).asMoney()))
                acc
            }
        )
        ratesLiveData.value = Diff(currentEntryList, newEntries, entriesDiffCallback)
        currentEntryList = newEntries
    }

    private suspend fun getCurrencyRates() {
        val base = baseCurrency
        val response = Core.get().requests.getCurrencyRatesRequest(base.currencyExt.currencyRate.currency).execute()
        if (response.hasError() || response.getData() == null)
            return
        val newEntries = mutableListOf(baseCurrency)
        response.getData()!!.forEach {
            newEntries.add(
                CurrencyRateEntry(
                    mapper.map(it),
                    (it.rate * baseCurrency.amount).asMoney()
                )
            )
        }
        withContext(Dispatchers.Main) {
            val diff = Diff(currentEntryList, newEntries, entriesDiffCallback)
            ratesLiveData.value = diff
            currentEntryList = newEntries
        }
    }

    private inner class CurrencyEntriesCallback : DiffUtil.ItemCallback<CurrencyRateEntry>() {
        override fun areItemsTheSame(old: CurrencyRateEntry, new: CurrencyRateEntry): Boolean =
            old.currencyExt.currencyRate.currency == new.currencyExt.currencyRate.currency

        override fun areContentsTheSame(old: CurrencyRateEntry, new: CurrencyRateEntry): Boolean {
            if (old.currencyExt.currencyRate.currency == new.currencyExt.currencyRate.currency
                && old.currencyExt.currencyRate.currency == baseCurrency.currencyExt.currencyRate.currency)
                return true
            return old.amount == new.amount
        }

        override fun getChangePayload(oldItem: CurrencyRateEntry, newItem: CurrencyRateEntry): Any? {
            return newItem.amount
        }
    }

    private fun CurrencyRateEntry.withAmount(amount: Float): CurrencyRateEntry =
        CurrencyRateEntry(this.currencyExt, amount)
}