package io.malykh.anton.screens.currency_rates

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.*
import android.view.View
import android.view.View.GONE
import android.widget.EditText
import io.malykh.anton.base.ActivityBase
import io.malykh.anton.base.Diff
import io.malykh.anton.presentation.R
import io.malykh.anton.screens.currency_rates.utils.asMoneyInput
import kotlinx.android.synthetic.main.activity_currencies.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DecimalFormatSymbols

internal class CurrencyRatesActivity : ActivityBase<CurrencyRatesViewModel>(R.layout.activity_currencies) {

    private companion object {
        const val STATE_POSTPONE_KEYBOARD = "STATE_POSTPONE_KEYBOARD"
        const val CHECK_SCROLL_STOPPED_DELAY_MS = 50L
    }

    override val viewModel: CurrencyRatesViewModel by lazy {
        ViewModelProviders.of(this).get(CurrencyRatesViewModel::class.java)
    }

    private val moneyInputWatcher by lazy { MoneyInputWatcher() }

    private var postponeKeyboardOnBaseCurrency = false

    private val adapter = CurrencyRatesAdapter(this::onCurrencyItemClicked, this::onCurrencyInputClicked)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currencies.adapter = adapter
        savedInstanceState?.let {
            postponeKeyboardOnBaseCurrency = it.getBoolean(STATE_POSTPONE_KEYBOARD)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean(STATE_POSTPONE_KEYBOARD, moneyInputWatcher.hasAttachedInput())
    }

    override fun onViewHeightChanged(changedBy: Int) {
        super.onViewHeightChanged(changedBy)
        if (changedBy < 0) {
            root.requestFocus()
        }
    }

    override fun onObserveData(viewModel: CurrencyRatesViewModel) {
        viewModel.getRatesLiveData().observe(
            this,
            Observer {
                it?.let { applyNewItems(it) }
            }
        )
    }

    private fun applyNewItems(newItems: Diff<CurrencyRateEntry>) {
        processing.visibility = GONE
        val atTop = (currencies.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() == 0
        val firstItemOffset = when {
            atTop -> {
                currencies.getChildAt(0).height - currencies.getChildAt(0).bottom
            }
            else -> 0
        }
        adapter.setItems(newItems)
        if (atTop || postponeKeyboardOnBaseCurrency) {
            (currencies.layoutManager as LinearLayoutManager)
                .scrollToPositionWithOffset(0, -firstItemOffset)
            var isScrollStart = true
            if (postponeKeyboardOnBaseCurrency) {
                launch {
                    while (isScrollStart || currencies.scrollState != RecyclerView.SCROLL_STATE_IDLE) {

                        isScrollStart = false
                        delay(CHECK_SCROLL_STOPPED_DELAY_MS)
                    }
                    val input = findViewById<EditText>(R.id.value)
                    input.callOnClick()
                    input.setSelection(input.length())
                }
                postponeKeyboardOnBaseCurrency = false
            }
        }
    }

    private fun onCurrencyInputClicked(input: EditText, entry: CurrencyRateEntry) {
        when {
            viewModel.isBaseEntry(entry) -> {
                moneyInputWatcher.attachInput(input)
            }
            else -> {
                if (moneyInputWatcher.hasAttachedInput()) {
                    moneyInputWatcher.detachInput()
                }
                postponeKeyboardOnBaseCurrency = true
                viewModel.onCurrencySelected(entry)
            }
        }
    }

    private fun onCurrencyItemClicked(entry: CurrencyRateEntry) {
        if (viewModel.isBaseEntry(entry)) {
            return
        }
        if (moneyInputWatcher.hasAttachedInput()) {
            moneyInputWatcher.detachInput()
            postponeKeyboardOnBaseCurrency = true
        }
        viewModel.onCurrencySelected(entry)
    }

    private inner class MoneyInputWatcher {

        private var moneyInput: EditText? = null

        private val focusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            when {
                hasFocus -> requestKeyboard(v)
                else -> {
                    hideKeyboard()
                    detachInput()
                }
            }
        }

        private val inputWatcher = object : TextWatcher {
            private var ignore = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (ignore) {
                    return
                }
                s?.let {
                    ignore = true
                    val newValue = s.toString().asMoneyInput()
                    moneyInput?.let {
                        val selection = when {
                            newValue != null && isDecimalSeparatorBeforeSelection(newValue, it.selectionEnd) -> {
                                it.selectionEnd - 1
                            }
                            else -> it.selectionEnd
                        }
                        it.setText(newValue)
                        it.setSelection(Math.min(selection, it.length()))
                        it.text.setSpan(selectionWatcher, 0, it.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                    }
                    val floatValue = if (newValue.isNullOrEmpty()) 0f else newValue.toFloat()
                    viewModel.onBaseCurrencyAmountChanged(floatValue)
                    ignore = false
                }
            }
        }

        private val selectionWatcher = object : SpanWatcher {
            override fun onSpanRemoved(text: Spannable?, what: Any?, start: Int, end: Int) {}
            override fun onSpanAdded(text: Spannable?, what: Any?, start: Int, end: Int) {
                onSpanChanged(text, what, -1, -1, start, end)
            }

            override fun onSpanChanged(text: Spannable?, what: Any?, ostart: Int, oend: Int, nstart: Int, nend: Int) {
                moneyInput?.let {
                    if (what != Selection.SELECTION_END
                        || it.selectionEnd != it.selectionStart
                    ) {

                        return
                    }

                    if (isDecimalSeparatorBeforeSelection(it.text.toString(), nstart)) {
                        it.setSelection(nstart - 1)
                    }
                }
            }

        }

        fun attachInput(input: EditText) {
            if (moneyInput == input)
                return
            detachInput()
            input.isFocusable = true
            input.isFocusableInTouchMode = true
            requestKeyboard(input) {
                input.onFocusChangeListener = focusChangeListener
            }
            input.addTextChangedListener(inputWatcher)
            input.text.setSpan(selectionWatcher, 0, input.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            moneyInput = input
        }

        fun detachInput() {
            moneyInput?.let {
                it.removeTextChangedListener(inputWatcher)
                it.text.removeSpan(selectionWatcher)
                it.clearFocus()
                it.isFocusable = false
                it.isFocusableInTouchMode = false
                it.onFocusChangeListener = null
            }
            moneyInput = null
        }

        fun hasAttachedInput() = moneyInput != null

        private fun isDecimalSeparatorBeforeSelection(inputText: CharSequence, position: Int): Boolean {
            return position > 0
                    && position < inputText.length
                    && inputText[position - 1] == DecimalFormatSymbols.getInstance().decimalSeparator
        }

    }
}