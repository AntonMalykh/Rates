package io.malykh.anton.presentation

import android.content.Context
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

public class CurrencyRatesActivity: AppCompatActivity(), CoroutineScope {

    private companion object {
        const val SHOW_KEYBOARD_RETRY_DELAY_MS = 100L
    }

    override val coroutineContext: CoroutineContext = Dispatchers.Main + Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        observeData()
    }

    private fun observeData() {

    }

    private fun showKeyboardOn(view: View, onKeyboardShown: (() -> Unit)? = null) {
        launch {
            while(!ViewCompat.isLaidOut(view))
                delay(SHOW_KEYBOARD_RETRY_DELAY_MS)
            view.requestFocus()
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(view, 0)
            onKeyboardShown?.invoke()
        }
    }
}