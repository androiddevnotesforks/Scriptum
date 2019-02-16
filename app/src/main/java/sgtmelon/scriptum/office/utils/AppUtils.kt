package sgtmelon.scriptum.office.utils

import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.app.adapter.ParentAdapter

object AppUtils {

    fun <T> ParentAdapter<T, RecyclerView.ViewHolder>.notifyItemChanged(list: MutableList<T>, p: Int) {
        setList(list)
        notifyItemChanged(p)
    }

    fun <T> ParentAdapter<T, RecyclerView.ViewHolder>.notifyItemRemoved(list: MutableList<T>, p: Int) {
        setList(list)
        notifyItemRemoved(p)
    }

}