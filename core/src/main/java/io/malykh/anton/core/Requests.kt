package io.malykh.anton.core

import io.malykh.anton.core.data.entity.Currency
import io.malykh.anton.core.data.entity.CurrencyRate
import io.malykh.anton.core.domain.request.Request

public interface Requests {
    fun getCurrencyRatesRequest(baseCurrency: Currency): Request<List<CurrencyRate>>
}