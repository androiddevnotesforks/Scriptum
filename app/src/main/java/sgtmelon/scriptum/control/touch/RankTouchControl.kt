package sgtmelon.scriptum.control.touch

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.presentation.listener.ItemListener
import sgtmelon.scriptum.screen.ui.main.RankFragment
import sgtmelon.scriptum.screen.vm.main.RankViewModel

/**
 * Control drag for [RankFragment], setup in [RankViewModel]
 */
class RankTouchControl(private val callback: Callback) : EdgeDragTouchHelper(), ItemListener.Drag {

    private var drag = false

    override fun setDrag(drag: Boolean) {
        this.drag = drag
    }

    private var dragFrom: Int = RecyclerView.NO_POSITION

    override fun getMovementFlags(recyclerView: RecyclerView,
                                  viewHolder: RecyclerView.ViewHolder): Int {
        val isDrag = callback.onTouchDrag() && drag

        val flagsDrag = if (isDrag) ItemTouchHelper.UP or ItemTouchHelper.DOWN else 0
        val flagsSwipe = 0

        return makeMovementFlags(flagsDrag, flagsSwipe)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            dragFrom = movePosition
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        val dragTo = viewHolder.adapterPosition
        if (dragFrom != dragTo) {
            callback.onTouchMoveResult()
        }
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder): Boolean {
        super.onMove(recyclerView, viewHolder, target)

        return callback.onTouchMove(viewHolder.adapterPosition, movePosition)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

    interface Callback {
        /**
         * Calls inside [getMovementFlags]. Need for check permission for drag.
         *
         * @return true if user can drag cards.
         */
        fun onTouchDrag(): Boolean

        /**
         * Calls inside [onMove]. Need for check permission for move card to another position.
         *
         * @return true if user cad drag card to another position.
         */
        fun onTouchMove(from: Int, to: Int): Boolean

        /**
         * Calls inside [clearView] for description drag result.
         */
        fun onTouchMoveResult()
    }

}