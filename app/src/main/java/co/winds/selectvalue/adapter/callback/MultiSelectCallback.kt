package co.winds.selectvalue.adapter.callback

import android.support.v7.util.DiffUtil
import co.winds.selectvalue.model.Model

class MultiSelectCallback(private val oldList: List<Model>, private val newList: List<Model>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

        val (_, persoName) = oldList[oldItemPosition]
        val (_, persoName1) = newList[newItemPosition]

        return persoName == persoName1
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}
