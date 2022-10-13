package sgtmelon.scriptum.cleanup.presentation.adapter.holder

import android.annotation.SuppressLint
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
import sgtmelon.scriptum.cleanup.domain.model.item.InputItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.extension.addOnNextAction
import sgtmelon.scriptum.cleanup.presentation.adapter.RollAdapter
import sgtmelon.scriptum.cleanup.presentation.control.note.input.IInputControl
import sgtmelon.scriptum.databinding.ItemRollWriteBinding
import sgtmelon.scriptum.infrastructure.adapter.callback.ItemDragListener

/**
 * Holder of note roll row edit state, use in [RollAdapter]
 */
// TODO add unbind function
class RollWriteHolder(
    private val binding: ItemRollWriteBinding,
    private val dragListener: ItemDragListener?,
    private val callback: Callback,
    private val inputControl: IInputControl?
) : ParentHolder(binding.root),
    View.OnTouchListener,
    TextWatcher {

    /**
     * Button fro drag
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
            imeOptions = EditorInfo.IME_ACTION_NEXT or EditorInfo.IME_FLAG_NO_FULLSCREEN

            addOnNextAction { callback.onRollActionNext() }
            addTextChangedListener(this@RollWriteHolder)
            setOnTouchListener(this@RollWriteHolder)
        }
        dragView.setOnTouchListener(this)
    }

    fun bind(item: RollItem) {
        inputControl?.isEnabled = false

        // TODO remove databinding and use only view binding
        binding.apply {
            this.item = item
            this.descText = item.text
        }.executePendingBindings()

        inputControl?.isEnabled = true
    }

    /**
     * TODO #ERROR error on fast add/remove
     * java.lang.IndexOutOfBoundsException: setSpan (6 ... 6) ends beyond length 5
     */
    fun setSelections(@IntRange(from = 0) position: Int) = rollEnter.apply {
        requestFocus()
        setSelection(if (position > text.toString().length) text.toString().length else position)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            dragListener?.setDrag(v.id == dragView.id)
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
            val absolutePosition = callback.getAbsolutePosition(adapterPosition) ?: return
            inputControl?.onRollChange(absolutePosition, it, textTo, cursorItem)

            textFrom = textTo
            cursorFrom = cursorTo
        }
    }

    override fun afterTextChanged(s: Editable) {
        val text = s.toString()

        callback.onInputRollChange(adapterPosition, text)
        binding.apply { descText = text }.executePendingBindings()
    }

    interface Callback {
        fun onInputRollChange(p: Int, text: String)
        fun getAbsolutePosition(adapterPosition: Int): Int?
        fun onRollActionNext()
    }

}