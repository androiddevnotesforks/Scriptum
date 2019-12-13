package sgtmelon.scriptum.adapter

import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.extension.clearAndAdd
import java.util.*

/**
 * Parent abstract class for adapters with common functions
 *
 * @param <E>  - list model
 * @param <VH> - holder for model
 */
abstract class ParentAdapter<T, VH : RecyclerView.ViewHolder>() : RecyclerView.Adapter<VH>() {

    protected val list: MutableList<T> = ArrayList()

    @CallSuper open fun setList(list: List<T>) {
        this.list.clearAndAdd(list)
    }

    override fun getItemCount() = list.size

    open fun notifyList(list: List<T>) {}

}