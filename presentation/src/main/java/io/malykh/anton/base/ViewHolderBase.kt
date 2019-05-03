package io.malykh.anton.base

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

internal abstract class ViewHolderBase<T: Any>(@LayoutRes layoutId: Int,
                                               parentView: ViewGroup):
    RecyclerView.ViewHolder(LayoutInflater.from(parentView.context).inflate(layoutId, parentView)) {

    /**
     * beware: this value is valid only when [bind] has already been called
     */
    protected lateinit var data: T

    open fun bind(item: T){
        this.data = item
    }
}