package sgtmelon.scriptum.cleanup.presentation.listener

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Interface for different touches on view
 */
// TODO remove/rebuild
interface ItemListener {

    @Deprecated("Create unique interface for each viewHolder")
    interface Click {
        /**
         * Interface for control click on list item with position == [p].
         * It may be different [view] inside [RecyclerView.ViewHolder].
         */
        fun onItemClick(view: View, p: Int)

    }

    @Deprecated("Create unique interface for each viewHolder")
    interface LongClick {
        /**
         * Interface for control long click on list item with position == [p].
         * It may be different [view] inside [RecyclerView.ViewHolder].
         */
        fun onItemLongClick(view: View, p: Int)
    }

    interface ActionClick {
        /**
         * Interface for control click on list item with position == [p].
         * It may be different [view] inside [RecyclerView.ViewHolder].
         *
         * For example to change binding in [RecyclerView.ViewHolder] after click
         * need use [action].
         */
        fun onItemClick(view: View, p: Int, action: () -> Unit = {})
    }
}