package io.malykh.anton.base

import android.support.v7.util.DiffUtil

/**
 * Used for applying [newItems] to a recyclerview.
 * [diffResult] is used for easily updating of view holders
 */
class Diff<Item>(oldItems: List<Item>,
                 val newItems: List<Item>,
                 itemCallback: DiffUtil.ItemCallback<Item>) {

    val diffResult: DiffUtil.DiffResult =
        DiffUtil.calculateDiff(
            object: DiffUtil.Callback(){
                override fun getOldListSize(): Int = oldItems.size
                override fun getNewListSize(): Int = newItems.size
                override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean =
                    itemCallback.areItemsTheSame(oldItems[oldPosition], newItems[newPosition])
                override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean =
                    itemCallback.areContentsTheSame(oldItems[oldPosition], newItems[newPosition])
                override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? =
                    itemCallback.getChangePayload(oldItems[oldItemPosition], newItems[newItemPosition])
            },
            false
        )
}