package io.malykh.anton.screens.currency_rates

import android.view.ViewGroup
import android.widget.EditText
import io.malykh.anton.base.AdapterBase
import io.malykh.anton.base.ViewHolderBase

internal class CurrencyRatesAdapter(onItemClickListener: ((CurrencyRateEntry) -> Unit)? = null,
                                    private val onInputClickListener: ((EditText, CurrencyRateEntry) -> Unit)? = null)
    : AdapterBase<CurrencyRateEntry>(onItemClickListener) {

    override fun onCreateViewHolder(viewParent: ViewGroup, viewType: Int): ViewHolderBase<CurrencyRateEntry> {
        return CurrencyRateViewHolder(
            viewParent,
            itemClickListener,
            onInputClickListener
        )
    }
}