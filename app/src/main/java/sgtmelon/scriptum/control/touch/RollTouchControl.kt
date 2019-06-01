package sgtmelon.scriptum.control.touch

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.office.intf.ItemListener
import sgtmelon.scriptum.screen.view.note.RollNoteFragment
import sgtmelon.scriptum.screen.vm.note.RollNoteViewModel

/**
 * Управление перетаскиванием для [RollNoteFragment], реализовывать в [RollNoteViewModel]
 */
class RollTouchControl(private val callback: Result) : ItemTouchHelper.Callback(),
        ItemListener.Drag {

    private var drag = false

    override fun setDrag(drag: Boolean) {
        this.drag = drag
    }

    private var dragFrom = RecyclerView.NO_POSITION

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) =
            callback.onResultTouchFlags(drag)

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

        if (dragFrom != RecyclerView.NO_POSITION) return

        dragFrom = if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            viewHolder?.adapterPosition ?: RecyclerView.NO_POSITION
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

        callback.onResultTouchClear(dragFrom, dragTo)

        dragFrom = RecyclerView.NO_POSITION
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) =
            callback.onResultTouchSwipe(viewHolder.adapterPosition)

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder) =
            callback.onResultTouchMove(viewHolder.adapterPosition, target.adapterPosition)

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView,
                             viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
                             actionState: Int, isCurrentlyActive: Boolean) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            /**
             * Конечная точка, где альфа = 0
             */
            val targetX = viewHolder.itemView.width.toFloat() / 2

            /**
             * Сдвиг, между начальной точкой и конечной
             */
            val translationX = Math.abs(
                    if (dX > 0) Math.min(dX, targetX) else Math.max(dX, -targetX)
            )

            val alpha = (1.0f - translationX / targetX).toDouble()
            viewHolder.itemView.alpha = Math.max(alpha, 0.2).toFloat()
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    interface Result {
        fun onResultTouchFlags(drag: Boolean): Int
        fun onResultTouchClear(dragFrom: Int, dragTo: Int)
        fun onResultTouchSwipe(p: Int)
        fun onResultTouchMove(from: Int, to: Int): Boolean
    }

}