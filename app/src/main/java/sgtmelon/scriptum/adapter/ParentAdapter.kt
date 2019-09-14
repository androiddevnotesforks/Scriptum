package sgtmelon.scriptum.adapter

import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * Parent abstract class for adapters with common functions
 *
 * @param <E>  - list model
 * @param <VH> - holder for model
 */
abstract class ParentAdapter<T, VH : RecyclerView.ViewHolder>() : RecyclerView.Adapter<VH>() {

    // TODO сделать list NotMutable

    protected val list: MutableList<T> = ArrayList()

    @CallSuper open fun setList(list: List<T>) {
        this.list.clear()
        this.list.addAll(list)
    }

    @CallSuper open fun setListItem(p: Int, item: T) {
        list[p] = item
    }

    override fun getItemCount() = list.size

    fun notifyDataSetChanged(list: MutableList<T>) {
        setList(list)
        notifyDataSetChanged()
    }

    fun notifyItemChanged(p: Int, list: MutableList<T>) {
        setList(list)
        notifyItemChanged(p)
    }

    fun notifyItemChanged(item: T, p: Int) {
        setListItem(p, item)
        notifyItemChanged(p)
    }

    fun notifyItemRemoved(p: Int, list: MutableList<T>) {
        setList(list)
        notifyItemRemoved(p)
    }

    fun notifyItemInserted(p: Int, list: MutableList<T>) {
        setList(list)
        notifyItemInserted(p)
    }

    fun notifyItemMoved(from: Int, to: Int, list: MutableList<T>) {
        setList(list)
        notifyItemMoved(from, to)
    }

}