package sgtmelon.scriptum.app.adapter.holder

import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.annotation.IntRange
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.RollAdapter
import sgtmelon.scriptum.app.control.input.InputCallback
import sgtmelon.scriptum.app.model.item.CursorItem
import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.databinding.ItemRollWriteBinding
import sgtmelon.scriptum.office.intf.ItemListener

/**
 * Держатель пункта списка в состоянии редактирования для [RollAdapter]
 */
class RollWriteHolder(private val binding: ItemRollWriteBinding,
                      private val dragListener: ItemListener.DragListener,
                      private val textChangeCallback: TextChange,
                      private val inputCallback: InputCallback
) : RecyclerView.ViewHolder(binding.root), View.OnTouchListener, TextWatcher {

    /**
     * Кнопка для перетаскивания
     */
    private val dragView: View = itemView.findViewById(R.id.roll_write_drag_button)
    private val rollEnter: EditText = itemView.findViewById(R.id.roll_write_enter)

    init {
        rollEnter.apply {
            addTextChangedListener(this@RollWriteHolder)
            setOnTouchListener(this@RollWriteHolder)
        }
        dragView.setOnTouchListener(this)
    }

    fun bind(rollItem: RollItem) = binding.apply { this.rollItem = rollItem }.executePendingBindings()

    fun setSelections(@IntRange(from = 0) position: Int) = rollEnter.apply {
        requestFocus()
        setSelection(position)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            dragListener.setDrag(v.id == dragView.id)
        }
        return false
    }

    private var textFrom = ""
    private var cursorFrom = 0

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        textFrom = s.toString()
        cursorFrom = rollEnter.selectionEnd
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (adapterPosition == RecyclerView.NO_POSITION) return

        val textTo = s.toString()
        val cursorTo = rollEnter.selectionEnd

        if (textFrom == textTo) return

        if (textTo.isNotEmpty()) {
            if (textFrom.isNotEmpty()) {
                val cursorItem = CursorItem(cursorFrom, cursorTo)
                inputCallback.onRollChange(adapterPosition, textFrom, textTo, cursorItem)

                textFrom = textTo
                cursorFrom = cursorTo
            }
        } else {
            inputCallback.onRollRemove(adapterPosition, binding.rollItem.toString())
        }

        textChangeCallback.onResultInputRollChange()
    }

    override fun afterTextChanged(s: Editable) {
        if (adapterPosition == RecyclerView.NO_POSITION) return

        if (textFrom.isNotEmpty()) {
            textChangeCallback.onResultInputRollAfter(adapterPosition, rollEnter.text.toString())
        }

        if (inputCallback.isChangeEnabled) {
            inputCallback.setEnabled(true)
        }
    }

    interface TextChange {
        fun onResultInputRollChange()
        fun onResultInputRollAfter(p: Int, text: String)
    }

}