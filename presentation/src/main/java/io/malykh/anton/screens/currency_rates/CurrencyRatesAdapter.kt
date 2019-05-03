package io.malykh.anton.screens.currency_rates

import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import io.malykh.anton.base.AdapterBase
import io.malykh.anton.base.ViewHolderBase
import io.malykh.anton.presentation.R

internal class CurrencyRatesAdapter: AdapterBase<CurrencyRateEntry>() {

    override fun onCreateViewHolder(viewParent: ViewGroup, viewType: Int): ViewHolderBase<CurrencyRateEntry> {
        return CurrencyRateViewHolder(viewParent)
    }

    private class CurrencyRateViewHolder(parent: ViewGroup)
        : ViewHolderBase<CurrencyRateEntry>(R.layout.item_currencies, parent) {

        val icon: ImageView = itemView.findViewById(R.id.icon)
        val name: TextView = itemView.findViewById(R.id.name)
        val description: TextView = itemView.findViewById(R.id.description)
        val value: EditText = itemView.findViewById(R.id.value)

        override fun bind(item: CurrencyRateEntry) {
            super.bind(item)
            icon.setImageDrawable(data.currency.flag)
            name.text = data.currency.currency.name
            description.text = data.currency.localizedDescription
            value.setText(data.amount.toString())
        }
    }
}