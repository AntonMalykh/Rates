package io.malykh.anton.core.data.repositories.currency_rates

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import io.malykh.anton.core.data.entity.Currency
import io.malykh.anton.core.data.entity.CurrencyRate
import io.malykh.anton.core.domain.response.CoreError.ResponseError
import io.malykh.anton.core.domain.response.Response
import io.malykh.anton.core.domain.response.ResponseImpl
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStreamReader
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal class CurrencyRatesRepositoryImpl:
    CurrencyRatesRepository{

    private companion object{
        const val BASE_URL = "https://revolut.duckdns.org/"
    }

    private val api: CurrencyRatesApi = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CurrencyRatesApi::class.java)

    override suspend fun getCurrencyRates(baseCurrency: Currency): Response<List<CurrencyRate>> {
        return suspendCoroutine {
            api.getCurrencyRates(baseCurrency.name).enqueue(
                object: Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        it.resume(ResponseImpl(errorImpl = ResponseError("Failed to execute request")))
                    }

                    override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                        when {
                            response.isSuccessful -> {
                                val jsonReader =
                                    Gson().newJsonReader(InputStreamReader(response.body()!!.byteStream()))
                                it.resume(ResponseImpl(dataImpl = readJson(jsonReader)))
                            }
                            else ->
                                it.resume(ResponseImpl(errorImpl = ResponseError("Request executed unsuccessfully")))
                        }
                    }
                }
            )
        }
    }

    private fun readJson(reader: JsonReader): List<CurrencyRate> {
        reader.use {
            if (!reader.hasNext())
                return emptyList()
            val rates = mutableListOf<CurrencyRate>()
            reader.beginObject()
            while(reader.nextName() != "rates")
                reader.skipValue()
            reader.beginObject()
            while (reader.peek() != JsonToken.END_OBJECT) {
                rates += CurrencyRate(
                    Currency.valueOf(reader.nextName()),
                    reader.nextDouble().toFloat()
                )
            }
            reader.endObject()
            return rates
        }
    }
}