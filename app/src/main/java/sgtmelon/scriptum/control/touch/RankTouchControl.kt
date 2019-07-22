package sgtmelon.scriptum.control.touch

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.listener.ItemListener
import sgtmelon.scriptum.screen.ui.main.RankFragment

/**
 * Управление перетаскиванием для [RankFragment]
 */
class RankTouchControl(private val callback: Result) : ItemTouchHelper.Callback(),
        ItemListener.Drag {

    private var drag = false

    override fun setDrag(drag: Boolean) {
        this.drag = drag
    }

    private var dragFrom: Int = RecyclerView.NO_POSITION

    override fun getMovementFlags(recyclerView: RecyclerView,
                                  viewHolder: RecyclerView.ViewHolder): Int {
        val flagsDrag = if (drag) ItemTouchHelper.UP or ItemTouchHelper.DOWN else 0
        val flagsSwipe = 0

        return makeMovementFlags(flagsDrag, flagsSwipe)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            dragFrom = viewHolder?.adapterPosition ?: RecyclerView.NO_POSITION
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        val dragTo = viewHolder.adapterPosition
        if (dragFrom != dragTo) {
            callback.onResultTouchClear(dragFrom, dragTo)
        }
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder) =
            callback.onResultTouchMove(viewHolder.adapterPosition, target.adapterPosition)

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

    interface Result {
        fun onResultTouchClear(dragFrom: Int, dragTo: Int)
        fun onResultTouchMove(from: Int, to: Int): Boolean
    }

}