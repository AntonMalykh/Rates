package io.malykh.anton.base

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import io.malykh.anton.core.Core
import io.malykh.anton.core.CoreImpl
import io.malykh.anton.screens.currency_rates.CurrencyRatesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
import java.lang.reflect.InvocationTargetException

class ViewModelFactoryImpl(private val application: Application): ViewModelProvider.AndroidViewModelFactory(application) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (AndroidViewModel::class.java.isAssignableFrom(modelClass)) {

            try {
                return when (modelClass){
                    CurrencyRatesViewModel::class.java ->
                        CurrencyRatesViewModel(application, CoreImpl().getRequests()) as T
                    else -> throw RuntimeException()
                }
            } catch (e: NoSuchMethodException) {
                throw RuntimeException("Cannot create an instance of $modelClass", e)
            } catch (e: IllegalAccessException) {
                throw RuntimeException("Cannot create an instance of $modelClass", e)
            } catch (e: InstantiationException) {
                throw RuntimeException("Cannot create an instance of $modelClass", e)
            } catch (e: InvocationTargetException) {
                throw RuntimeException("Cannot create an instance of $modelClass", e)
            }

        }
        return super.create(modelClass)
    }

}