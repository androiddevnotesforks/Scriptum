package sgtmelon.scriptum.app.control.touch

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.office.intf.ItemListener

/**
 * Управление перетаскиванием для [RollNoteFragment]
 */
class RollTouchControl(private val callback: Result) : ItemTouchHelper.Callback(),
        ItemListener.DragListener {

    private var drag = false

    override fun setDrag(drag: Boolean) {
        this.drag = drag
    }

    private var dragFrom = RecyclerView.NO_POSITION

    override fun getMovementFlags(recyclerView: RecyclerView,
                                  viewHolder: RecyclerView.ViewHolder): Int {
        val isEdit = callback.getEditMode()

        // TODO
//        val isEdit = noteCallback.viewModel.noteState.isEdit

        val flagsDrag = when (isEdit && drag) {
            true -> ItemTouchHelper.UP or ItemTouchHelper.DOWN
            false -> 0
        }

        val flagsSwipe = when (isEdit) {
            true -> ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            false -> 0

        }

        return ItemTouchHelper.Callback.makeMovementFlags(flagsDrag, flagsSwipe)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

        if (dragFrom != RecyclerView.NO_POSITION) return

        dragFrom = when (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            true -> viewHolder?.adapterPosition ?: RecyclerView.NO_POSITION
            false -> RecyclerView.NO_POSITION
        }
    }

    override fun clearView(recyclerView: RecyclerView,
                           viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        val dragTo = viewHolder.adapterPosition

        if (dragFrom == RecyclerView.NO_POSITION
                || dragTo == RecyclerView.NO_POSITION
                || dragFrom == dragTo) return

        callback.onTouchClear(dragFrom, dragTo)

        dragFrom = RecyclerView.NO_POSITION
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) =
            callback.onTouchSwipe(viewHolder.adapterPosition)

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder): Boolean {
        callback.onTouchMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

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
            val translationX = when (dX > 0) {
                true -> Math.abs(Math.min(dX, targetX))
                false -> Math.abs(Math.max(dX, -targetX))
            }

            val alpha = (1.0f - translationX / targetX).toDouble()

            viewHolder.itemView.alpha = Math.max(alpha, 0.2).toFloat()
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    interface Result {
        fun getEditMode(): Boolean

        fun onTouchClear(dragFrom: Int, dragTo: Int)

        fun onTouchSwipe(p: Int)

        fun onTouchMove(from: Int, to: Int)
    }

}