package io.malykh.anton.screens.currency_rates

import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import io.malykh.anton.base.AdapterBase
import io.malykh.anton.base.ViewHolderBase
import io.malykh.anton.presentation.R

internal class CurrencyRatesAdapter
    : AdapterBase<CurrencyRateEntry>() {

    var onInputFocusChangedListener: ((EditText, Boolean) -> Unit)? = null

    override fun onCreateViewHolder(viewParent: ViewGroup, viewType: Int): ViewHolderBase<CurrencyRateEntry> {
        return CurrencyRateViewHolder(
            viewParent,
            itemClickListener,
            onInputFocusChangedListener)
    }

    private class CurrencyRateViewHolder(parent: ViewGroup,
                                         val itemClickListener: ((CurrencyRateEntry) -> Unit)?,
                                         val onInputFocusChangedListener: ((EditText, Boolean) -> Unit)?)
        : ViewHolderBase<CurrencyRateEntry>(R.layout.item_currencies, parent) {

        val icon: ImageView = itemView.findViewById(R.id.icon)
        val name: TextView = itemView.findViewById(R.id.name)
        val description: TextView = itemView.findViewById(R.id.description)
        val value: EditText = itemView.findViewById(R.id.value)

        init {
            itemView.setOnClickListener {
                if (adapterPosition != 0)
                    itemClickListener?.invoke(data)
            }
            itemView.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) value.requestFocus() }
            value.setOnFocusChangeListener { _, hasFocus ->
                if (adapterPosition == 0) {
                    onInputFocusChangedListener?.invoke(value, hasFocus)
                }
            }
        }

        override fun bind(item: CurrencyRateEntry) {
            super.bind(item)
            icon.setImageDrawable(data.currencyExt.flag)
            name.text = data.currencyExt.currencyRate.currency.name
            description.text = data.currencyExt.localizedDescription
            applyAmount(data.amount)
        }

        override fun bindPayloads(payloads: List<Any>?): Boolean {
            super.bindPayloads(payloads)
            if (payloads.isNullOrEmpty() || payloads[0] !is Float)
                return false
            applyAmount(payloads[0] as Float)
            return true
        }

        private fun applyAmount(amount: Float) {
            value.setText(if (amount == 0f) null else amount.toString())
            value.apply{
                setOnClickListener {
                    if (adapterPosition == 0) {
                        isFocusable = true
                        isFocusableInTouchMode = true
                        requestFocus()
                    }
                    else
                        itemClickListener?.invoke(data)
                }
                isFocusable = false
                isFocusableInTouchMode = false
            }
        }
    }
}