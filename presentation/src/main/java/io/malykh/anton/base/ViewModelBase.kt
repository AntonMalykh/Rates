package io.malykh.anton.base

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

abstract class ViewModelBase(application: Application): AndroidViewModel(application), CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.IO + Job()

    override fun onCleared() {
        super.onCleared()
        coroutineContext.cancel()
    }
}