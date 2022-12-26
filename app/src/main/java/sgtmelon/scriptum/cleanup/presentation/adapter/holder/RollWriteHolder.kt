package sgtmelon.scriptum.cleanup.presentation.adapter.holder

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import androidx.annotation.IntRange
import androidx.recyclerview.widget.RecyclerView
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.extension.addOnNextAction
import sgtmelon.scriptum.cleanup.presentation.adapter.RollAdapter
import sgtmelon.scriptum.data.noteHistory.HistoryAction
import sgtmelon.scriptum.data.noteHistory.HistoryChange
import sgtmelon.scriptum.data.noteHistory.NoteHistory
import sgtmelon.scriptum.databinding.ItemRollWriteBinding
import sgtmelon.scriptum.infrastructure.adapter.callback.ItemDragListener
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentHolder
import sgtmelon.scriptum.infrastructure.adapter.touch.DragTouchListener

/**
 * Holder of note roll row edit state, use in [RollAdapter]
 */
// TODO add unbind function
class RollWriteHolder(
    private val binding: ItemRollWriteBinding,
    dragListener: ItemDragListener,
    private val callback: Callback,
    private val history: NoteHistory?
) : ParentHolder(binding.root),
    TextWatcher {

    init {
        val touchListener = DragTouchListener(dragListener, binding.dragButton)

        binding.textEnter.apply {
            setRawInputType(
                InputType.TYPE_CLASS_TEXT
                        or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                        or InputType.TYPE_TEXT_FLAG_AUTO_CORRECT
                        or InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE
            )
            imeOptions = EditorInfo.IME_ACTION_NEXT or EditorInfo.IME_FLAG_NO_FULLSCREEN

            addOnNextAction { callback.onRollActionNext() }
            addTextChangedListener(this@RollWriteHolder)
            setOnTouchListener(touchListener)
        }

        binding.dragButton.setOnTouchListener(touchListener)
    }

    fun bind(item: RollItem) {
        history?.isEnabled = false

        // TODO remove databinding and use only view binding
        binding.apply {
            this.item = item
            this.descText = item.text
        }.executePendingBindings()

        history?.isEnabled = true
    }

    /**
     * TODO #ERROR error on fast add/remove
     * java.lang.IndexOutOfBoundsException: setSpan (6 ... 6) ends beyond length 5
     */
    fun setSelections(@IntRange(from = 0) position: Int) = binding.textEnter.apply {
        requestFocus()
        setSelection(if (position > text.toString().length) text.toString().length else position)
    }

    // TODO may be somehow apply HistoryTextWatcher?

    private var valueFrom: String = ""
    private var cursorFrom = 0

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        valueFrom = s.toString()
        cursorFrom = binding.textEnter.selectionEnd
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        val valueTo = s.toString()
        val cursorTo = binding.textEnter.selectionEnd

        if (valueFrom == valueTo) return

        val position = adapterPosition
        if (position != RecyclerView.NO_POSITION) {
            val absolutePosition = callback.getAbsolutePosition(position)
            if (absolutePosition != null) {
                val action = HistoryAction.Roll.Enter(
                    absolutePosition,
                    HistoryChange(valueFrom, valueTo),
                    HistoryChange(cursorFrom, cursorTo)
                )
                history?.add(action)
            }
        }

        valueFrom = valueTo
        cursorFrom = cursorTo
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