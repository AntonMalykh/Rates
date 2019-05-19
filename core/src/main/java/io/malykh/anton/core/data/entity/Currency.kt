package io.malykh.anton.core.data.entity

import io.malykh.anton.core.EnumMockHelper


public enum class Currency: EnumMockHelper<Currency> {
    EUR,
    AUD,
    BGN,
    BRL,
    CAD,
    CHF,
    CNY,
    CZK,
    DKK,
    GBP,
    HKD,
    HRK,
    HUF,
    IDR,
    ILS,
    INR,
    ISK,
    JPY,
    KRW,
    MXN,
    MYR,
    NOK,
    NZD,
    PHP,
    PLN,
    RON,
    RUB,
    SEK,
    SGD,
    THB,
    TRY,
    USD,
    ZAR;

    override fun get(): Currency {
        return this
    }
}

