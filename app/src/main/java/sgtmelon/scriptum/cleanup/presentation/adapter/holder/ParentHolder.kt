package sgtmelon.scriptum.cleanup.presentation.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Parent class for viewHolder's.
 */
abstract class ParentHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    protected fun checkNoPosition(func: (Int) -> Unit): Boolean {
        val position = adapterPosition

        if (position== RecyclerView.NO_POSITION) return false

        func(position)
        return true
    }
}