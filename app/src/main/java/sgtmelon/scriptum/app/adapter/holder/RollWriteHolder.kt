package sgtmelon.scriptum.app.adapter.holder

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.annotation.IntRange
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.RollAdapter
import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.databinding.ItemRollWriteBinding
import sgtmelon.scriptum.office.intf.ItemIntf

/**
 * Держатель пункта списка в состоянии редактирования для [RollAdapter]
 */
@SuppressLint("ClickableViewAccessibility")
class RollWriteHolder(private val binding: ItemRollWriteBinding,
                      private val dragListener: ItemIntf.DragListener) : RecyclerView.ViewHolder(binding.root), View.OnTouchListener {

    /**
     * Кнопка для перетаскивания
     */
    val clickView: View = itemView.findViewById(R.id.click_button)
    val rollEnter: EditText = itemView.findViewById(R.id.roll_enter)

    init {
        rollEnter.setOnTouchListener(this)
        clickView.setOnTouchListener(this)
    }

    fun bind(rollItem: RollItem) {
        binding.rollItem = rollItem
        binding.executePendingBindings()
    }

    fun setSelections(@IntRange(from = 0) position: Int) {
        rollEnter.requestFocus()
        rollEnter.setSelection(position)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            dragListener.setDrag(v.id == R.id.click_button)
        }
        return false
    }

}