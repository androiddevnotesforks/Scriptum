package sgtmelon.scriptum.infrastructure.adapter.parent

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Parent class for viewHolder's.
 */
abstract class ParentHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    protected val context: Context get() = itemView.context

    /**
     * Sometimes [getAdapterPosition] returns [RecyclerView.NO_POSITION] (check func description).
     * This function will call [onSuccess] and return true only if position is correct.
     */
    protected inline fun checkPosition(onSuccess: (Int) -> Unit): Boolean {
        val position = adapterPosition

        if (position != RecyclerView.NO_POSITION) {
            onSuccess(position)
            return true
        }

        return true
    }
}