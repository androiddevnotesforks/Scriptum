package sgtmelon.scriptum.infrastructure.adapter.touch

import android.graphics.Canvas
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.infrastructure.adapter.animation.TouchAnimation

/**
 * Class with custom [onChildDraw], which prevent item dragging outside of recyclerView.
 * And also with custom drag/clear animation.
 */
abstract class EdgeDragTouchHelper(
    private val callback: EdgeDragCallback
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

            val topEdge = recyclerView.top - view.height / TOP_EDGE_RATIO
            val bottomEdge = recyclerView.height - view.bottom + view.height / BOTTOM_EDGE_RATIO
            val lastPosition = (recyclerView.adapter?.itemCount ?: 0) - 1

            edgeY = when {
                movePosition == 0 && dY < topEdge -> topEdge
                movePosition == lastPosition && dY > bottomEdge -> bottomEdge
                else -> dY
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, edgeY, actionState, isCurrentlyActive)
    }

    interface EdgeDragCallback {

        /**
         * Calls when user starts/ends dragging/swiping item.
         */
        fun onTouchAction(inAction: Boolean)
    }

    companion object {
        private const val TOP_EDGE_RATIO = 1.7f
        private const val BOTTOM_EDGE_RATIO = 2.2f
    }
}