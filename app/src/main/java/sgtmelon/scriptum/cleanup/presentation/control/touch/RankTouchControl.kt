package sgtmelon.scriptum.cleanup.presentation.control.touch

import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.infrastructure.adapter.callback.ItemDragListener
import sgtmelon.scriptum.infrastructure.adapter.touch.EdgeDragTouchHelper
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankFragment
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankViewModelImpl

/**
 * Control drag for [RankFragment], setup in [RankViewModelImpl]
 */
// TODO make common parent for roll and rank touch controls
class RankTouchControl(private val callback: Callback) : EdgeDragTouchHelper(callback),
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
        val swipeFlags = getSwipe(isEnabled = false)

        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

        if (dragFrom != RecyclerView.NO_POSITION) return

        if (actionState.isDrag()) {
            callback.onTouchDragStart()
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
            callback.onTouchMoveResult()
            dragFrom = RecyclerView.NO_POSITION
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

    interface Callback : EdgeDragCallback {
        /**
         * Calls when user start make drag, inside [getMovementFlags].
         * Need for check permission for drag.
         *
         * @return true if user can drag cards.
         */
        fun onTouchGetDrag(): Boolean

        /**
         * Calls when user only start drag item.
         */
        fun onTouchDragStart()

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