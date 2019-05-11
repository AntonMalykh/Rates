package io.malykh.anton.screens.currency_rates

import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import io.malykh.anton.base.AdapterBase
import io.malykh.anton.base.ViewHolderBase
import io.malykh.anton.presentation.R

internal class CurrencyRatesAdapter(onItemClickListener: ((CurrencyRateEntry) -> Unit)? = null,
                                    private val onInputClickListener: ((EditText, CurrencyRateEntry) -> Unit)? = null)
    : AdapterBase<CurrencyRateEntry>(onItemClickListener) {

    override fun onCreateViewHolder(viewParent: ViewGroup, viewType: Int): ViewHolderBase<CurrencyRateEntry> {
        return CurrencyRateViewHolder(
            viewParent,
            itemClickListener,
            onInputClickListener)
    }

    private class CurrencyRateViewHolder(parent: ViewGroup,
                                         val onItemClickListener: ((CurrencyRateEntry) -> Unit)? = null,
                                         val onInputClickListener: ((EditText, CurrencyRateEntry) -> Unit)? = null)
        : ViewHolderBase<CurrencyRateEntry>(R.layout.item_currencies, parent) {

        val icon: ImageView = itemView.findViewById(R.id.icon)
        val name: TextView = itemView.findViewById(R.id.name)
        val description: TextView = itemView.findViewById(R.id.description)
        val value: EditText = itemView.findViewById(R.id.value)

        init {
            itemView.setOnClickListener { onItemClickListener?.invoke(data) }
            value.setOnClickListener { onInputClickListener?.invoke(value, data) }
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
            value.apply{
                setText(if (amount == 0f) null else amount.toString())
                isFocusable = false
                isFocusableInTouchMode = false
            }
        }
    }
}