package sgtmelon.scriptum.cleanup.presentation.control.touch

import android.graphics.Canvas
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.infrastructure.adapter.animation.TouchAnimation
import sgtmelon.scriptum.infrastructure.adapter.touch.SkipElevationTouchHelper

/**
 * Class with custom [onChildDraw], which prevent item dragging outside of recyclerView.
 * And also with custom drag/clear animation.
 */
abstract class EdgeDragTouchHelper(
    private val callback: ParentCallback
) : SkipElevationTouchHelper() {

    private val animation = TouchAnimation()

    /**
     * Variable need for best performance and more productivity, because
     * get [RecyclerView.ViewHolder.getAdapterPosition] inside [onChildDraw] is hard calculating
     * operation.
     */
    protected var movePosition = RecyclerView.NO_POSITION

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

        if (actionState.isDrag() || actionState.isSwipe()) {
            callback.onTouchAction(inAction = true)
        }

        if (actionState.isDrag()) {
            movePosition = viewHolder?.adapterPosition ?: RecyclerView.NO_POSITION
            animation.onDrag(viewHolder)
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        callback.onTouchAction(inAction = false)

        /** Clear position on drag end. */
        movePosition = RecyclerView.NO_POSITION
        animation.onClear(viewHolder)
    }

    override fun onMove(
        recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        movePosition = target.adapterPosition
        return false
    }

    override fun onChildDraw(
        c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
    ) {
        var edgeY = dY

        if (actionState.isDrag()) {
            val view = viewHolder.itemView
            val pieceHeight = view.height / PIECE_RATIO

            val topEdge = recyclerView.top - pieceHeight
            val bottomEdge = recyclerView.height - view.bottom + pieceHeight

            val lastPosition = (recyclerView.adapter?.itemCount ?: 0) - 1

            edgeY = when {
                movePosition == 0 && dY < topEdge -> topEdge
                movePosition == lastPosition && dY > bottomEdge -> bottomEdge
                else -> dY
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, edgeY, actionState, isCurrentlyActive)
    }

    interface ParentCallback {
        /**
         * Calls when user start drag/swipe action with card. Also calls when user stop action.
         */
        fun onTouchAction(inAction: Boolean)
    }

    companion object {
        private const val PIECE_RATIO = 1.5f
    }
}