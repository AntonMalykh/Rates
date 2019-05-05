package io.malykh.anton.base

import android.support.v7.widget.RecyclerView

internal abstract class AdapterBase<T: Any>: RecyclerView.Adapter<ViewHolderBase<T>>() {

    protected var items = listOf<T>()
    protected var itemClickListener: ((T) -> Unit)? = null

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolderBase<T>, position: Int) {
        holder.bind(items[position])
    }

    override fun onBindViewHolder(holder: ViewHolderBase<T>, position: Int, payloads: MutableList<Any>) {
        if (!holder.bind(items[position], payloads))
            super.onBindViewHolder(holder, position, payloads)
    }

    fun setOnItemClickedListener(listener: (T) -> Unit) {
        itemClickListener = listener
    }

    fun setItems(diff: Diff<T>) {
        items = diff.newItems
        diff.diffResult.dispatchUpdatesTo(this)
    }
}