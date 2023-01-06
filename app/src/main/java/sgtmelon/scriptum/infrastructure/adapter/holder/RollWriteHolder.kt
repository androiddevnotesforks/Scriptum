package sgtmelon.scriptum.infrastructure.adapter.holder

import android.annotation.SuppressLint
import android.text.InputType
import android.view.inputmethod.EditorInfo
import androidx.annotation.IntRange
import java.lang.Integer.min
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.cleanup.extension.addOnNextAction
import sgtmelon.scriptum.cleanup.extension.bindBoolTint
import sgtmelon.scriptum.cleanup.extension.bindTextColor
import sgtmelon.scriptum.data.noteHistory.HistoryAction
import sgtmelon.scriptum.data.noteHistory.NoteHistory
import sgtmelon.scriptum.databinding.ItemRollWriteBinding
import sgtmelon.scriptum.infrastructure.adapter.callback.ItemDragListener
import sgtmelon.scriptum.infrastructure.adapter.callback.UnbindCallback
import sgtmelon.scriptum.infrastructure.adapter.parent.ParentHolder
import sgtmelon.scriptum.infrastructure.adapter.touch.DragTouchListener
import sgtmelon.scriptum.infrastructure.listener.HistoryTextWatcher

/**
 * Holder of roll item in edit state.
 */
@SuppressLint("ClickableViewAccessibility")
class RollWriteHolder(
    private val binding: ItemRollWriteBinding,
    dragListener: ItemDragListener,
    private val callback: Callback,
    private val history: NoteHistory
) : ParentHolder(binding.root),
    UnbindCallback,
    HistoryTextWatcher.Callback {

    private val textWatcher = HistoryTextWatcher(
        binding.textEnter, callback = this
    ) { value, cursor ->
        checkPosition {
            val position = callback.getAbsolutePosition(it) ?: return@checkPosition
            return@HistoryTextWatcher HistoryAction.Roll.Enter(position, value, cursor)
        }

        return@HistoryTextWatcher null
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
        history.saveChanges = false

        binding.dragButton.bindBoolTint(item.isCheck, R.attr.clAccent, R.attr.clContent)
        bindContentDescription(item.text)
        binding.textEnter.setText(item.text)
        binding.textEnter.bindTextColor(!item.isCheck, R.attr.clContent, R.attr.clContrast)

        history.saveChanges = true
    }

    private fun bindContentDescription(text: String) {
        binding.dragButton.contentDescription =
            context.getString(R.string.description_item_roll_move, text)
    }

    /**
     * TODO #ERROR error on fast add/remove
     * java.lang.IndexOutOfBoundsException: setSpan (6 ... 6) ends beyond length 5
     */
    fun setSelections(@IntRange(from = 0) position: Int) = with(binding.textEnter) {
        requestFocus()
        setSelection(min(position, text.toString().length))
    }

    override fun onHistoryAdd(action: HistoryAction) = history.add(action)

    override fun onHistoryEnterChanged(text: String) {
        checkPosition { callback.onRollEnterChanged(it, text) }
        bindContentDescription(text)
    }

    override fun unbind() {
        binding.textEnter.setOnTouchListener(null)
        binding.textEnter.removeTextChangedListener(textWatcher)
        binding.dragButton.setOnTouchListener(null)
    }

    interface Callback {
        fun getAbsolutePosition(adapterPosition: Int): Int?
        fun onRollEnterChanged(p: Int, text: String)
        fun onRollActionNext()
    }
}