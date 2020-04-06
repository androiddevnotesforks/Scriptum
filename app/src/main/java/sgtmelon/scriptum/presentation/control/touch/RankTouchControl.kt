package sgtmelon.scriptum.presentation.control.touch

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.presentation.listener.ItemListener
import sgtmelon.scriptum.presentation.screen.ui.impl.main.RankFragment
import sgtmelon.scriptum.presentation.screen.vm.impl.main.RankViewModel

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
         * Calls when user start make drag, inside [getMovementFlags].
         * Need for check permission for drag.
         *
         * @return true if user can drag cards.
         */
        fun onTouchDrag(): Boolean

        /**
         * Calls when user hold rank card and move it between another cards, inside [onMove].
         * Need for update items positions and call adapter notifyItemMoved.
         *
         * @return true if user cad drag card to another position.
         */
        fun onTouchMove(from: Int, to: Int): Boolean

        /**
         * Calls only after user cancel hold need update positions, inside [clearView].
         * Need for description drag result.
         */
        fun onTouchMoveResult()
    }

}