package sgtmelon.scriptum.infrastructure.adapter.touch

import sgtmelon.scriptum.infrastructure.adapter.touch.EdgeDragTouchHelper

/**
 * Listener for control cards dragging inside [EdgeDragTouchHelper]. It helps to know
 * which view can be dragged and which can't.
 */
interface ItemDragListener {

    fun setDrag(isDragAvailable: Boolean)
}