package sgtmelon.scriptum.app.adapter.holder

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.annotation.IntRange
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.adapter.RollAdapter
import sgtmelon.scriptum.app.control.input.InputCallback
import sgtmelon.scriptum.app.control.touch.BindIntf
import sgtmelon.scriptum.app.model.item.CursorItem
import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.databinding.ItemRollWriteBinding
import sgtmelon.scriptum.office.intf.ItemListener

/**
 * Держатель пункта списка в состоянии редактирования для [RollAdapter]
 */
@SuppressLint("ClickableViewAccessibility")
class RollWriteHolder(private val binding: ItemRollWriteBinding,
                      private val dragListener: ItemListener.DragListener,
                      private val rollWatcher: ItemListener.RollWatcher,
                      private val inputCallback: InputCallback,
                      private val bindIntf: BindIntf
) : RecyclerView.ViewHolder(binding.root), View.OnTouchListener, TextWatcher {

    /**
     * Кнопка для перетаскивания
     */
    private val dragView: View = itemView.findViewById(R.id.roll_write_drag_button)
    private val rollEnter: EditText = itemView.findViewById(R.id.roll_write_enter)

    init {
        rollEnter.addTextChangedListener(this)

        rollEnter.setOnTouchListener(this)
        dragView.setOnTouchListener(this)
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

        if (!TextUtils.isEmpty(textTo)) {
            if (!TextUtils.isEmpty(textFrom)) {
                val cursorItem = CursorItem(cursorFrom, cursorTo)
                inputCallback.onRollChange(adapterPosition, textFrom, textTo, cursorItem)

                textFrom = textTo
                cursorFrom = cursorTo
            }
        } else {
            inputCallback.onRollRemove(adapterPosition, binding.rollItem.toString())
        }

        bindIntf.bindInput()
    }

    override fun afterTextChanged(s: Editable) {
        if (adapterPosition == RecyclerView.NO_POSITION) return

        if (!TextUtils.isEmpty(textFrom)) {
            rollWatcher.afterRollChanged(adapterPosition, rollEnter.text.toString())
        }

        if (inputCallback.isChangeEnabled) {
            inputCallback.setEnabled(true)
        }
    }

}