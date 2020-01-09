package sgtmelon.scriptum.control.touch

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * Class with custom [onChildDraw], which prevent item dragging outside of recyclerView
 */
abstract class EdgeDragTouchHelper : ItemTouchHelper.Callback() {

    /**
     * Variable need for best performance and more productive, because
     * get [RecyclerView.ViewHolder.getAdapterPosition] inside [onChildDraw]
     * is hard calculating operation
     */
    protected var movePosition = RecyclerView.NO_POSITION

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

        /**
         * Get position on drag start
         */
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            movePosition = viewHolder?.adapterPosition ?: RecyclerView.NO_POSITION

            /**
             * Change alpha on drag.
             */
            viewHolder?.itemView?.animate()?.alpha(ALPHA_DRAG_MIN)?.duration = ALPHA_DURATION
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        /**
         * Clear alpha it was changed in drag or swipe.
         */
        viewHolder.itemView.animate().alpha(ALPHA_DRAG_MAX).duration = ALPHA_DURATION

        /**
         * Clear position on drag end.
         */
        movePosition = RecyclerView.NO_POSITION
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder): Boolean {
        /**
         * Get position on drag
         */
        movePosition = target.adapterPosition
        return false
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView,
                             viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
                             actionState: Int, isCurrentlyActive: Boolean) {
        var edgeY = dY

        if(actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            val view = viewHolder.itemView
            val pieceHeight = view.height / PIECE_RATIO

            val topEdge = recyclerView.top - pieceHeight
            val bottomEdge = recyclerView.height - view.bottom + pieceHeight

            val lastPosition = (recyclerView.adapter?.itemCount ?: 0) - 1

            if (movePosition == 0 && dY < topEdge) {
                edgeY = topEdge
            } else if (movePosition == lastPosition && dY > bottomEdge) {
                edgeY = bottomEdge
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, edgeY, actionState, isCurrentlyActive)
    }

    companion object {
        private const val PIECE_RATIO = 1.5f

        private const val ALPHA_DURATION = 200L
        private const val ALPHA_DRAG_MIN = 0.7f
        private const val ALPHA_DRAG_MAX = 1f
    }

}