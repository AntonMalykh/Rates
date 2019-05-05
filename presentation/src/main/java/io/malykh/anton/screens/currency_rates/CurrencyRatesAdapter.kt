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

    private inner class CurrencyRateViewHolder(parent: ViewGroup)
        : ViewHolderBase<CurrencyRateEntry>(R.layout.item_currencies, parent) {

        val icon: ImageView = itemView.findViewById(R.id.icon)
        val name: TextView = itemView.findViewById(R.id.name)
        val description: TextView = itemView.findViewById(R.id.description)
        val value: EditText = itemView.findViewById(R.id.value)

        init {
            itemView.setOnClickListener { itemClickListener?.invoke(data) }
        }

        override fun bind(item: CurrencyRateEntry) {
            super.bind(item)
            icon.setImageDrawable(data.currencyExt.flag)
            name.text = data.currencyExt.currencyRate.currency.name
            description.text = data.currencyExt.localizedDescription
            value.apply{
                setText(data.amount.toString())
                setOnClickListener {
                    if (adapterPosition == 0) {
                        isFocusable = true
                        isFocusableInTouchMode = true
                        (itemView.context as CurrencyRatesActivity).requestKeyboard(this)
                    }
                    else
                        itemClickListener?.invoke(data)
                }
                isFocusable = false
                isFocusableInTouchMode = false
            }
        }

        override fun bindPayloads(payloads: List<Any>?): Boolean {
            super.bindPayloads(payloads)
            if (payloads.isNullOrEmpty() || payloads[0] !is Float)
                return false
            value.setText(payloads[0].toString())
            return true
        }
    }
}