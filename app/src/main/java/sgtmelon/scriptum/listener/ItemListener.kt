package sgtmelon.scriptum.listener

import android.view.View

/**
 * Interface for different touches on view
 */
interface ItemListener {

    interface Click {
        fun onItemClick(view: View, p: Int)
    }

    interface LongClick {
        fun onItemLongClick(view: View, p: Int)
    }

    interface Drag {
        fun setDrag(drag: Boolean)
    }

}