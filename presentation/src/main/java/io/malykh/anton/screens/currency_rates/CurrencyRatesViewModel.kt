package io.malykh.anton.screens.currency_rates

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.support.v7.util.DiffUtil
import io.malykh.anton.base.Diff
import io.malykh.anton.base.ViewModelBase
import io.malykh.anton.core.Requests
import io.malykh.anton.core.data.entity.Currency
import io.malykh.anton.core.data.entity.CurrencyRate
import io.malykh.anton.screens.currency_rates.data.CurrencyExt
import io.malykh.anton.screens.currency_rates.data.CurrencyMapper
import io.malykh.anton.screens.currency_rates.utils.asMoney
import kotlinx.coroutines.*

/**
 * [ViewModelBase] that is used for rendering list of currencies.
 * @param requests is used for updating currencies.
 */
class CurrencyRatesViewModel(application: Application,
                                      private val requests: Requests)
    : ViewModelBase(application) {

    private companion object {
        const val DELAY_GET_CURRENCY_RATES_MS: Long = 1 * 1000
    }

    private val ratesLiveData = MutableLiveData<Diff<CurrencyRateEntry>>()
    private val mapper: CurrencyMapper by lazy { CurrencyMapper(application.resources) }
    private val entriesDiffCallback = CurrencyEntriesCallback()

    private var currentEntryList = emptyList<CurrencyRateEntry>()
    private var baseCurrency = CurrencyRateEntry(mapper.map(CurrencyRate(Currency.EUR, 1f)), 1f)
    private var requestedBaseCurrency: CurrencyRateEntry? = null
    private var baseCurrencyChangeTimeStamp = 0L

    init {
        launch {
            while (isActive) {
                getCurrencyRates()
                delay(DELAY_GET_CURRENCY_RATES_MS)
            }
        }
    }

    /**
     * Provides Live data that exposes updates of the list of currencies
     */
    fun getRatesLiveData(): LiveData<Diff<CurrencyRateEntry>> = ratesLiveData

    /**
     * Checks whether the given [entry] is currently the base one
     */
    fun isBaseEntry(entry: CurrencyRateEntry) =
        entry.currencyExt.currencyRate.currency == baseCurrency.currencyExt.currencyRate.currency

    /**
     * Callback to be invoked when used selected an entry from the list.
     */
    fun onCurrencySelected(selectedEntry: CurrencyRateEntry) {
        requestedBaseCurrency = selectedEntry
    }

    /**
     * Callback to be invoked when used changes the amount of the base currency
     */
    fun onBaseCurrencyAmountChanged(newValue: Float) {
        baseCurrency = baseCurrency.withAmount(newValue)
        baseCurrencyChangeTimeStamp = System.currentTimeMillis()
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
        val requestTimeStamp = System.currentTimeMillis()
        val base = requestedBaseCurrency ?: baseCurrency
        val response = requests.getCurrencyRatesRequest(base.currencyExt.currencyRate.currency).execute()
        if (response.hasError() || response.getData() == null)
            return
        withContext(Dispatchers.Main) {
            requestedBaseCurrency = null
            if (baseCurrencyChangeTimeStamp > requestTimeStamp)
                return@withContext
            if (baseCurrency != base)
                baseCurrency = base.withRate(1f)
            val newEntries = mutableListOf(baseCurrency)
            response.getData()!!.forEach {
                newEntries.add(
                    CurrencyRateEntry(
                        mapper.map(it),
                        (it.rate * baseCurrency.amount).asMoney()
                    )
                )
            }
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

    private fun CurrencyRateEntry.withRate(rate: Float): CurrencyRateEntry {
        return CurrencyRateEntry(
            CurrencyExt(
                CurrencyRate(
                    this.currencyExt.currencyRate.currency,
                    rate
                ),
                this.currencyExt.localizedDescription,
                this.currencyExt.flag
            ),
            this.amount
        )
    }

}
