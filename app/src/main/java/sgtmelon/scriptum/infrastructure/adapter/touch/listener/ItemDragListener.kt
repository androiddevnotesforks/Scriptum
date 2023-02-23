package sgtmelon.scriptum.infrastructure.adapter.touch.listener

/**
 * Listener for control cards dragging inside [EdgeDragTouchHelper]. It helps to know
 * which view can be dragged and which can't.
 */
interface ItemDragListener {

    fun setDrag(isDragAvailable: Boolean)
}