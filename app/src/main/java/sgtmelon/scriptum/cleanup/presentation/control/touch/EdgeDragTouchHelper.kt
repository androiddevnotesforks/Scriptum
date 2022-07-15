package sgtmelon.scriptum.cleanup.presentation.control.touch

import android.animation.AnimatorSet
import android.graphics.Canvas
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.extension.getAlphaAnimator
import sgtmelon.scriptum.cleanup.extension.getElevationAnimator
import sgtmelon.scriptum.cleanup.extension.getScaleXAnimator
import sgtmelon.scriptum.cleanup.extension.getScaleYAnimator

/**
 * Class with custom [onChildDraw], which prevent item dragging outside of recyclerView
 */
abstract class EdgeDragTouchHelper(
    private val callback: ParentCallback
) : ItemTouchHelper.Callback() {

    /**
     * Variable need for best performance and more productive, because
     * get [RecyclerView.ViewHolder.getAdapterPosition] inside [onChildDraw]
     * is hard calculating operation.
     */
    protected var movePosition = RecyclerView.NO_POSITION

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

        if (actionState.isDrag() || actionState.isSwipe()) {
            callback.onTouchAction(inAction = true)
        }

        /**
         * Get position on drag start.
         */
        if (actionState.isDrag()) {
            movePosition = viewHolder?.adapterPosition ?: RecyclerView.NO_POSITION

            /**
             * Change alpha and elevation on drag.
             */
            (viewHolder?.itemView as? CardView)?.also {
                AnimatorSet().apply {
                    this.duration = ANIM_DURATION
                    this.interpolator = AccelerateDecelerateInterpolator()

                    playTogether(
                        getAlphaAnimator(it, ALPHA_DRAG_MIN),
                        getScaleXAnimator(it, SCALE_MAX),
                        getScaleYAnimator(it, SCALE_MAX),
                        getElevationAnimator(it, R.dimen.elevation_6dp)
                    )
                }.start()
            }
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        callback.onTouchAction(inAction = false)

        /**
         * Clear alpha and elevation, it was changed in drag or swipe.
         */
        (viewHolder.itemView as? CardView)?.also {
            AnimatorSet().apply {
                this.duration = ANIM_DURATION
                this.interpolator = AccelerateDecelerateInterpolator()

                playTogether(
                    getAlphaAnimator(it, ALPHA_DRAG_MAX),
                    getScaleXAnimator(it, SCALE_MIN),
                    getScaleYAnimator(it, SCALE_MIN),
                    getElevationAnimator(it, R.dimen.elevation_2dp)
                )
            }.start()
        }

        /**
         * Clear position on drag end.
         */
        movePosition = RecyclerView.NO_POSITION
    }

    override fun onMove(
        recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        /**
         * Get position on drag.
         */
        movePosition = target.adapterPosition
        return false
    }

    override fun onChildDraw(
        c: Canvas, recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean
    ) {
        var edgeY = dY

        if (actionState.isDrag()) {
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

    protected fun Int.isDrag() = this == ItemTouchHelper.ACTION_STATE_DRAG

    protected fun Int.isSwipe() = this == ItemTouchHelper.ACTION_STATE_SWIPE

    protected fun getDrag(isEnabled: Boolean) = if (isEnabled) FULL_DRAG else 0

    protected fun getSwipe(isEnabled: Boolean) = if (isEnabled) FULL_SWIPE else 0

    interface ParentCallback {
        /**
         * Calls when user start drag/swipe action with card. Also calls when user stop action.
         */
        fun onTouchAction(inAction: Boolean)
    }

    companion object {
        private const val PIECE_RATIO = 1.5f

        private const val ANIM_DURATION = 300L
        private const val ALPHA_DRAG_MIN = 0.7f
        private const val ALPHA_DRAG_MAX = 1f
        private const val SCALE_MIN = 1f
        private const val SCALE_MAX = 1.02f

        protected const val FULL_DRAG = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        protected const val FULL_SWIPE = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    }

}