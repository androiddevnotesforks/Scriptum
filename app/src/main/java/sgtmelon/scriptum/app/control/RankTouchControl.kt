package sgtmelon.scriptum.app.control

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.app.adapter.RankAdapter
import sgtmelon.scriptum.app.view.fragment.main.RankFragment
import sgtmelon.scriptum.app.vm.fragment.main.RankViewModel
import sgtmelon.scriptum.office.intf.ItemIntf

/**
 * Управление перетаскиванием для [RankFragment]
 */
class RankTouchControl(private val vm: RankViewModel) : ItemTouchHelper.Callback(),
        ItemIntf.DragListener {

    lateinit var adapter: RankAdapter

    private var drag = false

    override fun setDrag(drag: Boolean) {
        this.drag = drag
    }

    private var dragFrom: Int = RecyclerView.NO_POSITION

    override fun getMovementFlags(recyclerView: RecyclerView,
                                  viewHolder: RecyclerView.ViewHolder): Int {
        val flagsDrag = when (drag) {
            true -> ItemTouchHelper.UP or ItemTouchHelper.DOWN
            false -> 0
        }

        val flagsSwipe = 0

        return ItemTouchHelper.Callback.makeMovementFlags(flagsDrag, flagsSwipe)
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
            adapter.notifyDataSetChanged(vm.onUpdateDrag(dragFrom, dragTo))
        }
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder): Boolean {
        val positionFrom = viewHolder.adapterPosition
        val positionTo = target.adapterPosition

        adapter.setList(vm.onUpdateMove(positionFrom, positionTo))
        adapter.notifyItemMoved(positionFrom, positionTo)

        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

}