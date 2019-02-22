package sgtmelon.scriptum.app.control.touch

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.app.adapter.RollAdapter
import sgtmelon.scriptum.app.view.NoteCallback
import sgtmelon.scriptum.app.view.RollNoteFragment
import sgtmelon.scriptum.app.vm.RollNoteViewModel
import sgtmelon.scriptum.office.intf.BindIntf
import sgtmelon.scriptum.office.intf.InputIntf
import sgtmelon.scriptum.office.intf.ItemIntf

/**
 * Управление перетаскиванием для [RollNoteFragment]
 */
class RollTouchControl(private val vm: RollNoteViewModel,
                       private val noteCallback: NoteCallback,
                       private val inputIntf: InputIntf,
                       private val bindIntf: BindIntf
) : ItemTouchHelper.Callback(),
        ItemIntf.DragListener {

    lateinit var adapter: RollAdapter

    private var drag = false

    override fun setDrag(drag: Boolean) {
        this.drag = drag
    }

    private var dragFrom = RecyclerView.NO_POSITION

    override fun getMovementFlags(recyclerView: RecyclerView,
                                  viewHolder: RecyclerView.ViewHolder): Int {
        val isEdit = noteCallback.viewModel.noteState.isEdit

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

        inputIntf.onRollMove(dragFrom, dragTo)
        bindIntf.bindInput()

        dragFrom = RecyclerView.NO_POSITION
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
        val p = viewHolder.adapterPosition
        val listRoll = vm.noteRepo.listRoll

        inputIntf.onRollRemove(p, listRoll[p].toString())
        bindIntf.bindInput()

        listRoll.removeAt(p)

        adapter.notifyItemRemoved(p, listRoll)
    }


    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder): Boolean {
        val positionFrom = viewHolder.adapterPosition
        val positionTo = target.adapterPosition

        val listRoll = vm.noteRepo.listRoll
        val rollItem = listRoll[positionFrom]

        listRoll.removeAt(positionFrom)
        listRoll.add(positionTo, rollItem)

        adapter.setList(listRoll)
        adapter.notifyItemMoved(positionFrom, positionTo)

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


}