package sgtmelon.scriptum.cleanup.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.cleanup.extension.clearAdd

/**
 * Parent abstract class for adapters with common functions
 *
 * @param <T>   - list model
 * @param <VH>  - holder for model
 */
abstract class ParentAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    private val list: MutableList<T> = ArrayList()

    fun getItem(position: Int): T? = list.getOrNull(position)

    fun setList(list: List<T>): RecyclerView.Adapter<VH> = apply { this.list.clearAdd(list) }

    override fun getItemCount() = list.size

}