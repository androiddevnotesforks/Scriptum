package sgtmelon.scriptum.infrastructure.adapter.touch

import android.graphics.Canvas
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import sgtmelon.scriptum.infrastructure.adapter.touch.listener.ItemDragListener
import sgtmelon.scriptum.infrastructure.utils.extensions.ALPHA_MAX

/**
 * Common class for control drag/swipe actions with [RecyclerView] items.
 */
class DragAndSwipeTouchHelper(private val callback: Callback) : EdgeDragTouchHelper(callback),
    ItemDragListener {

    /**
     * Variable for control press section. If user use long press on view which don't uses
     * for drag it will be false. More information you can find inside usage links for [setDrag].
     */
    private var isDragAvailable = false

    override fun setDrag(isDragAvailable: Boolean) {
        this.isDragAvailable = isDragAvailable
    }

    private var dragFrom: Int = RecyclerView.NO_POSITION

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = getDrag(isEnabled = callback.onTouchGetDrag() && isDragAvailable)
        val swipeFlags = getSwipe(callback.onTouchGetSwipe())

        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

        if (dragFrom != RecyclerView.NO_POSITION) return

        if (actionState.isDrag()) {
            callback.onTouchMoveStarts()
        }

        dragFrom = if (actionState.isDrag()) {
            movePosition
        } else {
            RecyclerView.NO_POSITION
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        val position = viewHolder.adapterPosition

        if (position == RecyclerView.NO_POSITION) return

        if (dragFrom != RecyclerView.NO_POSITION && dragFrom != position) {
            callback.onTouchMoveResult(dragFrom, position)
            dragFrom = RecyclerView.NO_POSITION
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
        callback.onTouchSwiped(viewHolder.adapterPosition)

        /** Don't know why, but after swipe [clearView] not called. */
        callback.onTouchAction(inAction = false)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        super.onMove(recyclerView, viewHolder, target)

        return callback.onTouchMove(viewHolder.adapterPosition, target.adapterPosition)
    }

    override fun onChildDraw(
        c: Canvas, recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean
    ) {
        if (actionState.isSwipe()) {
            /** Corner position where alpha will be equal [ALPHA_MIN_SWIPE]. */
            val targetX = viewHolder.itemView.width.toFloat() / 2

            /** Position of x between -targetX and targetX. */
            val translationX = abs(if (dX > 0) min(dX, targetX) else max(dX, -targetX))

            /** Have values between 0.0 and 1.0. */
            val ratio = translationX / targetX

            val alpha = ALPHA_MAX - ratio
            val resultAlpha = max(alpha, ALPHA_MIN_SWIPE)

            viewHolder.itemView.alpha = resultAlpha
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    interface Callback : EdgeDragCallback {

        /**
         * Calls when user start make drag/swipe, inside [getMovementFlags].
         * Need for check permission for drag/swipe.
         *
         * @return true if user can drag/swipe cards.
         */
        fun onTouchGetDrag(): Boolean
        fun onTouchGetSwipe(): Boolean

        /**
         * Calls when user swipe card, inside [onSwiped].
         */
        fun onTouchSwiped(position: Int)

        /**
         * Calls when user only start drag item.
         */
        fun onTouchMoveStarts()

        /**
         * Calls when user hold card and move it between another cards, inside [onMove].
         * Need for update items positions and call adapter notifyItemMoved.
         *
         * @return true if user cad drag card to another position.
         */
        fun onTouchMove(from: Int, to: Int): Boolean

        /**
         * Calls only after user cancel hold need update positions, inside [clearView].
         * Need for description drag result.
         */
        fun onTouchMoveResult(from: Int, to: Int)
    }

    companion object {
        private const val ALPHA_MIN_SWIPE = 0.2f
    }
}