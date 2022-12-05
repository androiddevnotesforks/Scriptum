package sgtmelon.scriptum.infrastructure.adapter.diff

import androidx.recyclerview.widget.DiffUtil
import sgtmelon.scriptum.infrastructure.utils.extensions.clearAdd

abstract class ParentDiff<T> : DiffUtil.Callback() {

    private val oldList: MutableList<T> = ArrayList()
    private val newList: MutableList<T> = ArrayList()

    fun setList(oldList: List<T>, newList: List<T>) {
        this.oldList.clearAdd(oldList)
        this.newList.clearAdd(newList)
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areItemsTheSame(oldList[oldItemPosition], newList[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    abstract fun areItemsTheSame(oldItem: T, newItem: T): Boolean

}