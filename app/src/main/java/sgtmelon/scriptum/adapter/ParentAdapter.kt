package sgtmelon.scriptum.adapter

import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.extension.clearAndAdd
import java.util.*

/**
 * Parent abstract class for adapters with common functions
 *
 * @param <T>   - list model
 * @param <VH>  - holder for model
 */
abstract class ParentAdapter<T, VH : RecyclerView.ViewHolder> :
        RecyclerView.Adapter<VH>() {

    protected val list: MutableList<T> = ArrayList()

    fun setList(list: List<T>): ParentAdapter<T, VH> = apply {
        this.list.clearAndAdd(list)
    }

    override fun getItemCount() = list.size

}