package sgtmelon.scriptum.presentation.control.touch

import android.graphics.Canvas
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.presentation.listener.ItemListener
import sgtmelon.scriptum.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.presentation.screen.vm.impl.note.RollNoteViewModel
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * Control drag and swipe for [RollNoteFragment], setup in [RollNoteViewModel]
 */
class RollTouchControl(private val callback: Callback) : EdgeDragTouchHelper(callback),
    ItemListener.Drag {

    /**
     * Variable for control press section. If user use long press on view which don't uses
     * for drag it will be false. More information you can find inside usage links for [setDrag].
     */
    private var mayDrag = false

    override fun setDrag(mayDrag: Boolean) {
        this.mayDrag = mayDrag
    }

    private var dragFrom = RecyclerView.NO_POSITION

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = getDrag(callback.onTouchGetDrag(mayDrag))
        val swipeFlags = getSwipe(callback.onTouchGetSwipe())

        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

        if (actionState.isDrag()) {
            callback.onTouchDragStart()
        }

        if (dragFrom != RecyclerView.NO_POSITION) return

        dragFrom = if (actionState.isDrag()) {
            movePosition
        } else {
            RecyclerView.NO_POSITION
        }
    }

    override fun clearView(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ) {
        super.clearView(recyclerView, viewHolder)

        val position = viewHolder.adapterPosition

        if (position == RecyclerView.NO_POSITION) return

        callback.onTouchClear(position)

        if (dragFrom != RecyclerView.NO_POSITION && dragFrom != position) {
            callback.onTouchMoveResult(dragFrom, position)
            dragFrom = RecyclerView.NO_POSITION
        }
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
        callback.onTouchSwiped(viewHolder.adapterPosition)
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
            /**
             * Corner position where alpha will be equal [ALPHA_SWIPE_MIN].
             */
            val targetX = viewHolder.itemView.width.toFloat() / 2

            /**
             * Position of x between -targetX and targetX.
             */
            val translationX = abs(if (dX > 0) min(dX, targetX) else max(dX, -targetX))

            /**
             * Have values between 0.0 and 1.0.
             */
            val ratio = translationX / targetX

            val alpha = ALPHA_SWIPE_MAX - ratio
            val resultAlpha = max(alpha, ALPHA_SWIPE_MIN)

            viewHolder.itemView.alpha = resultAlpha
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    interface Callback : ParentCallback {
        /**
         * Calls when user start make drag/swipe, inside [getMovementFlags].
         * Need for check permission for drag/swipe.
         *
         * @return true if user can drag/swipe cards.
         *
         * Pass here [mayDrag] need for detect when to close keyboard. Otherwise keyboard will
         * be closed on long press inside rollEnter.
         */
        fun onTouchGetDrag(mayDrag: Boolean): Boolean
        fun onTouchGetSwipe(): Boolean

        /**
         * Calls when user only start drag item.
         */
        fun onTouchDragStart()

        /**
         * Calls when user swipe card, inside [onSwiped]
         */
        fun onTouchSwiped(p: Int)

        /**
         * Calls when user hold card and move it between another cards, inside [onMove].
         * Need for update items positions and call adapter notifyItemMoved.
         *
         * @return true if user cad drag card to another position.
         */
        fun onTouchMove(from: Int, to: Int): Boolean

        /**
         * Calls inside [clearView].
         *
         * Need update item for prevent lags in future. After change note mode list disappear for
         * a second if don't call [RecyclerView.Adapter.notifyItemChanged] inside [clearView].
         */
        fun onTouchClear(position: Int)

        /**
         * Calls only after user cancel hold need update positions, inside [clearView].
         * Need for description drag result.
         */
        fun onTouchMoveResult(from: Int, to: Int)
    }

    companion object {
        private const val ALPHA_SWIPE_MIN = 0.2f
        private const val ALPHA_SWIPE_MAX = 1.0f
    }

}