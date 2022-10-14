package sgtmelon.scriptum.cleanup.presentation.adapter.holder

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Parent class for viewHolder's.
 */
abstract class ParentHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    protected val context: Context get() = itemView.context

    protected inline fun checkPosition(onSuccess: (Int) -> Unit): Boolean {
        val position = adapterPosition

        if (position != RecyclerView.NO_POSITION) {
            onSuccess(position)
            return true
        }

        return true
    }
}