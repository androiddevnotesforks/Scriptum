package sgtmelon.scriptum.infrastructure.adapter.callback

import sgtmelon.scriptum.cleanup.presentation.control.touch.EdgeDragTouchHelper

/**
 * Interface for control cards drag inside [EdgeDragTouchHelper]. It helps to know
 * which view can be dragged and which can't.
 */
interface ItemDragListener {

    fun setDrag(isDragAvailable: Boolean)
}