package io.malykh.anton.base

import android.support.v7.widget.RecyclerView


/**
 * Base abstract implementation.
 * This implementation can be changed only by applying [Diff] using [setItems]
 *
 * [T] is data items type.
 */
abstract class AdapterBase<T: Any> (protected val itemClickListener: ((T) -> Unit)? = null)
    : RecyclerView.Adapter<ViewHolderBase<T>>() {

    /**
     * Data items
     */
    protected var items = listOf<T>()

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolderBase<T>, position: Int) {
        holder.bind(items[position])
    }

    override fun onBindViewHolder(holder: ViewHolderBase<T>, position: Int, payloads: MutableList<Any>) {
        if (!holder.bind(items[position], payloads))
            super.onBindViewHolder(holder, position, payloads)
    }

    /**
     * Applies new data stored in [diff]
     */
    fun setItems(diff: Diff<T>) {
        items = diff.newItems
        diff.diffResult.dispatchUpdatesTo(this)
    }
}