package sgtmelon.scriptum.presentation.listener

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.presentation.control.touch.EdgeDragTouchHelper

/**
 * Interface for different touches on view
 */
// TODO remove/rebuild
interface ItemListener {

    interface Click {
        /**
         * Interface for control click on list item with position == [p].
         * It may be different [view] inside [RecyclerView.ViewHolder].
         */
        fun onItemClick(view: View, p: Int)

    }

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

    interface Drag {
        /**
         * Interface for control cards drag inside [EdgeDragTouchHelper]. It helps to know
         * which view can be dragged and which can't.
         */
        fun setDrag(mayDrag: Boolean)
    }

}