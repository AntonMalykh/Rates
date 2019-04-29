package io.malykh.anton.core.data.repositories.currency_rates

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

internal interface CurrencyRatesApi {

    @GET("latest")
    fun getCurrencyRates(@Query("base") baseCurrency: String): Call<ResponseBody>
}