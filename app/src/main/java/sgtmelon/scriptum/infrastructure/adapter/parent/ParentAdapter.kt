package sgtmelon.scriptum.infrastructure.adapter.parent

import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.infrastructure.utils.extensions.clearAdd

/**
 * Parent abstract class for simple adapters.
 */
abstract class ParentAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    private val list: MutableList<T> = ArrayList()

    fun getItem(position: Int): T? = list.getOrNull(position)

    fun setList(list: List<T>): RecyclerView.Adapter<VH> = apply { this.list.clearAdd(list) }

    override fun getItemCount() = list.size

}