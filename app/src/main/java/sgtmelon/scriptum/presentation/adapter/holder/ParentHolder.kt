package sgtmelon.scriptum.presentation.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Parent class for viewHolder's.
 */
abstract class ParentHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    protected fun checkNoPosition(func: () -> Unit): Boolean {
        if (adapterPosition == RecyclerView.NO_POSITION) return false

        func()
        return true
    }

}