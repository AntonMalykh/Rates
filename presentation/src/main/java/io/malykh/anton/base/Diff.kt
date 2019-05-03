package io.malykh.anton.base

import android.support.v7.util.DiffUtil

internal class Diff<Item>(oldItems: List<Item>,
                          val newItems: List<Item>,
                          itemCallback: DiffUtil.ItemCallback<Item>) {

    val diffResult: DiffUtil.DiffResult =
        DiffUtil.calculateDiff(object: DiffUtil.Callback(){
            override fun getOldListSize(): Int = oldItems.size
            override fun getNewListSize(): Int = newItems.size
            override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean =
                itemCallback.areItemsTheSame(oldItems[oldPosition], newItems[newPosition])
            override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean =
                itemCallback.areContentsTheSame(oldItems[oldPosition], newItems[newPosition])

        })
}