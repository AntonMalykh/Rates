package io.malykh.anton.base

import android.arch.lifecycle.ViewModel
import android.content.Context
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import io.malykh.anton.presentation.R
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class ActivityBase<Model: ViewModel>(private val layoutId: Int): AppCompatActivity(), CoroutineScope {

    private companion object {
        const val SHOW_KEYBOARD_RETRY_DELAY_MS = 100L
    }

    override val coroutineContext: CoroutineContext = Dispatchers.Main + Job()
    protected abstract val viewModel: Model
    private var recentContentHeight = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        findViewById<View>(android.R.id.content).apply {
            viewTreeObserver.addOnGlobalLayoutListener {
                val changedHeight = recentContentHeight - height
                if (changedHeight != 0)
                    onViewHeightChanged(changedHeight)
                recentContentHeight = height
            }
        }
    }

    protected open fun onViewHeightChanged(changedBy: Int) {

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        onObserveData(viewModel)
    }

    protected abstract fun onObserveData(viewModel: Model)

    protected fun requestKeyboard(view: View, onKeyboardShown: (() -> Unit)? = null) {
        launch {
            while(!ViewCompat.isLaidOut(view))
                delay(SHOW_KEYBOARD_RETRY_DELAY_MS)
            view.requestFocusFromTouch()
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .showSoftInput(view, 0)
            onKeyboardShown?.invoke()
        }
    }

    protected fun hideKeyboard() {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(findViewById<View>(android.R.id.content).windowToken, 0)
    }
}