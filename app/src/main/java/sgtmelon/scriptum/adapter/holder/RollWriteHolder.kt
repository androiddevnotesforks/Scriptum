package sgtmelon.scriptum.adapter.holder

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.annotation.IntRange
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.R
import sgtmelon.scriptum.adapter.RollAdapter
import sgtmelon.scriptum.control.input.InputCallback
import sgtmelon.scriptum.databinding.ItemRollWriteBinding
import sgtmelon.scriptum.listener.ItemListener
import sgtmelon.scriptum.model.item.InputItem
import sgtmelon.scriptum.room.entity.RollItem

/**
 * Держатель пункта списка в состоянии редактирования для [RollAdapter]
 *
 * @author SerjantArbuz
 */
class RollWriteHolder(private val binding: ItemRollWriteBinding,
                      private val dragListener: ItemListener.Drag,
                      private val changeCallback: RollChange,
                      private val inputCallback: InputCallback
) : RecyclerView.ViewHolder(binding.root),
        View.OnTouchListener,
        TextWatcher {

    /**
     * Кнопка для перетаскивания
     */
    private val dragView: View = itemView.findViewById(R.id.roll_write_drag_button)
    private val rollEnter: EditText = itemView.findViewById(R.id.roll_write_enter)

    init {
        rollEnter.apply {
            setRawInputType(InputType.TYPE_CLASS_TEXT
                    or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                    or InputType.TYPE_TEXT_FLAG_AUTO_CORRECT
                    or InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE
            )
            imeOptions = EditorInfo.IME_ACTION_DONE or EditorInfo.IME_FLAG_NO_FULLSCREEN

            addTextChangedListener(this@RollWriteHolder)
            setOnTouchListener(this@RollWriteHolder)
        }
        dragView.setOnTouchListener(this)
    }

    fun bind(rollItem: RollItem) = inputCallback.makeNotEnabled {
        binding.apply { this.rollItem = rollItem }.executePendingBindings()
    }

    /**
     * TODO ошибка при быстром добавлении / удалении
     * java.lang.IndexOutOfBoundsException: setSpan (6 ... 6) ends beyond length 5
     */

    fun setSelections(@IntRange(from = 0) position: Int) = rollEnter.apply {
        requestFocus()
        setSelection(if (position > text.toString().length) text.toString().length else position)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            dragListener.setDrag(v.id == dragView.id)
        }
        return false
    }

    private var textFrom: String? = null
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

        textFrom?.let {
            val cursorItem = InputItem.Cursor(cursorFrom, cursorTo)
            inputCallback.onRollChange(adapterPosition, it, textTo, cursorItem)

            textFrom = textTo
            cursorFrom = cursorTo
        }
    }

    override fun afterTextChanged(s: Editable) =
            changeCallback.onResultInputRollChange(adapterPosition, s.toString())

    interface RollChange {
        fun onResultInputRollChange(p: Int, text: String)
    }

}