package sgtmelon.scriptum.cleanup.presentation.adapter.holder

import android.annotation.SuppressLint
import android.text.InputType
import android.view.inputmethod.EditorInfo
import androidx.annotation.IntRange
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.extension.addOnNextAction
import sgtmelon.scriptum.cleanup.presentation.control.note.input.watcher.HistoryTextWatcher
import sgtmelon.scriptum.data.noteHistory.HistoryAction
import sgtmelon.scriptum.data.noteHistory.NoteHistory
import sgtmelon.scriptum.databinding.ItemRollWriteBinding
import sgtmelon.scriptum.infrastructure.adapter.callback.ItemDragListener
import sgtmelon.scriptum.infrastructure.adapter.callback.UnbindCallback
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentHolder
import sgtmelon.scriptum.infrastructure.adapter.touch.DragTouchListener

/**
 * Holder of roll item in edit state.
 */
@SuppressLint("ClickableViewAccessibility")
class RollWriteHolder(
    private val binding: ItemRollWriteBinding,
    dragListener: ItemDragListener,
    private val callback: Callback,
    private val history: NoteHistory?
) : ParentHolder(binding.root),
    UnbindCallback,
    HistoryTextWatcher.Callback {

    private val textWatcher = HistoryTextWatcher(
        binding.textEnter, callback = this
    ) { value, cursor ->
        checkPosition {
            val absolutePosition = callback.getAbsolutePosition(it) ?: return@checkPosition
            history?.add(HistoryAction.Roll.Enter(absolutePosition, value, cursor))
        }
    }

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
            addTextChangedListener(textWatcher)
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

    override fun onHistoryEnterChanged(text: String) {
        checkPosition { callback.onInputRollChange(it, text) }
        binding.apply { descText = text }.executePendingBindings()
    }

    override fun unbind() {
        binding.textEnter.setOnTouchListener(null)
        binding.textEnter.removeTextChangedListener(textWatcher)
        binding.dragButton.setOnTouchListener(null)
    }

    interface Callback {
        fun onInputRollChange(p: Int, text: String)
        fun getAbsolutePosition(adapterPosition: Int): Int?
        fun onRollActionNext()
    }

}