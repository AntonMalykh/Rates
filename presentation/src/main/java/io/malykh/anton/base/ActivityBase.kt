package io.malykh.anton.base

import android.arch.lifecycle.ViewModel
import android.content.Context
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.view.View
import android.view.inputmethod.InputMethodManager
import io.malykh.anton.presentation.R
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Base activity implementation.
 * Valid [layoutId] should be passed to be inflated as content view
 * [Model] is view model type that is used with an extender
 */
abstract class ActivityBase<Model: ViewModel>(private val layoutId: Int): AppCompatActivity(), CoroutineScope {

    private companion object {
        const val SHOW_KEYBOARD_RETRY_DELAY_MS = 100L
    }

    override val coroutineContext: CoroutineContext = Dispatchers.Main + Job()
    /**
     * [ViewModel] implementation
     */
    protected abstract val viewModel: Model

    private var recentContentHeight = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
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

    /**
     * Called when height of the view is changed.
     * For example when keyboard appears or disappears.
     */
    protected open fun onViewHeightChanged(changedBy: Int) {

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        onObserveData(viewModel)
    }

    /**
     * Here is the place where [viewModel] can be observed.
     */
    protected abstract fun onObserveData(viewModel: Model)

    /**
     * Shows the soft keyboard and focuses the [view].
     * [onKeyboardShown] is called when keyboard is completely shown
     */
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

    /**
     * Hides the soft keyboard
     */
    protected fun hideKeyboard() {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(findViewById<View>(android.R.id.content).windowToken, 0)
    }
}