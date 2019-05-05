package io.malykh.anton.screens.currency_rates

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import io.malykh.anton.base.ActivityBase
import io.malykh.anton.presentation.R
import kotlinx.android.synthetic.main.activity_currencies.*

internal class CurrencyRatesActivity: ActivityBase<CurrencyRatesViewModel>(R.layout.activity_currencies) {

    override val viewModel: CurrencyRatesViewModel by lazy{
        ViewModelProviders.of(this).get(CurrencyRatesViewModel::class.java)
    }

    private val adapter = CurrencyRatesAdapter().apply {
        setOnItemClickedListener{ viewModel.onCurrencySelected(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currencies.adapter = adapter
    }

    override fun onObserveData(viewModel: CurrencyRatesViewModel) {
        viewModel.getRatesLiveData().observe(
            this,
            Observer {
                it?.let { value ->
                    adapter.setItems(value)
                }
            }
        )
    }
}