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

    private var drag = false

    override fun setDrag(drag: Boolean) {
        this.drag = drag
    }

    private var dragFrom = RecyclerView.NO_POSITION

    override fun getMovementFlags(recyclerView: RecyclerView,
                                  viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = getDrag(isEnabled = callback.onTouchGetDrag() && drag)
        val swipeFlags = getSwipe(isEnabled = callback.onTouchGetSwipe())

        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

        if (dragFrom != RecyclerView.NO_POSITION) return

        dragFrom = if (actionState.isDrag()) {
            movePosition
        } else {
            RecyclerView.NO_POSITION
        }
    }

    override fun clearView(recyclerView: RecyclerView,
                           viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        val dragTo = viewHolder.adapterPosition
        if (dragFrom == RecyclerView.NO_POSITION
                || dragTo == RecyclerView.NO_POSITION
                || dragFrom == dragTo) return

        callback.onTouchMoveResult(dragFrom, dragTo)

        dragFrom = RecyclerView.NO_POSITION
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
        callback.onTouchSwiped(viewHolder.adapterPosition)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder) : Boolean {
        super.onMove(recyclerView, viewHolder, target)

        return callback.onTouchMove(viewHolder.adapterPosition, target.adapterPosition)
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView,
                             viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
                             actionState: Int, isCurrentlyActive: Boolean) {
        if (actionState.isSwipe()) {
            /**
             * End position where alpha equal 0
             */
            val targetX = viewHolder.itemView.width.toFloat() / 2

            /**
             * Shift between start and end position
             */
            val translationX = abs(if (dX > 0) min(dX, targetX) else max(dX, -targetX))

            val alpha = (ALPHA_SWIPE_MAX - translationX / targetX)
            viewHolder.itemView.alpha = max(alpha, ALPHA_SWIPE_MIN)
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    interface Callback : ParentCallback {
        /**
         * Calls when user start make drag/swipe, inside [getMovementFlags].
         * Need for check permission for drag/swipe.
         *
         * @return true if user can drag/swipe cards.
         */
        fun onTouchGetDrag(): Boolean
        fun onTouchGetSwipe(): Boolean

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