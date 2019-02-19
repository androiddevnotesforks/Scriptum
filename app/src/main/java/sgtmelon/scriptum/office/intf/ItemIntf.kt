package sgtmelon.scriptum.office.intf

import android.view.View

interface ItemIntf {

    interface ClickListener {
        fun onItemClick(view: View, p: Int)
    }

    interface LongClickListener {
        fun onItemLongClick(view: View, p: Int)
    }

    interface DragListener {
        fun setDrag(drag: Boolean)
    }

    interface RollWatcher {
        fun afterRollChanged(p: Int, text: String)
    }

}