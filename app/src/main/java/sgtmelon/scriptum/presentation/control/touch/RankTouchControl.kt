package sgtmelon.scriptum.presentation.control.touch

import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.presentation.listener.ItemListener
import sgtmelon.scriptum.presentation.screen.ui.impl.main.RankFragment
import sgtmelon.scriptum.presentation.screen.vm.impl.main.RankViewModel

/**
 * Control drag for [RankFragment], setup in [RankViewModel]
 */
class RankTouchControl(private val callback: Callback) : EdgeDragTouchHelper(callback),
    ItemListener.Drag {

    /**
     * Variable for control press section. If user use long press on view which don't uses
     * for drag it will be false. More information you can find inside usage links for [setDrag].
     */
    private var mayDrag = false

    override fun setDrag(mayDrag: Boolean) {
        this.mayDrag = mayDrag
    }

    private var dragFrom: Int = RecyclerView.NO_POSITION

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = getDrag(isEnabled = callback.onTouchGetDrag() && mayDrag)
        val swipeFlags = getSwipe(isEnabled = false)

        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

        if (actionState.isDrag()) {
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

    override fun onMove(
        recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        super.onMove(recyclerView, viewHolder, target)

        return callback.onTouchMove(viewHolder.adapterPosition, movePosition)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) = Unit

    interface Callback : ParentCallback {
        /**
         * Calls when user start make drag, inside [getMovementFlags].
         * Need for check permission for drag.
         *
         * @return true if user can drag cards.
         */
        fun onTouchGetDrag(): Boolean

        /**
         * Calls when user rank card and move it between another cards, inside [onMove].
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