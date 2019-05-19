package io.malykh.anton.base

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Base [RecyclerView.ViewHolder] implementation
 */
abstract class ViewHolderBase<T: Any>(@LayoutRes layoutId: Int,
                                      parentView: ViewGroup):
    RecyclerView.ViewHolder(
        LayoutInflater
            .from(parentView.context)
            .inflate(layoutId, parentView, false)) {

    /**
     * beware: this value is valid only when [bind] has already been called
     */
    protected lateinit var data: T

    /**
     * Binds data to a view
     */
    open fun bind(item: T){
        this.data = item
    }

    /**
     * Binds data to a view allowing to apply [payloads]
     */
    fun bind(item: T, payloads: List<Any>?): Boolean {
        this.data = item
        return bindPayloads(payloads)
    }

    /**
     * Binds payloads.
     *
     * @return TRUE if payloads are successfully bound, no need to update the whole view.
     */
    protected open fun bindPayloads(payloads: List<Any>?): Boolean = false
}
