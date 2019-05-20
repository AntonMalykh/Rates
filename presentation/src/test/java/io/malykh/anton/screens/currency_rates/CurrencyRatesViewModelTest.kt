package io.malykh.anton.screens.currency_rates

import android.app.Application
import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.content.res.Resources
import android.graphics.drawable.BitmapDrawable
import io.malykh.anton.core.Requests
import io.malykh.anton.core.data.entity.Currency
import io.malykh.anton.core.data.entity.CurrencyRate
import io.malykh.anton.core.domain.request.Request
import io.malykh.anton.core.domain.response.CoreError
import io.malykh.anton.core.domain.response.Response
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executors

@ExperimentalCoroutinesApi
class CurrencyRatesViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mainThreadSurrogate = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    @Mock
    lateinit var requestsMock: Requests
    @Mock
    lateinit var applicationMock: Application

    val getCurrencyRatesResponse = listOf(CurrencyRate(Currency.AUD, 1f), CurrencyRate(Currency.USD, 1f))

    @Suppress("UNCHECKED_CAST")
    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)

        MockitoAnnotations.initMocks(this)

        val getCurrenciesRequestMock = mock(Request::class.java)
        runBlocking {
            `when`(getCurrenciesRequestMock.execute()).thenReturn(object : Response<List<CurrencyRate>> {
                override fun getData(): List<CurrencyRate>? {
                    return getCurrencyRatesResponse
                }

                override fun getError(): CoreError? = null

            })
        }
        for (cur in Currency.values()) {
            `when`(requestsMock.getCurrencyRatesRequest(cur)).thenReturn(getCurrenciesRequestMock as Request<List<CurrencyRate>>?)
        }

        val resourcesMock = mock(Resources::class.java)
        `when`(resourcesMock.getString(ArgumentMatchers.anyInt())).thenReturn("TEST")
        `when`(resourcesMock.getDrawable(ArgumentMatchers.anyInt(), ArgumentMatchers.any())).thenReturn(BitmapDrawable())
        `when`(applicationMock.resources).thenReturn(resourcesMock)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `base entry is always first`() {
        runBlocking {
            val job = launch {
                val model = CurrencyRatesViewModel(
                    applicationMock,
                    requestsMock
                )
                model.getRatesLiveData().observeForever {  }

                while(model.getRatesLiveData().value == null)
                    delay(500)
                assertTrue(model.isBaseEntry(model.getRatesLiveData().value!!.newItems[0]))
            }
            job.join()
        }
    }

    @Test
    fun `when new base currency selected then next result contains new entry as base`() {
        runBlocking {
            val model = CurrencyRatesViewModel(
                applicationMock,
                requestsMock
            )
            model.getRatesLiveData().observeForever { }

            val job = launch {
                while (model.getRatesLiveData().value == null)
                    delay(500)
                val previousData = model.getRatesLiveData().value
                val newBaseEntry = model.getRatesLiveData().value!!.newItems[1]
                model.onCurrencySelected(newBaseEntry)
                while (model.getRatesLiveData().value == previousData)
                    delay(500)
                assertTrue(
                    model.isBaseEntry(newBaseEntry)
                            && model.getRatesLiveData().value!!.newItems[0] == newBaseEntry)
            }
            job.join()
        }
    }

    @Test
    fun `when base currency amount changed all currencies amount recalculated`() {
        val model = CurrencyRatesViewModel(
            applicationMock,
            requestsMock
        )
        model.getRatesLiveData().observeForever {  }
        runBlocking {
            val job = launch {
                while(model.getRatesLiveData().value == null)
                    delay(500)
                model.coroutineContext.cancel()
            }
            job.join()
        }
        val newBaseAmount = 2.3f
        model.onBaseCurrencyAmountChanged(newBaseAmount)
        model.getRatesLiveData().observeForever {
            val baseCurrencyIsCorrectlyChanged =
                it!!.newItems[0].amount == it.newItems[0].currencyExt.currencyRate.rate * newBaseAmount
            val responseCurrencyIsCorrectlyChanged =
                it.newItems[1].amount == it.newItems[0].currencyExt.currencyRate.rate * newBaseAmount
            assertTrue(baseCurrencyIsCorrectlyChanged && responseCurrencyIsCorrectlyChanged)
        }
    }
}
